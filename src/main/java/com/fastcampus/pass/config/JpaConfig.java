package com.fastcampus.pass.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //entity 필드내의 속성에서 시간 값을 자동으로 넣어준다.
@Configuration
public class JpaConfig {
}
