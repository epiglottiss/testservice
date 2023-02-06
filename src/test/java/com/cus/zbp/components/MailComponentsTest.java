package com.cus.zbp.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailComponentsTest {

  @MockBean
  private JavaMailSender javaMailSender;

  @Autowired
  private MailComponents mailComponents;

  @Test
  void testSendMail() {
    // JavaMailSender javaMailSender = new JavaMailSenderImpl();

    mailComponents = new MailComponents(javaMailSender);
    boolean result = mailComponents.sendMail("iop245@naver.com", "subject", "text");

    assertEquals(true, result);
  }

}
