package works.tripod.ttpbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import works.tripod.ttpbook.model.BookSearchInput;
import works.tripod.ttpbook.model.BookSearchOutput;
import works.tripod.ttpbook.service.BookApiService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private final BookApiService bookApi;

    @PostMapping("/bookApi")
    public @ResponseBody ResponseEntity<BookSearchOutput> bookApi(BookSearchInput bookSearchInput) throws ExecutionException, InterruptedException, TimeoutException {
        log.debug(bookSearchInput.toString());

        long current = System.currentTimeMillis();

        // naver api
        CompletableFuture<BookSearchOutput> searchOutputCompletableFutureNaver = CompletableFuture.supplyAsync(() -> bookApi.callApi(bookSearchInput));

        // aladin api
        ///CompletableFuture<BookSearchOutput> searchOutputCompletableFutureAladin = CompletableFuture.supplyAsync(() -> bookApi.callApi(bookSearchInput));

        // receive
        CompletableFuture<Object> rst = CompletableFuture
                .anyOf(searchOutputCompletableFutureNaver);

        //BookSearchOutput result = (BookSearchOutput) rst.get(2000L, TimeUnit.MILLISECONDS);
        BookSearchOutput result = (BookSearchOutput) rst.get();

        long resultMilliseconds = System.currentTimeMillis() - current;
        log.debug("api call time :: ");
        log.debug(Long.toString(resultMilliseconds));

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


}
