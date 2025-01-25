package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Sound;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class QuackTest extends DuckActionsClient {
    @Test(description = "Проверка кряканья уточки с чётным id")
    @CitrusTest
    public void quackWithEvenId (@Optional @CitrusResource TestCaseRunner runner){
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        oddParityCheck(runner, "${duckId}", "rubber");
        quack(runner, "${duckId}", "3", "2");
        Sound sound = new Sound().sound("quack-quack, quack-quack, quack-quack");
        validateResponse(runner, sound);
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка кряканья уточки с нечётным id")
    @CitrusTest
    public void quackWithOddId (@Optional @CitrusResource TestCaseRunner runner){
        Duck duck = new Duck().color("yellow").height(0.03).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getDuckId(runner);
        parityCheck(runner, "${duckId}", "rubber");
        quack(runner, "${duckId}", "3", "2");Sound sound = new Sound().sound("quack-quack, quack-quack, quack-quack");
        validateResponse(runner, sound);
        deleteDuck(runner, "${duckId}");
    }
}
