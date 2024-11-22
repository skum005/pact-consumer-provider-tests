import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockServerCreator {

    private WireMockServer wireMockServer;
    public static final String MOCK_SERVER_HOST = "localhost";
    public static final String MOCK_SERVER_PORT = "9999";
    public static final String SCHEME = "http";
    public static final String MOCK_SERVER = SCHEME + "://" + MOCK_SERVER_HOST + ":" + MOCK_SERVER_PORT;

    public void createMockServer() {
        try {
            wireMockServer = new WireMockServer();
            configureFor("localhost", 8080);
            wireMockServer.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to start mockserver" + exception.getMessage());
        }
    }

    public void stubAllOkay() {
        try {
            stubFor(any(anyUrl()).willReturn(ok()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void createPostStub(String basePath, String requestBody, String responseBody) {
        try {
            stubFor(
                    post(urlPathEqualTo(basePath))
                            .withRequestBody(equalToJson(requestBody))
                            .withHeader("Content-Type", equalTo("application/json; charset=UTF-8"))
                            //.withBasicAuth("", "")
                            //.withQueryParams(queryParam)
                            .willReturn(okJson(responseBody).withStatus(201)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void createGetStub(String basePath, String responseJson) {
        try {
            stubFor(
                    get(urlPathEqualTo(basePath))
                            //.withHeader("", "")
                            //.withBasicAuth("", "")
                            //.withQueryParams(queryParam)
                            .willReturn(okJson(responseJson).withStatus(200)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void stopMockServer() {
        try {
            if(wireMockServer != null)
                wireMockServer.stop();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
