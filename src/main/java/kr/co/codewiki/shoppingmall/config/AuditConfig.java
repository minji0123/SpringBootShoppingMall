package kr.co.codewiki.shoppingmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // jpa auditing 기능 활성화
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() { // auditoraware: 등록자와 수정자를 처리
        return new AuditorAwareImpl();
    }

}
