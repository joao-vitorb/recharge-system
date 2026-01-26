package com.codebyjoao.recharge_api.service;

import com.codebyjoao.recharge_api.api.dto.RechargeResultMessage;
import com.codebyjoao.recharge_api.config.RabbitConfig;
import com.codebyjoao.recharge_api.domain.entity.RechargeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RechargeResultListener {

    private final RechargeService rechargeService;

    @RabbitListener(queues = RabbitConfig.RECHARGE_RESULTS_QUEUE)
    public void onMessage(RechargeResultMessage msg) {
        RechargeStatus status = RechargeStatus.valueOf(msg.status());
        rechargeService.applyResult(msg.rechargeId(), status, msg.failureReason());
    }
}
