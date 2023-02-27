package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.Builder;
import lombok.Getter;
import site.bookmore.bookmore.books.util.api.BookSearchApiParams;

@Getter
public class KakaoSearchParams implements BookSearchApiParams {
    private final String query;
    private final String sort;
    private final Integer page;
    private final Integer size;
    private final String target;

    @Builder
    public KakaoSearchParams(String query, String sort, Integer page, Integer size, String target) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
        this.target = target;
    }

    public static KakaoSearchParams of(String isbn) {
        return KakaoSearchParams.builder()
                .query(isbn)
                .target("isbn")
                .build();
    }
}
