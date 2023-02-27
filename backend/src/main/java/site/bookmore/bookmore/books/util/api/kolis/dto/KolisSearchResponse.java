package site.bookmore.bookmore.books.util.api.kolis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.BookSearchResponse;
import site.bookmore.bookmore.books.util.mapper.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class KolisSearchResponse implements BookSearchResponse {
    @JsonProperty(value = "PAGE_NO")
    private String page;
    @JsonProperty(value = "TOTAL_COUNT")
    private String totalCount;
    private List<Doc> docs;

    private long getTotalCount() {
        return Long.parseLong(totalCount);
    }

    @Override
    public Pageable getPageable() {
        return PageRequest.of(Integer.parseInt(page), docs.size());
    }

    @Override
    public Integer getSize() {
        return docs.size();
    }

    @Override
    public Long getTotal() {
        return getTotalCount();
    }

    @Override
    public Book getFirst() {
        if (docs.size() == 0) return null;
        return BookMapper.of(docs.get(0));
    }

    @Override
    public List<Book> getAll() {
        return docs.stream().map(BookMapper::of).collect(Collectors.toList());
    }
}
