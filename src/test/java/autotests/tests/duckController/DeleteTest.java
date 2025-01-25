package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DeleteTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка удалилась")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        deleteDuck(runner, "${duckId}");
        validateResponse(runner);
    }
}
