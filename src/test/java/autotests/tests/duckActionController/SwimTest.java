package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class SwimTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка поплыла с существующем id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        getDuckId(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner, "{\n"+ "  \"message\": \"I'm swimming\"\n"+ "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того, что с несуществующим id появляется ошибка")
    @CitrusTest
    public void swimWithNonExistentDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String nonExistentId = "99999";
        duckSwim(runner, nonExistentId);
        validateErrorResponse(runner, HttpStatus.NOT_FOUND, "Duck not found");
    }
}