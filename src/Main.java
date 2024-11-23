import com.fastcgi.FCGIInterface;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
        String queryString = System.getProperties().getProperty("QUERY_STRING");
        if (queryString == null || queryString.isEmpty()) {
            throw new ValidateException("Параметры запроса отсутствуют");
        }

        JSONObject jsonObject = new JSONObject();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                jsonObject.put(keyValue[0], keyValue[1]);
            }
        }
        return jsonObject;
    }

    private String responseError(String httpTemplate, String error) {
        var formattedNow = LocalDateTime.now().format(formatter);
        var json = String.format(httpResponses.ERROR_JSON, formattedNow, error);
        return String.format(httpTemplate, json.getBytes(StandardCharsets.UTF_8).length, json);
    }

    public void run() {
        var fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            try {
                String requestMethod = System.getProperties().getProperty("REQUEST_METHOD");
                if (!"GET".equals(requestMethod)) {
                    throw new ValidateException("Поддерживаются только GET запросы");
                }

                String URI = System.getProperties().getProperty("REQUEST_URI");
                if (!URI.contains("/fcgi-bin/web1.jar")) {
                    String response = responseError(httpResponses.HTTP_NOT_FOUND, "Неверный URI");
                    System.out.println(response);
                    continue;
                }

                JSONObject jsonObject = getParametersFromQuery(); // Чтение из строки запроса
                Validator validator = new Validator();
                validator.validate(jsonObject);

                Dto dto = new Dto();
                dto.setAll(jsonObject);

                var startTime = Instant.now();
                boolean result = dotChecker.isInsideArea(dto.getX(), dto.getY(), dto.getR());
                var endTime = Instant.now();

                long timeTakenNanos = ChronoUnit.NANOS.between(startTime, endTime);
                String formattedNow = LocalDateTime.now().format(formatter);

                var json = String.format(httpResponses.RESULT_JSON, timeTakenNanos, formattedNow, result);
                var response = String.format(httpResponses.HTTP_RESPONSE, json.getBytes(StandardCharsets.UTF_8).length, json);
                System.out.println(response);

            } catch (ValidateException e) {
                String response = responseError(httpResponses.HTTP_BAD_REQUEST, e.getMessage());
                System.out.println(response);
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
