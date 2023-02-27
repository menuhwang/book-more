package site.bookmore.bookmore.books.util.api.naver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.AbstractBookSearch;
import site.bookmore.bookmore.books.util.api.BookSearchApiParams;
import site.bookmore.bookmore.books.util.api.BookSearchResponse;
import site.bookmore.bookmore.books.util.api.naver.dto.NaverSearchResponse;

import java.util.Map;

@Slf4j
public class NaverBooksearch extends AbstractBookSearch {
    public NaverBooksearch(String baseUrl, Map<String, String> headers, String endpoint) {
        super(baseUrl, headers, endpoint);
    }

    @Override
    public Mono<Book> searchByISBN(BookSearchApiParams searchApiParams) {
        StopWatch stopWatch = new StopWatch();
        return super.searchByISBN(searchApiParams)
                .doOnSubscribe(subscription -> {
                    log.info("네이버 도서 상세조회");
                    stopWatch.start();
                })
                .doOnSuccess((book) -> {
                    stopWatch.stop();
                    log.info("네이버 도서 상세조회 완료 [{}ms]", stopWatch.getTotalTimeMillis());
                });
    }

    @Override
    protected Class<? extends BookSearchResponse> responseClass() {
        return NaverSearchResponse.class;
    }
}
