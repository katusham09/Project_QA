package autotests;

import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;

public class EndpointConfig {

    @Bean("DuckService")
    public HttpClient DuckService() {
        return new HttpClientBuilder()
                .requestUrl("http://localhost:2222")
                .build();
    }
}
