package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class QuackTest extends DuckActionsClient {
    @Test(description = "Проверка кряканья уточки с чётным id")
    @CitrusTest
    public void quackWithEvenId (@Optional @CitrusResource TestCaseRunner runner){
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        oddParityCheck(runner, "${duckId}", "rubber");
        quack(runner, "${duckId}", "3", "2");
        validateResponse(runner, "{ \"sound\": \"quack-quack, quack-quack, quack-quack\" }");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка кряканья уточки с нечётным id")
    @CitrusTest
    public void quackWithOddId (@Optional @CitrusResource TestCaseRunner runner){
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        parityCheck(runner, "${duckId}", "rubber");
        quack(runner, "${duckId}", "3", "2");
        validateResponse(runner, "{ \"sound\": \"quack-quack, quack-quack, quack-quack\" }");
        deleteDuck(runner, "${duckId}");
    }
}
