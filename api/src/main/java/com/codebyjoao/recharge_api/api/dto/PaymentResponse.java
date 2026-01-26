package com.codebyjoao.recharge_api.api.dto;

import com.codebyjoao.recharge_api.domain.entity.PaymentType;

import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long clientId,
        PaymentType type,
        String token,
        Instant createdAt
) {}
