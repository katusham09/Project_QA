package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public abstract class DuckTestBase extends TestNGCitrusSpringSupport {

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }

    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id));
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(responseMessage));
    }

    public String getDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        return "${duckId}";
    }
}
