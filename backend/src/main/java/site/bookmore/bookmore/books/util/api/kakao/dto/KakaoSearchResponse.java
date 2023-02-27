package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.BookSearchResponse;
import site.bookmore.bookmore.books.util.mapper.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSearchResponse implements BookSearchResponse {
    private Meta meta;
    private List<Document> documents;

    @Override
    public Pageable getPageable() {
        return PageRequest.of(meta.getPageable_count(), documents.size());
    }

    @Override
    public Integer getSize() {
        return documents.size();
    }

    @Override
    public Long getTotal() {
        return meta.getTotal_count();
    }

    @Override
    public Book getFirst() {
        if (documents.size() == 0) return null;
        return BookMapper.of(documents.get(0));
    }

    @Override
    public List<Book> getAll() {
        return documents.stream().map(BookMapper::of).collect(Collectors.toList());
    }
}
