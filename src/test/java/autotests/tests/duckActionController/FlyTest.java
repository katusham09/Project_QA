package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class FlyTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка летает с существующим id и активными крыльями")
    @CitrusTest
    public void flyWithActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "duckActionController/flyWithActiveWings.json");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и со связанными крыльями")
    @CitrusTest
    public void flyWithFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "duckActionController/flyWithFixedWings.json");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyWithUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.UNDEFINED);
        createDuck(runner, duck);
        getDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "duckActionController/flyWithUndefinedWings.json");
        deleteDuck(runner, "${duckId}");
    }
}
