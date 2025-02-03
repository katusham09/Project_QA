package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class CreateTest extends DuckActionsClient {
    Duck duck1 = new Duck()
            .id("@ignore@")
            .color("yellow")
            .height(0.05)
            .material("rubber")
            .sound("quack")
            .wingsState(WingState.ACTIVE);
    Duck duck2 = new Duck()
            .id("@ignore@")
            .color("green")
            .height(1.0)
            .material("plastic")
            .sound("meow")
            .wingsState(WingState.FIXED);
    Duck duck3 = new Duck()
            .id("@ignore@")
            .color("blue")
            .height(5.05)
            .material("wood")
            .sound("quack")
            .wingsState(WingState.UNDEFINED);
    Duck duck4 = new Duck()
            .id("@ignore@")
            .color("black")
            .height(2.03)
            .material("glass")
            .sound("quack")
            .wingsState(WingState.ACTIVE);
    Duck duck5 = new Duck()
            .id("@ignore@")
            .color("pink")
            .height(0.35)
            .material("metal")
            .sound("quack")
            .wingsState(WingState.FIXED);

    @Test(dataProvider = "duckList")
    @CitrusTest
    @CitrusParameters({"payload", "response", "runner"})
    public void successfulDuckCreate(Object payload, Object expectedPayload, @Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, payload);
        validateResponse(runner, expectedPayload);
    }

    @DataProvider(name = "duckList")
    public Object[][] DuckProvider() {
        return new Object[][]{
                {duck1, duck1, null},
                {duck2, duck2, null},
                {duck3, duck3, null},
                {duck4, duck4, null},
                {duck5, duck5, null}
        };
    }

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
