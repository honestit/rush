package com.github.rush.zerowaste.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.rush.services.zerowaste.GetSuggestionsRequest;
import com.github.rush.services.zerowaste.GetSuggestionsResponse;
import com.github.rush.zerowaste.configuration.ZeroWasteServiceTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import io.grpc.internal.testing.StreamRecorder;

@SpringBootTest
@SpringJUnitConfig(classes = { ZeroWasteServiceTestConfiguration.class })
public class ZeroWasteServiceTest {

  @Autowired
  private ZeroWasteServiceImpl zeroWasteService;

  @Test
  public void testGetSuggestionsShouldReturnDefault() throws Exception {
    GetSuggestionsRequest request = GetSuggestionsRequest.getDefaultInstance();
    StreamRecorder<GetSuggestionsResponse> responseObserver = StreamRecorder.create();

    zeroWasteService.getSuggestions(request, responseObserver);

    if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
      fail("The call did not terminate in time");
    }
    assertNull(responseObserver.getError());
    List<GetSuggestionsResponse> results = responseObserver.getValues();
    assertEquals(1, results.size());
    GetSuggestionsResponse response = results.get(0);
    assertEquals(GetSuggestionsResponse.getDefaultInstance(), response);
  }
}
