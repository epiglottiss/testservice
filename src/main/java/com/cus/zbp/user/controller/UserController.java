package com.cus.zbp.user.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.cus.zbp.user.model.PasswordResetInput;
import com.cus.zbp.user.model.UserInput;
import com.cus.zbp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
  private final UserService userService;

  @GetMapping("/user/login")
  public String login() {
    return "user/login";
  }

  @PostMapping("/user/register")
  public String register(Model model, HttpServletRequest request, UserInput parameter) {
    boolean result = true;
    try {
      userService.register(parameter);
    } catch (Exception e) {
      result = false;
      log.error(e.getMessage());
    }
    model.addAttribute("result", result);
    return "user/register_complete";
  }

  @GetMapping("/user/email-auth")
  public String emailAuth(Model model, HttpServletRequest request) {

    String uuid = request.getParameter("id");
    System.out.println(uuid);

    boolean result = true;

    try {
      userService.emailAuth(uuid);
    } catch (Exception e) {
      result = false;
      log.error(e.getMessage());
    }
    model.addAttribute("result", result);

    return "user/email_auth";
  }

  @GetMapping("/user/find/password")
  public String findPassword() {
    return "user/find_password";
  }

  @PostMapping("/user/find/password")
  public String findPassword(Model model, PasswordResetInput parameter) {
    boolean result = true;
    try {
      userService.sendResetPassword(parameter);
    } catch (Exception e) {
      result = false;
      log.error(e.getMessage());
    }
    model.addAttribute("result", result);

    return "user/find_password_result";
  }

  // @GetMapping("/user/reset/password")
  // public String resetPassword(Model model, HttpServletRequest request) {
  //
  // boolean result = true;
  // try {
  // userService.checkResetPassword(request.getParameter("id"));
  // } catch (Exception e) {
  // result = false;
  // log.error(e.getMessage());
  // }
  // model.addAttribute("result", result);
  // return "user/reset_password";
  // }

  @PostMapping("/user/reset/password")
  public String resetPassword(Model model, PasswordResetInput parameter) {
    boolean result = true;
    try {
      userService.resetPassword(parameter.getId(), parameter.getPassword());
    } catch (Exception e) {
      result = false;
      log.error(e.getMessage());
    }

    model.addAttribute("result", result);
    return "user/reset_password_result";
  }

}
