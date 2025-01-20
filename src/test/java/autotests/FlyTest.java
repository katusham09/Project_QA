package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class FlyTest extends DuckTestBase {
    @Test(description = "Проверка того, что уточка летает с существующим id и активными крыльями")
    @CitrusTest
    public void flyWithActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        runner.variable("messageText", "I am flying");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \".*${messageText}.*\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и со связанными крыльями")
    @CitrusTest
    public void flyWithFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        runner.variable("messageText", "I can not fly");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \".*${messageText}.*\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyWithUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "UNDEFINED");
        runner.variable("messageText", "Wings are not detected");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \".*${messageText}.*\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));
    }
}
