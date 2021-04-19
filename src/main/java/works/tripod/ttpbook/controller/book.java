package works.tripod.ttpbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import works.tripod.ttpbook.model.BookSearchInput;
import works.tripod.ttpbook.model.BookSearchOutput;
import works.tripod.ttpbook.service.BookApiService;

@Slf4j
@RestController
@RequestMapping("/api")
public class book {

    @Autowired
    BookApiService bookApi;

    @PostMapping("/bookApi")
    public @ResponseBody ResponseEntity<BookSearchOutput> bookApi(BookSearchInput bookSearchInput) {
        log.debug(bookSearchInput.toString());
        BookSearchOutput bookSearchOutput = bookApi.callApi(bookSearchInput);
        return new ResponseEntity<>(bookSearchOutput, HttpStatus.OK);
    }


}
