package site.bookmore.bookmore.books.util.api.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.BookSearchResponse;
import site.bookmore.bookmore.books.util.mapper.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NaverSearchResponse implements BookSearchResponse {
    private Long total;
    private Integer start;
    private Integer display;
    private List<Item> items;

    public Pageable getPageable() {
        return PageRequest.of(start, display);
    }

    @Override
    public Integer getSize() {
        return display;
    }

    public Long getTotal() {
        return total;
    }

    @Override
    public Book getFirst() {
        if (items.size() == 0) return null;
        return BookMapper.of(items.get(0));
    }

    @Override
    public List<Book> getAll() {
        return items.stream().map(BookMapper::of).collect(Collectors.toList());
    }
}
