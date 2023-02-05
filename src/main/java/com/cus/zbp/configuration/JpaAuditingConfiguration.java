package com.cus.zbp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Autoscan되어서 자동으로 Bean 등록된다
@Configuration
@EnableJpaAuditing // jpa auditing 켜진 상태로, 값 자동 저장 지원
public class JpaAuditingConfiguration {
}
