package com.rtbplatform.dsp.service;

import com.rtbplatform.auction.dto.BidRequest;
import com.rtbplatform.auction.dto.BidResponse;
import com.rtbplatform.dsp.entity.Dsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DspCallerService {

    private final RestClient restClient;
    private final Executor dspExecutor;

    public DspCallerService(@Qualifier("dspExecutor") Executor dspExecutor) {
        this.restClient = RestClient.builder().build();
        this.dspExecutor = dspExecutor;
    }

    public CompletableFuture<BidResponse> callDspAsync(Dsp dsp, BidRequest bidRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Perform synchronous call inside the async executor thread
                return restClient.post()
                        .uri(dsp.getEndpointUrl())
                        .body(bidRequest)
                        .retrieve()
                        .body(BidResponse.class);
            } catch (Exception ex) {
                log.warn("DSP {} failed or timed out: {}", dsp.getName(), ex.getMessage());
                return null;
            }
        }, dspExecutor).orTimeout(dsp.getTimeoutMs(), TimeUnit.MILLISECONDS)
          .exceptionally(ex -> {
              log.warn("DSP {} exceeded timeout of {}ms", dsp.getName(), dsp.getTimeoutMs());
              return null;
          });
    }
}
