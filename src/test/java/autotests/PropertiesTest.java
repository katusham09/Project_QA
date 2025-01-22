package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class PropertiesTest extends TestNGCitrusSpringSupport {
    @Test(description = "Проверка получения характеристик уточки с четным id и material = wood")
    @CitrusTest
    public void checkDuckWithEvenIdr(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
        getDuckId(runner);
        runner.$(action -> {
            String duckId = action.getVariable("duckId");
            if (Integer.parseInt(duckId) % 2 != 0) {
                deleteDuck(runner, duckId);
                createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
                getDuckId(runner);
            }});
        propertiesDuck(runner, "${duckId}");
        validateResponseJsonPath(runner, jsonPath().expression("$.material", "wood"));
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка получения характеристик уточки с нечетным id и material = rubber")
    @CitrusTest
    public void checkDuckWithOddIdr(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        runner.$(action -> {
            String duckId = action.getVariable("duckId");
            if (Integer.parseInt(duckId) % 2 != 0) {
                deleteDuck(runner, duckId);
                createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
                getDuckId(runner);
            }});
        propertiesDuck(runner, "${duckId}");
        validateResponseJsonPath(runner, jsonPath().expression("$.material", "rubber"));
        deleteDuck(runner, "${duckId}");
    }

    public void propertiesDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id));
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }

    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id));
    }

    public void getDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    public void validateResponseJsonPath(TestCaseRunner runner, JsonPathMessageValidationContext.Builder body) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(body)
        );
    }
}
