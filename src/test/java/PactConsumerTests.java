import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Using Junit5 Pact Consumer Library
 */

@PactConsumerTest
@PactTestFor(providerName = "ArticlesProvider", hostInterface="localhost")
public class PactConsumerTests {

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
                .body("{\"responsetest\": true}").toPact(V4Pact.class);
    }

    @Pact(provider="ArticlesProvider", consumer="test_consumer")
    public V4Pact pactForTestConsumer_MultipleInteractions(PactDslWithProvider builder) {
        return builder
                .given("test state")
                .uponReceiving("Post request - interaction 1")
                .path("/hello")
                //interaction 1
                .body("{\"name\": \"harry\"}")
                .method("POST")
                .willRespondWith()
                .status(201)
                .body("{\"responsetest\": true}")
                //interaction 2
                .uponReceiving("post request - interaction 2")
                .path("/hello")
                .method("POST")
                .body("{\"name\": \"harry\"}")
                .willRespondWith()
                .status(201)
                .body("{\"responsetest\": true}")
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "pactForTestConsumer_SingleInteraction")
    @Story("GET Endpoint Interactions")
    @Description("Create PACT file for \"pactConfigForSomeEndpoint\" and run the tests against the same")
    public void consumerTest1(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();
        Allure.attachment("Mock Server URL - test 1", mockServer.getUrl());
        Response response = RestAssured.given().get("/articles.json");
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    @PactTestFor(pactMethod = "pactForTestConsumer_MultipleInteractions")
    @Story("POST Endpoint Interactions")
    @Description("Create PACT file for \"pactConfigForSomeEndpoint\" and run the tests against the same")
    public void consumerTest2(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();
        JSONObject jsonObj = new JSONObject()
                .put("name","harry");
        Allure.attachment("Mock Server URL - test 2", mockServer.getUrl());
        Response response = RestAssured.given().contentType("application/json").body(jsonObj.toString()).when().post("/hello");
        Assertions.assertEquals(201, response.getStatusCode());
    }

    // to generate allure report use the below command:
    // allure generate allure-results --clean -o allure-report
}
