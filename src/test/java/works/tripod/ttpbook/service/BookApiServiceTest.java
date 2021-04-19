package works.tripod.ttpbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookApiServiceTest {

    @Autowired
    private BookApiService bookApiService;

    @Test
    void callApi() {

        bookApiService.callApi("도메인 주도 설계");


    }
}
