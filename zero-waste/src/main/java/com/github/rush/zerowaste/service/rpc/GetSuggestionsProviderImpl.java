package com.github.rush.zerowaste.service.rpc;

import com.github.rush.services.zerowaste.GetSuggestionsRequest;
import com.github.rush.services.zerowaste.GetSuggestionsResponse;

import org.springframework.stereotype.Service;

@Service
public class GetSuggestionsProviderImpl implements GetSuggestionsProvider {

  @Override
  public GetSuggestionsResponse getSuggestions(GetSuggestionsRequest request) {
    return GetSuggestionsResponse.getDefaultInstance();
  }
}
