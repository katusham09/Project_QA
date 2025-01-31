package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Sound;
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
@Feature("Эндпоинт /api/duck/action/quack")
public class QuackTest extends DuckActionsClient {
    @Test(description = "Проверка кряканья уточки с чётным id")
    @CitrusTest
    public void quackWithEvenId (@Optional @CitrusResource TestCaseRunner runner){
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        oddParityCheck(runner, "${duckId}", "rubber");
        quack(runner, "${duckId}", "3", "2");
        Sound sound = new Sound().sound("quack-quack, quack-quack, quack-quack");
        validateResponse(runner, sound);
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }

    @Test(description = "Проверка кряканья уточки с нечётным id")
    @CitrusTest
    public void quackWithOddId (@Optional @CitrusResource TestCaseRunner runner){
        int id = new Random().nextInt(1000000);
        runner.variable("duckId",id);
        createDuck(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");
        parityCheck(runner, "${duckId}", "rubber");
        quack(runner, "${duckId}", "3", "2");
        Sound sound = new Sound().sound("quack-quack, quack-quack, quack-quack");
        validateResponse(runner, sound);
        runner.$(doFinally().actions(deleteDuckBD(runner, "${duckId}")));
    }
}
