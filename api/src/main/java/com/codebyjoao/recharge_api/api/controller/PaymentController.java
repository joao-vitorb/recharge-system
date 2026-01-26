package com.codebyjoao.recharge_api.api.controller;

import com.codebyjoao.recharge_api.api.dto.CreatePaymentRequest;
import com.codebyjoao.recharge_api.api.dto.PaymentResponse;
import com.codebyjoao.recharge_api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @PathVariable Long clientId,
            @Valid @RequestBody CreatePaymentRequest req,
            UriComponentsBuilder uriBuilder
    ) {
        PaymentResponse created = paymentService.create(clientId, req);

        var location = uriBuilder
                .path("/api/v1/clients/{clientId}/payments/{paymentId}")
                .buildAndExpand(clientId, created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> list(@PathVariable Long clientId) {
        return ResponseEntity.ok(paymentService.listByClient(clientId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long clientId,
            @PathVariable Long paymentId
    ) {
        paymentService.delete(clientId, paymentId);
        return ResponseEntity.noContent().build();
    }
}
