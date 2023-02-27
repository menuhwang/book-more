package site.bookmore.bookmore.books.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import site.bookmore.bookmore.books.util.api.kakao.KakaoBookSearch;
import site.bookmore.bookmore.books.util.api.kolis.KolisBookSearch;
import site.bookmore.bookmore.books.util.api.naver.NaverBooksearch;
import site.bookmore.bookmore.books.util.crawler.BookCrawler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BookSearchApiConfig {
    @Bean
    public NaverBooksearch naverBooksearch(@Value("${api.token.naver.client.id}") String clientId, @Value("${api.token.naver.client.secret}") String clientSecret) {
        final String BASE_URL = "https://openapi.naver.com";
        final String SEARCH_ENDPOINT = "/v1/search/book_adv.json";
        final String HEADER_CLIENT_ID = "X-Naver-Client-Id";
        final String HEADER_CLIENT_SECRET = "X-Naver-Client-Secret";

        final Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_CLIENT_ID, clientId);
        headers.put(HEADER_CLIENT_SECRET, clientSecret);

        return new NaverBooksearch(BASE_URL, headers, SEARCH_ENDPOINT);
    }

    @Bean
    public KakaoBookSearch kakaoBookSearch(@Value("${api.token.kakao}") String token, BookCrawler bookCrawler) {
        final String BASE_URL = "http://dapi.kakao.com";
        final String SEARCH_ENDPOINT = "/v3/search/book";

        final Map<String, String> headers = new HashMap<>();

        headers.put(HttpHeaders.AUTHORIZATION, token);

        return new KakaoBookSearch(BASE_URL, headers, SEARCH_ENDPOINT, bookCrawler);
    }

    @Bean
    public KolisBookSearch kolisBookSearch(@Value("${api.token.kolis}") String token) {
        final String BASE_URL = "https://www.nl.go.kr";
        final String SEARCH_ENDPOINT = "/seoji/SearchApi.do";

        return new KolisBookSearch(BASE_URL, null, SEARCH_ENDPOINT, token);
    }
}
