package com.videoStreaming.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.videoStreaming.config.Constants;
import com.videoStreaming.security.SecurityUtils;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserUsername().orElse(Constants.SYSTEM_ACCOUNT));
	}

}
