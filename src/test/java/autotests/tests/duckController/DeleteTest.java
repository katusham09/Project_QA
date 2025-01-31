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

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/delete")
public class DeleteTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка удалилась")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        deleteDuck(runner, "${duckId}");
        validateResponse(runner, "duckController/deleteDuck.json");
    }
}
