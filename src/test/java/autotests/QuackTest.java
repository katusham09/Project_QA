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

public class QuackTest extends TestNGCitrusSpringSupport {
    @Test(description = "Проверка кряканья уточки с чётным id")
    @CitrusTest
    public void quackWithEvenId (@Optional @CitrusResource TestCaseRunner runner){
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        runner.$(action -> {
            String duckId = action.getVariable("duckId");
            if (Integer.parseInt(duckId) % 2 != 0) {
                deleteDuck(runner, duckId);
                createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
                getDuckId(runner);
            }});
        quack(runner, "${duckId}", "3", "2");
        validateResponse(runner, "{ \"sound\": \"quack-quack, quack-quack, quack-quack\" }");
        deleteDuck(runner, "${duckId}");
    }


    @Test(description = "Проверка кряканья уточки с нечётным id")
    @CitrusTest
    public void quackWithOddId (@Optional @CitrusResource TestCaseRunner runner){
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        runner.$(action -> {
            String duckId = action.getVariable("duckId");
            if (Integer.parseInt(duckId) % 2 == 0) {
                deleteDuck(runner, "${duckId}");
                createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
                getDuckId(runner);
            }});
        quack(runner, "${duckId}", "3", "2");
        validateResponse(runner, "{ \"sound\": \"quack-quack, quack-quack, quack-quack\" }");
        deleteDuck(runner, "${duckId}");
    }

    public void quack(TestCaseRunner runner, String id, String repetitions, String quacks) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/quack")
                        .message()
                        .queryParam("id", id)
                        .queryParam("repetitionCount", repetitions)
                        .queryParam("soundCount", quacks));
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
