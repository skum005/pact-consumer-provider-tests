import lombok.Getter;

@Getter
public class TestContext {

    private RestClient restClient;
    private MockServerCreator mockServerCreator;

    public TestContext() {
        restClient = new RestClient();
        mockServerCreator = new MockServerCreator();
    }

}
