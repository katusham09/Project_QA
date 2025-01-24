package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class CreateTest extends DuckActionsClient {
    @Test(description = "Проверка того, что уточка создалась с материалом rubber")
    @CitrusTest
    public void createRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        validateResponse(runner, "{\n" +
                "\"id\": \"@ignore@\",\n" +
                "\"color\": \"yellow\",\n" +
                "\"height\": 0.03,\n" +
                "\"material\": \"rubber\",\n" +
                "\"sound\": \"quack\",\n" +
                "\"wingsState\": \"ACTIVE\"\n" +
                "}");
    }

    @Test(description = "Проверка того, что уточка создалась с материалом wood")
    @CitrusTest
    public void createWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
        validateResponse(runner, "{\n" +
                "\"id\": \"@ignore@\",\n" +
                "\"color\": \"yellow\",\n" +
                "\"height\": 0.03,\n" +
                "\"material\": \"wood\",\n" +
                "\"sound\": \"quack\",\n" +
                "\"wingsState\": \"ACTIVE\"\n" +
                "}");
    }
}
