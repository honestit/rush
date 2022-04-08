package com.github.rush.zerowaste.service;

import com.github.rush.services.zerowaste.GetSuggestionsRequest;
import com.github.rush.services.zerowaste.GetSuggestionsResponse;
import com.github.rush.services.zerowaste.ZeroWasteServiceGrpc.ZeroWasteServiceImplBase;
import com.github.rush.zerowaste.service.rpc.GetSuggestionsProvider;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ZeroWasteServiceImpl extends ZeroWasteServiceImplBase {

  private final GetSuggestionsProvider getSuggestionsProvider;

  public ZeroWasteServiceImpl(GetSuggestionsProvider getSuggestionsProvider) {
    this.getSuggestionsProvider = getSuggestionsProvider;
  }

  @Override
  public void getSuggestions(GetSuggestionsRequest request, StreamObserver<GetSuggestionsResponse> responseObserver) {
    responseObserver.onNext(getSuggestionsProvider.getSuggestions(request));
    responseObserver.onCompleted();
  }

}
