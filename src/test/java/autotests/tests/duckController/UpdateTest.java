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
@Feature("Эндпоинт /api/duck/update")
public class UpdateTest extends DuckActionsClient {
    @Test(description = "Проверка того, что у уточки изменился цвет и высота")
    @CitrusTest
    public void updateDuckColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        updateDuck(runner, "${duckId}", "black", "0.1","rubber", "quack", "ACTIVE");
        validateDuckInDatabase(runner, "${duckId}", "black", "0.1","rubber", "quack", "ACTIVE");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка того, что у уточки изменился цвет и звук")
    @CitrusTest
    public void updateDuckColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        updateDuck(runner, "${duckId}", "black", "0.03","rubber", "meow", "ACTIVE");
        validateDuckInDatabase(runner, "${duckId}", "black", "0.03","rubber", "meow", "ACTIVE");
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }
}
