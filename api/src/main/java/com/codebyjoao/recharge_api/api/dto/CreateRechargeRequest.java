package com.codebyjoao.recharge_api.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateRechargeRequest(
        @NotNull Long clientId,
        @NotNull Long paymentId,
        @NotBlank @Size(max = 20) String phone,
        @NotNull @Positive BigDecimal amount
) {}
