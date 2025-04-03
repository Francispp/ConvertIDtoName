package org.example.convertidtoname;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeEnrichmentControllerTest {

    @LocalServerPort
    private int port=8080;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        HttpClient httpClient = HttpClient.create(ConnectionProvider.newConnection())
                .responseTimeout(Duration.ofSeconds(10));

        webTestClient = WebTestClient.bindToServer(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:" + port)
                .codecs(config -> config.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 设置 10MB
                .build();
    }

    @Test
    public void testEnrichTrades() {
        ClassPathResource csvFile = new ClassPathResource("/static/trade_error.csv");

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", csvFile);

        webTestClient.post()
                .uri("/TradeEnrichmentController/enrich")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(parts)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/csv")
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    System.out.println("响应内容：\n" + body);
                });

    }


}
