package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class UpdateTest extends DuckActionsClient {
    @Test(description = "Проверка того, что у уточки изменился цвет и высота")
    @CitrusTest
    public void updateDuckColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        updateDuck(runner, "${duckId}", "black", 0.1,"rubber", "quack", "ACTIVE");
        validateResponse(runner, "duckController/updateDuck.json");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что у уточки изменился цвет и звук")
    @CitrusTest
    public void updateDuckColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        updateDuck(runner, "${duckId}", "black", 0.03,"rubber", "meow", "ACTIVE");
        validateResponse(runner, "duckController/updateDuck.json");
        deleteDuck(runner, "${duckId}");
    }
}
