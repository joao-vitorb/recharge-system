package com.codebyjoao.recharge_api.api.dto;

import java.time.Instant;

public record ClientResponse(
        Long id,
        String name,
        String phone,
        Instant createdAt
) {}
