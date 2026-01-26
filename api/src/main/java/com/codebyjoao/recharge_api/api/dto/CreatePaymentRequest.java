package com.codebyjoao.recharge_api.api.dto;

import com.codebyjoao.recharge_api.domain.entity.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePaymentRequest(
        @NotNull PaymentType type,
        @NotBlank @Size(max = 120) String token
) {}
