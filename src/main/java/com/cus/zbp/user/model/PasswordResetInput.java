package com.cus.zbp.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetInput {
  private String email;
  private String name;
  private String id;
  private String password;
}
