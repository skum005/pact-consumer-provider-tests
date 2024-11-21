import lombok.Getter;

@Getter
public class TestContext {

    private RestClient restClient;

    public TestContext() {
        restClient = new RestClient();
    }

}
