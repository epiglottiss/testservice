package com.cus.zbp.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInput {
  String email;
  String password;
  String name;
  Boolean noEmailAuth;

}
