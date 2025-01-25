package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class PropertiesTest extends DuckActionsClient {
    @Test(description = "Проверка получения характеристик уточки с четным id и material = wood")
    @CitrusTest
    public void checkDuckWithEvenIdr(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        oddParityCheck(runner, "${duckId}", "wood");
        propertiesDuck(runner, "${duckId}");
        validateResponse(runner, duck);
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка получения характеристик уточки с нечетным id и material = rubber")
    @CitrusTest
    public void checkDuckWithOddIdr(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        parityCheck(runner, "${duckId}", "rubber");
        propertiesDuck(runner, "${duckId}");
        validateResponse(runner, duck);
        deleteDuck(runner, "${duckId}");
    }
}
