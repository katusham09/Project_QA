package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class PropertiesTest extends DuckActionsClient {
    @Test(description = "Проверка получения характеристик уточки с четным id и material = wood")
    @CitrusTest
    public void checkDuckWithEvenIdr(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
        getDuckId(runner);
        oddParityCheck(runner, "${duckId}", "wood");
        propertiesDuck(runner, "${duckId}");
        validateResponse(runner, "{\n" +
                "\"color\": \"yellow\",\n" +
                "\"height\": 0.03,\n" +
                "\"material\": \"wood\",\n" +
                "\"sound\": \"quack\",\n" +
                "\"wingsState\": \"ACTIVE\"\n" +
                "}");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка получения характеристик уточки с нечетным id и material = rubber")
    @CitrusTest
    public void checkDuckWithOddIdr(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        parityCheck(runner, "${duckId}", "rubber");
        propertiesDuck(runner, "${duckId}");
        validateResponse(runner, "{\n" +
                "\"color\": \"yellow\",\n" +
                "\"height\": 0.03,\n" +
                "\"material\": \"rubber\",\n" +
                "\"sound\": \"quack\",\n" +
                "\"wingsState\": \"ACTIVE\"\n" +
                "}");
        deleteDuck(runner, "${duckId}");
    }
}
