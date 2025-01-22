package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class UpdateTest extends TestNGCitrusSpringSupport {
    @Test(description = "Проверка того, что у уточки изменился цвет и высота")
    @CitrusTest
    public void updateDuckColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        updateDuck(runner, "${duckId}", "black", 0.1,"rubber", "quack", "ACTIVE");
        validateResponse(runner, "{\n\"message\": \"Duck with id = ${duckId} is updated\"\n}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что у уточки изменился цвет и звук")
    @CitrusTest
    public void updateDuckColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
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
                        .queryParam("id", id)
                        .queryParam("color", color)
                        .queryParam("height", String.valueOf(height))
                        .queryParam("material", material)
                        .queryParam("sound", sound)
                        .queryParam("wingsState", wingsState)
        );
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

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage));
    }
}
