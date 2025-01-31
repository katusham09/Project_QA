package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class CreateTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка создалась с материалом rubber")
    @CitrusTest
    public void createRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().id("@ignore@").color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03","rubber", "quack", "ACTIVE");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка того, что уточка создалась с материалом wood")
    @CitrusTest
    public void createWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().id("@ignore@").color("yellow").height(0.03).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03","wood", "quack", "ACTIVE");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }
}