package com.codebyjoao.recharge_platform.service;

import com.codebyjoao.recharge_platform.config.RabbitConfig;
import com.codebyjoao.recharge_platform.dto.RechargeRequestMessage;
import com.codebyjoao.recharge_platform.dto.RechargeResultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class RechargeRequestListener {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.RECHARGE_REQUESTS_QUEUE)
    public void onMessage(RechargeRequestMessage msg) {
        boolean ok = msg.amount().compareTo(new BigDecimal("200.00")) <= 0;

        RechargeResultMessage result = ok
                ? new RechargeResultMessage(msg.rechargeId(), "SUCCESS", null)
                : new RechargeResultMessage(msg.rechargeId(), "FAILED", "Amount exceeds platform limit");

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RECHARGE_RESULT_KEY, result);
    }
}
