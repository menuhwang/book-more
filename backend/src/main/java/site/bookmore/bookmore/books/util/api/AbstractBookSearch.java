package site.bookmore.bookmore.books.util.api;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

public abstract class AbstractBookSearch implements BookSearch {
    private final WebClient webClient;
    private final Map<String, String> headers;
    private final String endpoint;

    public AbstractBookSearch(String baseUrl, Map<String, String> headers, String endpoint) {
        this.webClient = WebClient.create(baseUrl);
        this.headers = headers;
        this.endpoint = endpoint;
    }

    @Override
    public Mono<Page<Book>> search(BookSearchApiParams searchApiParams) {
        return webClient.get()
                .uri(uriBuilder -> uriBuild(uriBuilder, searchApiParams))
                .headers(this::setHeaders)
                .retrieve()
                .bodyToMono(responseClass())
                .map(BookSearchResponse::toPage);
    }

    @Override
    public Mono<Book> searchByISBN(BookSearchApiParams searchApiParams) {
        return webClient.get()
                .uri(uriBuilder -> uriBuild(uriBuilder, searchApiParams))
                .headers(this::setHeaders)
                .retrieve()
                .bodyToMono(responseClass())
                .map(this::mapToBook);
    }

    private void setHeaders(HttpHeaders httpHeaders) {
        if (headers == null) return;
        for (String key : headers.keySet()) {
            httpHeaders.add(key, headers.get(key));
        }
    }

    protected URI uriBuild(UriBuilder uriBuilder, BookSearchApiParams bookSearchApiParams) {
        uriBuilder.path(endpoint);
        Class<?> clazz = bookSearchApiParams.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        try {
            for (Method method : methods) {
                String name = method.getName();
                if (!name.startsWith("get")) continue;
                String fieldName = name.substring(3).toLowerCase();
                Object value = method.invoke(bookSearchApiParams);
                if (value != null) uriBuilder.queryParam(fieldName, value);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            // InvocationTargetException : KakaoSearchParams 필드가 null인경우 api 호출시 제외됨
        }
        return uriBuilder.build();
    }

    protected Book mapToBook(BookSearchResponse bookSearchResponse) {
        return bookSearchResponse.getFirst();
    }

    protected abstract Class<? extends BookSearchResponse> responseClass();
}
