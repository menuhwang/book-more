package site.bookmore.bookmore.books.util.api.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.AbstractBookSearch;
import site.bookmore.bookmore.books.util.api.BookSearchApiParams;
import site.bookmore.bookmore.books.util.api.BookSearchResponse;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchResponse;
import site.bookmore.bookmore.books.util.crawler.BookCrawler;

import java.util.Map;

@Slf4j
public class KakaoBookSearch extends AbstractBookSearch {
    private final BookCrawler bookCrawler;

    public KakaoBookSearch(String baseUrl, Map<String, String> headers, String endpoint, BookCrawler bookCrawler) {
        super(baseUrl, headers, endpoint);
        this.bookCrawler = bookCrawler;
    }

    @Override
    public Mono<Book> searchByISBN(BookSearchApiParams searchApiParams) {
        StopWatch stopWatch = new StopWatch();
        return super.searchByISBN(searchApiParams)
                .doOnSubscribe(subscription -> {
                    log.info("카카오 도서 상세조회");
                    stopWatch.start();
                })
                .doOnSuccess((book) -> {
                    stopWatch.stop();
                    log.info("카카오 도서 상세조회 완료 [{}ms]", stopWatch.getTotalTimeMillis());
                });
    }

    @Override
    protected Class<? extends BookSearchResponse> responseClass() {
        return KakaoSearchResponse.class;
    }

    @Override
    protected Book mapToBook(BookSearchResponse bookSearchResponse) {
        Book book = super.mapToBook(bookSearchResponse);
        Book crawl = bookCrawler.execute(((KakaoSearchResponse) bookSearchResponse).getDocuments().get(0).getUrl());
        return book.merge(crawl);
    }
}
