package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class PropertiesTest extends DuckActionsClient {
    @Test(description = "Проверка получения характеристик уточки с четным id и material = wood")
    @CitrusTest
    public void checkDuckWithEvenIdr(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
        getDuckId(runner);
        runner.$(action -> {
            String duckId = action.getVariable("duckId");
            if (Integer.parseInt(duckId) % 2 != 0) {
                deleteDuck(runner, duckId);
                createDuck(runner, "yellow", 0.03, "wood", "quack", "ACTIVE");
                getDuckId(runner);
            }});
        propertiesDuck(runner, "${duckId}");
        validateResponseJsonPath(runner, jsonPath().expression("$.material", "wood"));
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка получения характеристик уточки с нечетным id и material = rubber")
    @CitrusTest
    public void checkDuckWithOddIdr(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        runner.$(action -> {
            String duckId = action.getVariable("duckId");
            if (Integer.parseInt(duckId) % 2 != 0) {
                deleteDuck(runner, duckId);
                createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
                getDuckId(runner);
            }});
        propertiesDuck(runner, "${duckId}");
        validateResponseJsonPath(runner, jsonPath().expression("$.material", "rubber"));
        deleteDuck(runner, "${duckId}");
    }
}
