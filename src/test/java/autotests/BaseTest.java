package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = EndpointConfig.class)
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    protected void sendGetRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get(path));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .delete(path));
    }

    protected void sendPutRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .put(path)
                        .message());
    }

    protected void sendPostRequest(TestCaseRunner runner, HttpClient URL, String path,  Object body) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .post(path)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    protected void validateResponse(TestCaseRunner runner, HttpClient URL, HttpStatus expectedStatus, String expectedMessage) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(expectedMessage));
    }

    protected void validateResponse(TestCaseRunner runner, HttpClient URL, Object expectedPayload) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    protected void validateResponse(TestCaseRunner runner, HttpClient URL, String expectedPayload) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }


}
