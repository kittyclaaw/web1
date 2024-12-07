public class HttpResponse {

    public final String HTTP_RESPONSE = """
            HTTP/1.1 200 OK\r
            Content-Type: application/json\r
            Content-Length: %d\r
            \r
            %s
            """;

    public final String HTTP_BAD_REQUEST = """
            HTTP/1.1 400 Bad Request\r
            Content-Type: application/json; charset=UTF-8\r
            Content-Length: %d\r
            \r
            %s
            """;

    public final String HTTP_FORBIDDEN = """
            HTTP/1.1 403 Forbidden\r
            Content-Type: application/json; charset=UTF-8\r
            Content-Length: %d\r
            \r
            %s
            """;

    public final String HTTP_NOT_FOUND = """
            HTTP/1.1 404 Not Found\r
            Content-Type: application/json; charset=UTF-8\r
            Content-Length: %d\r
            \r
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
