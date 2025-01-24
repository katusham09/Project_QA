package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class UpdateTest extends DuckActionsClient {
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
}
