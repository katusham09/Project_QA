package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = EndpointConfig.class)
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    //CRUD methods

    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    public void updateDuck(TestCaseRunner runner, String id, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .put("/api/duck/update")
                        .message()
                        .queryParam("id", id)
                        .queryParam("color", color)
                        .queryParam("height", String.valueOf(height))
                        .queryParam("material", material)
                        .queryParam("sound", sound)
                        .queryParam("wingsState", wingsState)
        );
    }

    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id));
    }

    // Methods with actions

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    public void quack(TestCaseRunner runner, String id, String repetitions, String quacks) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/quack")
                        .message()
                        .queryParam("id", id)
                        .queryParam("repetitionCount", repetitions)
                        .queryParam("soundCount", quacks));
    }

    public void propertiesDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id));
    }

    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));
    }

    // Validation

    @Description("Валидация c передачей ответа String’ой")
    public void validateResponse(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n"+ "  \"message\": \"Duck is deleted\"\n"+ "}"));
    }


    @Description("Валидация c передачей ответа из папки Payload")
    public void validateResponse(TestCaseRunner runner, Object expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Description("Валидация c передачей ответа из папки Resources")
    public void validateResponse(TestCaseRunner runner, String expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }

    public void validateErrorResponse(TestCaseRunner runner, HttpStatus expectedStatus, String expectedMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" + "  \"message\": \"" + expectedMessage + "\"\n" + "}"));
    }

    // Additional methods for extracting id and parity

    public void getDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    public void parityCheck(TestCaseRunner runner, String id, String material) {
        runner.$(action -> {
            String duckId = action.getVariable(id);
            if (Integer.parseInt(duckId) % 2 == 0) {
                deleteDuck(runner, duckId);
                Duck duck = new Duck().color("yellow").height(0.03).material(material).sound("quack").wingsState(WingState.ACTIVE);
                createDuck(runner, duck);
                getDuckId(runner);
            }});
    }

    public void oddParityCheck(TestCaseRunner runner, String id, String material) {
        runner.$(action -> {
            String duckId = action.getVariable(id);
            if (Integer.parseInt(duckId) % 2 != 0) {
                deleteDuck(runner, duckId);
                Duck duck = new Duck().color("yellow").height(0.03).material(material).sound("quack").wingsState(WingState.ACTIVE);
                createDuck(runner, duck);
                getDuckId(runner);
            }});
    }
}
