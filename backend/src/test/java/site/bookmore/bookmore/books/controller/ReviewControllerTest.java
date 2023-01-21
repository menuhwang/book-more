package site.bookmore.bookmore.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.entity.Chart;
import site.bookmore.bookmore.books.service.ReviewService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @Autowired
    ObjectMapper objectMapper;

    /* ========== 도서 리뷰 등록 ========== */
    @Test
    @DisplayName("도서 리뷰 등록 성공")
    @WithMockUser
    void create_success() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("body", false, Chart.builder().build());

        // when
        when(reviewService.create(any(ReviewRequest.class), eq("9791158393083"), anyString()))
                .thenReturn(1L);

        // then
        mockMvc.perform(post("/api/v1/books/9791158393083/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.message").value("리뷰 등록 완료"));

        verify(reviewService).create(any(ReviewRequest.class), eq("9791158393083"), anyString());
    }

    /* ========== 도서 리뷰 조회 ========== */
    @Test
    @DisplayName("도서 리뷰 조회 성공")
    @WithMockUser
    void read_success() throws Exception {
        // when
        when(reviewService.read(any(), eq("9791158393083")))
                .thenReturn(Page.empty());

        // then
        mockMvc.perform(get("/api/v1/books/9791158393083/reviews")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").exists());

        verify(reviewService).read(any(), eq("9791158393083"));
    }

    /* ========== 도서 리뷰 좋아요 | 취소 ========== */
    @Test
    @DisplayName("도서 리뷰 좋아요 성공")
    @WithMockUser
    void doLikes_success() throws Exception {
        // when
        when(reviewService.doLikes(anyString(), eq(1L)))
                .thenReturn(true);

        // then
        mockMvc.perform(post("/api/v1/books/reviews/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다."));

        verify(reviewService).doLikes(anyString(), eq(1L));
    }

    @Test
    @DisplayName("도서 리뷰 좋아요 취소 성공")
    @WithMockUser
    void doLikes_cancel_success() throws Exception {
        // when
        when(reviewService.doLikes(anyString(), eq(1L)))
                .thenReturn(false);

        // then
        mockMvc.perform(post("/api/v1/books/reviews/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value("좋아요가 취소되었습니다."));

        verify(reviewService).doLikes(anyString(), eq(1L));
    }
}