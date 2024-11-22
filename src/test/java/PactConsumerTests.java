import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Using Junit5 Pact Consumer Library
 */

@PactConsumerTest
@PactTestFor(providerName = "ArticlesProvider", hostInterface="localhost")
public class PactConsumerTests extends BaseTest {

    @Pact(provider="ArticlesProvider", consumer="test_consumer")
    public V4Pact pactForTestConsumer_SingleInteraction(PactDslWithProvider builder) {
        return builder
                .given("test state")
                .uponReceiving("get request - single interaction")
                .path("/articles.json")
                //.body("{\"name\": \"harry\"}")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(pactDslJSONBodyLamda_JsonInsideArray()).toPact(V4Pact.class);
    }

    @Pact(provider="ArticlesProvider", consumer="test_consumer")
    public V4Pact pactForTestConsumer_MultipleInteractions(PactDslWithProvider builder) {
        return builder
                .given("test state")
                .uponReceiving("Post request - interaction 1")
                .path("/hello")
                //interaction 1
                .body(pactDslJSONBodyLamda_ArrayInsideJson())
                .method("POST")
                .willRespondWith()
                .status(201)
                .body(pactDslJSONBodyLamda_ArrayInsideJson())
                //interaction 2
                .uponReceiving("post request - interaction 2")
                .path("/hello")
                .method("POST")
                .body(pactDslJSONBodyLamda_ArrayInsideJson())
                .willRespondWith()
                .status(201)
                .body(pactDslJSONBodyLamda_ArrayInsideJson())
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "pactForTestConsumer_SingleInteraction")
    @Story("GET Endpoint Interactions")
    @Description("Create PACT file for \"pactConfigForSomeEndpoint\" and run the tests against the same")
    public void consumerTest1(MockServer mockServer) {
        Allure.attachment("Mock Server URL - test 1", mockServer.getUrl());
        Response response = restClient.submitRequest(restClient.generateRequestSpec(mockServer.getUrl(), "/articles.json"), Method.GET);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    @PactTestFor(pactMethod = "pactForTestConsumer_MultipleInteractions")
    @Story("POST Endpoint Interactions")
    @Description("Create PACT file for \"pactConfigForSomeEndpoint\" and run the tests against the same")
    public void consumerTest2(MockServer mockServer) {
        JSONObject requestBody = new JSONObject()
                .put("name","harry");
        String reqBody = "{\n" +
                "  \"name\": \"Santosh\",\n" +
                "  \"age\": 37,\n" +
                "  \"isRetired\": false,\n" +
                "  \"address\": [\"Flat 100\"]\n" +
                "}\n";
        Map<String, String> headers = new HashMap<String, String> (
                Map.of("Content-Type","application/json; charset=UTF-8"));
        Allure.attachment("Mock Server URL - test 2", mockServer.getUrl());
        Response response = restClient.submitRequest(restClient.generateRequestSpec(mockServer.getUrl(), "/hello", headers, null, reqBody), Method.POST);
        Assertions.assertEquals(201, response.getStatusCode());
    }

    // to generate allure report use the below command:
    // allure generate allure-results --clean -o allure-report
}
