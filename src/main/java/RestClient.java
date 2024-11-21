import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestClient {

    private RequestSpecification requestSpecification;

    public RequestSpecification generateRequestSpec(String baseURI, String basePath) {
        try {
            return requestSpecification = given().baseUri(baseURI).basePath(basePath);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to generate request specification. " + exception.getMessage());
        }
    }

    public RequestSpecification generateRequestSpec(String baseURI, String basePath, Map<String, String> headers, Map<String, String> params, String body) {
        try {
            generateRequestSpec(baseURI, basePath);
            if(headers != null && !headers.isEmpty())
                requestSpecification = requestSpecification.headers(headers);
            if(body != null && !body.equals(""))
                requestSpecification.body(body);
            if(params != null && !params.isEmpty())
                requestSpecification.params(params);
            return requestSpecification;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to generate request specification. " + exception.getMessage());
        }
    }

    public Response submitRequest(RequestSpecification requestSpecification, Method method) {
        try {
            return requestSpecification.request(method);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to submit request. " + exception.getMessage());
        }
    }

}
