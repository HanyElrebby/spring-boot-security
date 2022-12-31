package com.videoStreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class VideoStreamingApplication {
	
	@Bean
	public AuditorAware<String> auditorAware() {
		return new com.videoStreaming.audit.AuditorAwareImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(VideoStreamingApplication.class, args);
	}

}
