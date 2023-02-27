package site.bookmore.bookmore.books.util.api.kolis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.util.api.BookSearchApiParams;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KolisSearchParams implements BookSearchApiParams {
    @Builder.Default
    private int page_no = 1;
    @Builder.Default
    private int page_size = 20;
    private String isbn;
    private String title;
    private String author;

    public static KolisSearchParams of(String isbn) {
        return KolisSearchParams.builder()
                .isbn(isbn)
                .build();
    }
}
