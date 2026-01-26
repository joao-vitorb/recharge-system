package com.codebyjoao.recharge_api.api.dto;

public record RechargeResultMessage(
        Long rechargeId,
        String status,
        String failureReason
) {}
