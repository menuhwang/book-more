package site.bookmore.bookmore.books.util.api;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;

public interface BookSearch {
    Mono<Page<Book>> search(BookSearchApiParams searchParams);

    Mono<Book> searchByISBN(BookSearchApiParams searchParams);
}
