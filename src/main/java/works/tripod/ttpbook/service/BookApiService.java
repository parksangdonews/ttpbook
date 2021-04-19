package works.tripod.ttpbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import works.tripod.ttpbook.model.BookSearchOutput;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class BookApiService implements BookApi {

    public static final String API_URI = "https://openapi.naver.com/v1/search/book.json";

    @Value("${X-Naver-Client-Id}")
    private String API_ID;

    @Value("${X-Naver-Client-Secret}")
    private String API_KEY;


    @Override
    public void callApi(String input) {

        log.debug("input ::: ");
        log.debug(input);

        ObjectMapper mapper = new ObjectMapper();

        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()){

            // set serach key
            URI uri = new URI(API_URI);
            URIBuilder uriBuilder = new URIBuilder(uri, StandardCharsets.UTF_8).addParameter("query", input);
            HttpGet httpGet = new HttpGet(uriBuilder.toString());

            // set
            log.debug("API_SET ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            log.debug(API_URI);
            log.debug(API_ID);
            log.debug(API_KEY);
            log.debug("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

            // set header
            httpGet.setHeader("X-Naver-Client-Id", API_ID);
            httpGet.setHeader("X-Naver-Client-Secret", API_KEY);

            BookSearchOutput output = closeableHttpClient.execute(httpGet, httpResponse -> mapper.readValue(httpResponse.getEntity().getContent(), BookSearchOutput.class));

            log.info(output.toString());

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }


    }
}
