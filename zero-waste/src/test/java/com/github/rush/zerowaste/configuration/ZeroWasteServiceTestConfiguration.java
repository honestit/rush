package com.github.rush.zerowaste.configuration;

import com.github.rush.zerowaste.service.ZeroWasteServiceImpl;
import com.github.rush.zerowaste.service.rpc.GetSuggestionsProvider;
import com.github.rush.zerowaste.service.rpc.GetSuggestionsProviderImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZeroWasteServiceTestConfiguration {

  @Bean
  GetSuggestionsProvider getSuggestionsProvider() {
    return new GetSuggestionsProviderImpl();
  }

  @Bean
  ZeroWasteServiceImpl zeroWasteService(GetSuggestionsProvider getSuggestionsProvider) {
    return new ZeroWasteServiceImpl(getSuggestionsProvider);
  }
}