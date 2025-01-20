package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class SwimTest extends DuckTestBase {
    @Test(description = "Проверка того, что уточка поплыла с существующем id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        runner.variable("messageText", "I'm swimming");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        duckSwim(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \"${messageText}\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что с несуществующим id появляется ошибка")
    @CitrusTest
    public void swimWithNonExistentDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String nonExistentId = "99999";
        duckSwim(runner, nonExistentId);
        validateErrorResponse(runner, HttpStatus.NOT_FOUND, "Duck not found");
    }

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    public void validateErrorResponse(TestCaseRunner runner, HttpStatus expectedStatus, String expectedMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(expectedStatus)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" + "  \"message\": \"" + expectedMessage + "\"\n" + "}"));
    }
}