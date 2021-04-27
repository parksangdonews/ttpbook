package works.tripod.ttpbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import works.tripod.ttpbook.model.BookSearchInput;
import works.tripod.ttpbook.model.BookSearchOutput;
import works.tripod.ttpbook.service.BookApiService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/api")
public class book {

    @Autowired
    BookApiService bookApi;

    @PostMapping("/bookApi")
    public @ResponseBody ResponseEntity<BookSearchOutput> bookApi(BookSearchInput bookSearchInput) throws ExecutionException, InterruptedException, TimeoutException {
        log.debug(bookSearchInput.toString());

        // naver api
        CompletableFuture<BookSearchOutput> searchOutputCompletableFutureNaver = CompletableFuture.supplyAsync(() -> bookApi.callApi(bookSearchInput));

        // aladin api
        // TODO:
        ///CompletableFuture<BookSearchOutput> searchOutputCompletableFutureAladin = CompletableFuture.supplyAsync(() -> bookApi.callApi(bookSearchInput));

        // receive
        CompletableFuture<Object> rst = CompletableFuture
                .anyOf(searchOutputCompletableFutureNaver);

        BookSearchOutput result = (BookSearchOutput) rst.get(2000L, TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


}
