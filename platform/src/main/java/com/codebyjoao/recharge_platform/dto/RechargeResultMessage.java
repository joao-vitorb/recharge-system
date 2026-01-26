package com.codebyjoao.recharge_platform.dto;

public record RechargeResultMessage(
        Long rechargeId,
        String status,
        String failureReason
) {}
