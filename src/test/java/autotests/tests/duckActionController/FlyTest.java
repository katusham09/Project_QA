package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/fly")
public class FlyTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка летает с существующим id и активными крыльями")
    @CitrusTest
    public void flyWithActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        duckFly(runner, "${duckId}");
        validateResponse(runner, "duckActionController/flyWithActiveWings.json");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и со связанными крыльями")
    @CitrusTest
    public void flyWithFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
        duckFly(runner, "${duckId}");
        validateResponse(runner, "duckActionController/flyWithFixedWings.json");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyWithUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "UNDEFINED");
        duckFly(runner, "${duckId}");
        validateResponse(runner, "duckActionController/flyWithUndefinedWings.json");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }
}
