package site.bookmore.bookmore.books.util.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.bookmore.bookmore.books.entity.Book;

import java.util.List;

public interface BookSearchResponse {
    default Page<Book> toPage() {
        return new PageImpl<>(getAll(), getSize() > 0 ? getPageable() : Pageable.unpaged(), getTotal());
    }

    Pageable getPageable();

    Integer getSize();

    Long getTotal();

    Book getFirst();

    List<Book> getAll();
}
