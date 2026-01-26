package com.codebyjoao.recharge_api.api.dto;

import com.codebyjoao.recharge_api.domain.entity.RechargeStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record RechargeResponse(
        Long id,
        Long clientId,
        Long paymentId,
        String phone,
        BigDecimal amount,
        RechargeStatus status,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {}
