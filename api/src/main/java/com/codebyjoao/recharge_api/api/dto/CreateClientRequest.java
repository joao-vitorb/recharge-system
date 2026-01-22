package com.codebyjoao.recharge_api.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 20) String phone
) {}
