package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class CreateTest extends DuckTestBase {
    @Test(description = "Проверка того, что уточка создалась с материалом rubber")
    @CitrusTest
    public void createRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/get")
                        .queryParam("id", "${duckId}"));
        validateResponse(runner, "\"material\": \"rubber\"");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка создалась с материалом wood")
    @CitrusTest
    public void createWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/get")
                        .queryParam("id", "${duckId}"));
        validateResponse(runner, "{\n"+ "  \"material\": \"wood\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }
}
