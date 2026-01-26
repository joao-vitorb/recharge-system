package com.codebyjoao.recharge_api.api.dto;

import java.math.BigDecimal;

public record RechargeRequestMessage(
        Long rechargeId,
        Long clientId,
        Long paymentId,
        String phone,
        BigDecimal amount,
        String paymentToken,
        String paymentType
) {}
