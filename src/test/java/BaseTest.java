public class BaseTest {

    protected TestContext testContext;
    protected RestClient restClient;

    public BaseTest() {
        testContext = new TestContext();
        restClient = testContext.getRestClient();
    }

}
