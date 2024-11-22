import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@Provider("ArticlesProvider")
@PactFolder("target/pacts")
public class PactProviderTests extends BaseTest {

    @State("test state") // This must match the state in the Pact file
    public void setupTestState() {
        // Setup logic for "test state"
        System.out.println("Setting up provider for 'test state'");
        // Example: Add mock data, configure the service, etc.
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        mockServerCreator.createMockServer();
        mockServerCreator.createGetStub("/articles.json", pactDslJSONBodyLamda_JsonInsideArray().toString());
        mockServerCreator.createPostStub("/hello", pactDslJSONBodyLamda_ArrayInsideJson().toString(), pactDslJSONBodyLamda_ArrayInsideJson().toString());
        context.verifyInteraction();
        mockServerCreator.stopMockServer();
    }

}
