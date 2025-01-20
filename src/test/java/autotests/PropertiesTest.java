package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class PropertiesTest extends DuckTestBase {
    @Test(description = "Проверка получения характеристик уточки с четным id и material = wood")
    @CitrusTest
    public void checkDuckWithEvenIdr(@Optional @CitrusResource TestCaseRunner runner) {
        String duckId = createDuckAndGetId(runner, "wood");
        while (Integer.parseInt(duckId) % 2 != 0) {
            String duckIdTmp = duckId;
            duckId = createDuckAndGetId(runner, "wood");
            deleteDuck(runner, duckIdTmp);
        }
        propertiesDuck(runner, duckId);
        validateResponse(runner, "{\n"+ "  \"material\": \"wood\"\n"+ "}");
        deleteDuck(runner, duckId);
    }

    @Test(description = "Проверка получения характеристик уточки с нечетным id и material = rubber")
    @CitrusTest
    public void checkDuckWithOddIdr(@Optional @CitrusResource TestCaseRunner runner) {
        String duckId = createDuckAndGetId(runner, "wood");
        while (Integer.parseInt(duckId) % 2 == 0) {
            String duckIdTmp = duckId;
            duckId = createDuckAndGetId(runner, "rubber");
            deleteDuck(runner, duckIdTmp);
        }
        propertiesDuck(runner, duckId);
        validateResponse(runner, "{\n"+ "  \"material\": \"rubber\"\n"+ "}");
        deleteDuck(runner, duckId);
    }

    public void propertiesDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id));
    }

    public String createDuckAndGetId(TestCaseRunner runner, String material) {
        createDuck(runner, "yellow", 0.03, material, "quack", "ACTIVE");
        return getDuckId(runner);
    }
}
