package works.tripod.ttpbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import works.tripod.ttpbook.model.BookSearchInput;

@SpringBootTest
class BookApiServiceTest {

    @Autowired
    private BookApiService bookApiService;

    @Test
    void callApi() {

        bookApiService.callApi(new BookSearchInput("도메인 주도 설계"));


    }
}
