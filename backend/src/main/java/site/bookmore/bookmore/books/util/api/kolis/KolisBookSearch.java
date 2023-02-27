package site.bookmore.bookmore.books.util.api.kolis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.AbstractBookSearch;
import site.bookmore.bookmore.books.util.api.BookSearchApiParams;
import site.bookmore.bookmore.books.util.api.BookSearchResponse;
import site.bookmore.bookmore.books.util.api.kolis.dto.KolisSearchResponse;

import java.net.URI;
import java.util.Map;

@Slf4j
public class KolisBookSearch extends AbstractBookSearch {
    private final String token;

    public KolisBookSearch(String baseUrl, Map<String, String> headers, String endpoint, String token) {
        super(baseUrl, headers, endpoint);
        this.token = token;
    }

    @Override
    public Mono<Book> searchByISBN(BookSearchApiParams searchApiParams) {
        StopWatch stopWatch = new StopWatch();
        return super.searchByISBN(searchApiParams)
                .doOnSubscribe(subscription -> {
                    log.info("국립 중앙 도서관 도서 상세조회");
                    stopWatch.start();
                })
                .doOnSuccess((book) -> {
                    stopWatch.stop();
                    log.info("국립 중앙 도서관 도서 상세조회 완료 [{}ms]", stopWatch.getTotalTimeMillis());
                });
    }

    @Override
    protected Class<? extends BookSearchResponse> responseClass() {
        return KolisSearchResponse.class;
    }

    @Override
    protected URI uriBuild(UriBuilder uriBuilder, BookSearchApiParams bookSearchApiParams) {
        uriBuilder.queryParam("cert_key", token)
                .queryParam("result_style", "json");
        return super.uriBuild(uriBuilder, bookSearchApiParams);
    }
}
