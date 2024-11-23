public class HttpResponse {

    public final String HTTP_RESPONSE = """
            Content-Type: application/json
            Content-Length: %d

            %s
            """;


    public final String HTTP_BAD_REQUEST = """
            HTTP/1.1 400 Bad Request
            Content-Type: application/json
            Content-Length: %d

            %s
            """;

    public final String HTTP_NOT_FOUND = """
            HTTP/1.1 404 Not Found
            Content-Type: application/json
            Content-Length: %d

            %s
            """;

    public final String RESULT_JSON = """
            {
                "time": "%s нс",
                "now": "%s",
                "result": %b
            }
            """;

    public final String ERROR_JSON = """
            {
                "now": "%s",
                "reason": "%s"
            }
            """;
}
