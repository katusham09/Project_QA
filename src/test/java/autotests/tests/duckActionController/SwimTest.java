package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
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
@Feature("Эндпоинт /api/duck/action/swim")
public class SwimTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка поплыла с существующем id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        duckSwim(runner, "${duckId}");
        Message message = new Message().message("I'm swimming");
        validateResponse(runner, message);
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка того, что с несуществующим id появляется ошибка")
    @CitrusTest
    public void swimWithNonExistentDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String nonExistentId = "1000001";
        duckSwim(runner, nonExistentId);
        validateResponse(runner, HttpStatus.NOT_FOUND, "{\n" + "  \"message\": \"" + "Duck not found" + "\"\n" + "}");
        runner.$(doFinally().actions(deleteDuckBD(runner, "1000001")));
    }
}