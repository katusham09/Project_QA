package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.actions.ExecuteSQLAction;
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
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Random;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = EndpointConfig.class)
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    //CRUD methods

    @Step("Создание уточки по эндпоинту")
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

    @Step("Создание уточки через БД")
    public ExecuteSQLAction createDuck(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        return runner.$(
                sql(testDb)
                        .statement("INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE) VALUES ("
                                + id + ", '"
                                + color + "', "
                                + height + ", '"
                                + material + "', '"
                                + sound + "', '"
                                + wingsState + "')")
        );
    }

    @Step("Изменение уточки")
    public void updateDuck(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
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

    @Step("Удаление уточки по эндпоинту")
    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id));
    }

    @Step("Удаление уточки через БД")
    public ExecuteSQLAction deleteDuckBD(TestCaseRunner runner, String id) {
        return runner.$(sql(testDb).statement("DELETE FROM DUCK WHERE ID=" + id));
    }

    // Methods with actions

    @Step("Уточка плывёт")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    @Step("Уточка крякает")
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

    @Step("Получение характеристик уточки")
    public void propertiesDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id));
    }

    @Step("Уточка летает")
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));
    }

    // Validation

    @Step("Валидация с передачей ответа String'ой")
    @Description("Валидация c передачей ответа String’ой")
    public void validateResponse(TestCaseRunner runner, HttpStatus expectedStatus, String expectedMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(expectedMessage));
    }

    @Step("Валидация с передачей ответа из папки Payload")
    @Description("Валидация c передачей ответа из папки Payload")
    public void validateResponse(TestCaseRunner runner, Object expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Step("Валидация с передачей ответа из папки Resources")
    @Description("Валидация c передачей ответа из папки Resources")
    public void validateResponse(TestCaseRunner runner, String expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }

    @Step("Валидация ответа через БД")
    protected void validateDuckInDatabase(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        runner.$(query(testDb)
                .statement("SELECT * FROM DUCK WHERE ID=" + id)
                .validate("COLOR",color)
                .validate("HEIGHT",height)
                .validate("MATERIAL",material)
                .validate("SOUND",sound)
                .validate("WINGS_STATE",wingsState));
    }


    // Additional methods for extracting id and parity

    @Step("Получение ID уточки")
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

    @Step("Проверка четности id")
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

    @Step("Проверка нечетности id")
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
