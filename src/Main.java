import com.fastcgi.FCGIInterface;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.net.URLDecoder;

public class Main {

    private final HttpResponse httpResponses;
    private final DateTimeFormatter formatter;
    private final DotChecker dotChecker;

    public Main() {
        this.httpResponses = new HttpResponse();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dotChecker = new DotChecker();
    }

    private JSONObject getParametersFromQuery() throws ValidateException {
        String queryString = System.getProperty("QUERY_STRING");
        if (queryString == null || queryString.isEmpty()) {
            throw new ValidateException("Параметры запроса отсутствуют");
        }

        JSONObject jsonObject = new JSONObject();
        try {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    jsonObject.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new ValidateException("Некорректный формат параметров запроса");
        }
        return jsonObject;
    }

    private String responseError(String httpTemplate, String error) {
        var formattedNow = LocalDateTime.now().format(formatter);
        var json = String.format(httpResponses.ERROR_JSON, formattedNow, error);
        int contentLength = json.getBytes(StandardCharsets.UTF_8).length;
        return String.format(httpTemplate, contentLength, json);
    }

    public void run() {
        var fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            try {
                String requestMethod = System.getProperty("REQUEST_METHOD");
                if (!"GET".equals(requestMethod)) {
                    String response = responseError(httpResponses.HTTP_FORBIDDEN, "Поддерживаются только GET запросы");
                    System.out.print(response);
                    continue;
                }

                String URI = System.getProperty("REQUEST_URI");
                if (URI == null || !URI.contains("/fcgi-bin/web1.jar")) {
                    String response = responseError(httpResponses.HTTP_NOT_FOUND, "Неверный URI");
                    System.out.print(response);
                    continue;
                }

                int pathEndIndex = URI.indexOf("/fcgi-bin/web1.jar");
                String pathAfterWeb1Jar = URI.substring(pathEndIndex + "/fcgi-bin/web1.jar".length());
                if (pathAfterWeb1Jar.contains("/")) {
                    String response = responseError(httpResponses.HTTP_FORBIDDEN, "Запрос с дополнительными путями запрещен");
                    System.out.print(response);
                    continue;
                }

                JSONObject jsonObject = getParametersFromQuery();
                Validator validator = new Validator();
                validator.validate(jsonObject);

                Dto dto = new Dto();
                dto.setAll(jsonObject);

                var startTime = Instant.now();
                boolean result;
                try {
                    result = dotChecker.isInsideArea(dto.getX(), dto.getY(), dto.getR());
                } catch (Exception e) {
                    throw new ValidateException("Ошибка проверки точки");
                }
                var endTime = Instant.now();

                long timeTakenNanos = ChronoUnit.NANOS.between(startTime, endTime);
                String formattedNow = LocalDateTime.now().format(formatter);

                var json = String.format(httpResponses.RESULT_JSON, timeTakenNanos, formattedNow, result);
                int contentLength = json.getBytes(StandardCharsets.UTF_8).length;

                var response = String.format(httpResponses.HTTP_RESPONSE, contentLength, json);
                System.out.print(response);

            } catch (ValidateException e) {
                String response = responseError(httpResponses.HTTP_BAD_REQUEST, e.getMessage());
                System.out.print(response);
            } catch (Exception e) {
                String response = responseError(httpResponses.HTTP_BAD_REQUEST, "Непредвиденная ошибка: " + e.getMessage());
                System.out.print(response);
            }
        }
    }


    public static void main(String[] args) {
        new Main().run();
    }
}
