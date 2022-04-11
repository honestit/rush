package com.github.rush.zerowaste.service.rpc;

import com.github.rush.services.zerowaste.GetSuggestionsRequest;
import com.github.rush.services.zerowaste.GetSuggestionsResponse;

public interface GetSuggestionsProvider {

  GetSuggestionsResponse getSuggestions(GetSuggestionsRequest request);
}
