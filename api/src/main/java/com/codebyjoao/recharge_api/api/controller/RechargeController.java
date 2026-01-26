package com.codebyjoao.recharge_api.api.controller;

import com.codebyjoao.recharge_api.api.dto.CreateRechargeRequest;
import com.codebyjoao.recharge_api.api.dto.RechargeResponse;
import com.codebyjoao.recharge_api.service.RechargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RechargeController {

    private final RechargeService rechargeService;

    @PostMapping("/recharges")
    public ResponseEntity<RechargeResponse> create(
            @Valid @RequestBody CreateRechargeRequest req,
            UriComponentsBuilder uriBuilder
    ) {
        RechargeResponse created = rechargeService.create(req);

        var location = uriBuilder
                .path("/api/v1/recharges/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/clients/{clientId}/recharges")
    public ResponseEntity<List<RechargeResponse>> listByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(rechargeService.listByClient(clientId));
    }
}
