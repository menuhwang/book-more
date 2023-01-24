package site.bookmore.bookmore.alarms.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.alarms.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.service.AlarmService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
class AlarmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlarmService alarmService;

    AlarmResponse response = AlarmResponse.builder()
            .id(1L)
            .alarmType(AlarmType.NEW_FOLLOW_REVIEW)
            .targetUser(1L)
            .fromUser(2L)
            .source(3L)
            .build();


    @Test
    @DisplayName("팔로잉 알림 테스트")
    @WithMockUser(username = "user2")
    void alarm_following_list() throws Exception {
        Page<AlarmResponse> responsePage = new PageImpl<>(List.of(response));
        given(alarmService.findByFollowingReview(any(Pageable.class), eq("user2"))).willReturn(responsePage);

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists())
                .andExpect(jsonPath("$..['alarmType']").exists())
                .andExpect(jsonPath("$..['targetUser']").exists())
                .andExpect(jsonPath("$..['fromUser']").exists())
                .andExpect(jsonPath("$..['source']").exists());

        verify(alarmService).findByFollowingReview(any(Pageable.class), eq("user2"));
    }

    @Test
    @DisplayName("알람 조회 실패 - 권한 없음")
    @WithAnonymousUser
    void alarm_following_list_fail() throws Exception {
        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


}