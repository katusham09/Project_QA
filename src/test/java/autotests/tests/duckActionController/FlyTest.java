package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class FlyTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка летает с существующим id и активными крыльями")
    @CitrusTest
    public void flyWithActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \"I am flying\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и со связанными крыльями")
    @CitrusTest
    public void flyWithFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        getDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \"I can not fly\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что уточка не летает с существующим id и крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyWithUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "UNDEFINED");
        getDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \"Wings are not detected\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }
}
