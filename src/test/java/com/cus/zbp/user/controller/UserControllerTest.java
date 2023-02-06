package com.cus.zbp.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.cus.zbp.user.model.PasswordResetInput;
import com.cus.zbp.user.model.UserInput;
import com.cus.zbp.user.service.UserService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

// https://stackoverflow.com/questions/39554285/spring-test-returning-401-for-unsecured-urls
// web security로 인해 mockMvc가 401(Unauthorized) 반환하는 문제 해결
@WebMvcTest(controllers = UserController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testLogin() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/user/login")).andDo(print()).andExpect(view().name(("user/login")))
        .andExpect(status().isOk());
  }

  @Test
  void successRegister() throws Exception {
    // given
    UserInput userInput = UserInput.builder().email("not-exist-eamil@naver1.com").name("noname")
        .password("password").build();
    // when
    // then
    mockMvc
        .perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userInput)))
        .andExpect(status().isOk()).andExpect(view().name("user/register_complete"))
        .andExpect(model().attribute("result", true));
  }

  @Test
  void failRegister() throws Exception {
    // given
    UserInput userInput =
        UserInput.builder().email("not-exist-eamil@naver1.com").name("noname").build();
    // when
    doThrow(Exception.class).when(userService).register(any());
    // then
    mockMvc
        .perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userInput)))
        .andExpect(status().isOk()).andExpect(view().name("user/register_complete"))
        .andExpect(model().attribute("result", false));
  }

  @Test
  void successEmailAuth() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/user/email-auth?id=test1234")).andDo(print()).andExpect(status().isOk())
        .andExpect(model().attribute("result", true)).andExpect(view().name("user/email_auth"));
  }

  @Test
  void failEmailAuth() throws Exception {
    // given
    // mocking된 void 반환 함수 exception 만들기
    // https://www.baeldung.com/mockito-void-methods
    doThrow(Exception.class).when(userService).emailAuth(anyString());
    // when
    // then
    mockMvc.perform(get("/user/email-auth?id=test1234")).andDo(print()).andExpect(status().isOk())
        .andExpect(model().attribute("result", false)).andExpect(view().name("user/email_auth"));
  }

  @Test
  void getFindPasswordView() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/user/find/password")).andDo(print())
        .andExpect(view().name(("user/find_password"))).andExpect(status().isOk());
  }

  @Test
  void successFindPasswordRequest() throws JacksonException, Exception {
    // given
    PasswordResetInput passwordInput =
        PasswordResetInput.builder().id("reset-uuid").password("new-password").build();
    // when
    // then
    mockMvc
        .perform(post("/user/find/password").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordInput)))
        .andExpect(status().isOk()).andExpect(view().name("user/find_password_result"))
        .andExpect(model().attribute("result", true));
  }

  @Test
  void failFindPasswordRequest() throws JacksonException, Exception {
    // given
    PasswordResetInput passwordInput =
        PasswordResetInput.builder().id("reset-uuid").password("new-password").build();
    doThrow(Exception.class).when(userService).sendResetPassword(any());
    // when
    // then
    mockMvc
        .perform(post("/user/find/password").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordInput)))
        .andExpect(status().isOk()).andExpect(view().name("user/find_password_result"))
        .andExpect(model().attribute("result", false));
  }

  // @Test
  // void getResetPasswordViewWhenPossible() throws Exception {
  // // given
  // // when
  // // then
  // mockMvc.perform(get("/user/reset/password")).andDo(print())
  // .andExpect(view().name(("user/reset_password"))).andExpect(status().isOk())
  // .andExpect(model().attribute("result", true));
  // }

  // @Test
  // void getResetPasswordViewWhenImpossible() throws Exception {
  // // given
  // // when
  // // then
  // mockMvc.perform(get("/user/reset/password")).andDo(print())
  // .andExpect(view().name(("user/reset_password"))).andExpect(status().isOk())
  // .andExpect(model().attribute("result", false));
  // }


  @Test
  void successResetPassword() throws Exception {
    // given
    PasswordResetInput passwordInput =
        PasswordResetInput.builder().id("reset-uuid").password("new-password").build();
    // when
    // then
    mockMvc
        .perform(post("/user/reset/password").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordInput)))
        .andExpect(status().isOk()).andExpect(view().name("user/reset_password_result"))
        .andExpect(model().attribute("result", true));
  }

  @Test
  void failResetPassword() throws Exception {
    // given
    PasswordResetInput passwordInput =
        PasswordResetInput.builder().id("reset-uuid").password("new-password").build();

    doThrow(Exception.class).when(userService).resetPassword(anyString(), anyString());
    // when
    // then
    mockMvc
        .perform(post("/user/reset/password").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordInput)))
        .andExpect(status().isOk()).andExpect(view().name("user/reset_password_result"))
        .andExpect(model().attribute("result", false));
  }
}
