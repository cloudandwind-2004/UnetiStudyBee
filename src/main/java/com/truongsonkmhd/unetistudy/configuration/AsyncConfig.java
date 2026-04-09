package com.truongsonkmhd.unetistudy.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Cấu hình để hỗ trợ @Async giúp gửi mail không block thread chính.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
