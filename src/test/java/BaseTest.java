import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArray;
import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;

public class BaseTest {

    protected TestContext testContext;
    protected RestClient restClient;
    protected MockServerCreator mockServerCreator;

    public BaseTest() {
        testContext = new TestContext();
        restClient = testContext.getRestClient();
        mockServerCreator = testContext.getMockServerCreator();
    }

    public DslPart pactDslJSONBodyLamda_ArrayInsideJson() {
        // PACT body - lamda. JSON array inside a JSON object
        return newJsonBody(b -> {
            b.stringType("name", "Santosh");
            b.integerType("age", 37);
            b.booleanType("isRetired", false);
            b.array("address", (a -> a.stringValue("Flat 100")));
        }).build();
    }

    public DslPart pactDslJSONBodyLamda_JsonInsideArray() {
        // PACT body - lamda. JSON array inside a JSON object
        return newJsonArray(array -> {
            array.object(obj -> {
                obj.stringType("name", "Santosh");
                obj.integerType("age", 37);
                obj.booleanType("isRetired", false);
                obj.array("address", (a -> a.stringValue("Flat 100")));
            });
        }).build();
    }

    // this is not working. Throwing a null pointer exception
    public DslPart pactDslJsonBody() {
        return new PactDslJsonBody()
                .stringType("name", "Santosh")
                .integerType("age", 37)
                .booleanType("isRetired", false)
                .array("address")
                .stringValue("Flat 100")
                .stringValue("Main Road")
                .closeArray().closeObject();
    }

}
