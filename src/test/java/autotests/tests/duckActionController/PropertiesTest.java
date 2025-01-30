package autotests.tests.duckActionController;

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

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/properties")
public class PropertiesTest extends DuckActionsClient {
    @Test(description = "Проверка получения характеристик уточки с четным id и material = wood")
    @CitrusTest
    public void checkDuckWithEvenIdr(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "wood", "quack", "ACTIVE");
        oddParityCheck(runner, "${duckId}", "wood");
        propertiesDuck(runner, "${duckId}");
        Duck duck = new Duck().id("@ignore@").color("yellow").height(0.03).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        validateResponse(runner, duck);
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка получения характеристик уточки с нечетным id и material = rubber")
    @CitrusTest
    public void checkDuckWithOddIdr(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        parityCheck(runner, "${duckId}", "rubber");
        propertiesDuck(runner, "${duckId}");
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        validateResponse(runner, duck);
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }
}
