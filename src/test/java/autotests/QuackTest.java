package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class QuackTest extends DuckTestBase {
    @Test(description = "Проверка кряканья уточки с чётным id")
    @CitrusTest
    public void quackWithEvenId (@Optional @CitrusResource TestCaseRunner runner){
        String duckId = createDuckAndGetId(runner);
        while (Integer.parseInt(duckId) % 2 != 0) {
            String duckIdTmp = duckId;
            duckId = createDuckAndGetId(runner);
            deleteDuck(runner, duckIdTmp);
        }
        quack(runner, duckId, "3", "2");
        validateResponse(runner, "{ \"sound\": \"quack-quack, quack-quack, quack-quack\" }");
        deleteDuck(runner, duckId);
    }

    @Test(description = "Проверка кряканья уточки с нечётным id")
    @CitrusTest
    public void quackWithOddId (@Optional @CitrusResource TestCaseRunner runner){
        String duckId = createDuckAndGetId(runner);
        while (Integer.parseInt(duckId) % 2 == 0) {
            String duckIdTmp = duckId;
            duckId = createDuckAndGetId(runner);
            deleteDuck(runner, duckIdTmp);
        }
        quack(runner, duckId, "3", "2");
        validateResponse(runner, "{ \"sound\": \"quack-quack, quack-quack, quack-quack\" }");
        deleteDuck(runner, duckId);
    }

    public String createDuckAndGetId(TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        return getDuckId(runner);
    }

    public void quack(TestCaseRunner runner, String id, String repetitions, String quacks) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .put("/api/duck/quack")
                        .message()
                        .body("{\n" +
                                "\"id\": \"" + id + "\",\n" +
                                "\"repetitions\": " + repetitions + ",\n" +
                                "\"quacks\": " + quacks + "\n" + "}"));
    }
}
