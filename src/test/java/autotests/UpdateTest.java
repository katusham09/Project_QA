package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class UpdateTest extends DuckTestBase {
    @Test(description = "Проверка того, что у уточки изменился цвет и высота")
    @CitrusTest
    public void updateDuckColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        updateDuck(runner, "${duckId}", "black", 0.1,"rubber", "quack", "ACTIVE");
        validateResponse(runner, "{\n"+ "  \"message\": \"Duck with id = ${duckId} is updated\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что у уточки изменился цвет и звук")
    @CitrusTest
    public void updateDuckColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        updateDuck(runner, "${duckId}", "black", 0.03,"rubber", "meow", "ACTIVE");
        validateResponse(runner, "{\n"+ "  \"message\": \"Duck with id = ${duckId} is updated\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    public void updateDuck(TestCaseRunner runner, String id, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .put("/api/duck/update")
                        .message()
                        .body("{\n" +
                                "\"id\": \"" + id + "\",\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }
}
