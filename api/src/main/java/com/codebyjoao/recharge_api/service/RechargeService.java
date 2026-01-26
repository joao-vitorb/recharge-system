package com.codebyjoao.recharge_api.service;

import com.codebyjoao.recharge_api.api.dto.CreateRechargeRequest;
import com.codebyjoao.recharge_api.api.dto.RechargeRequestMessage;
import com.codebyjoao.recharge_api.api.dto.RechargeResponse;
import com.codebyjoao.recharge_api.config.RabbitConfig;
import com.codebyjoao.recharge_api.domain.entity.*;
import com.codebyjoao.recharge_api.exception.NotFoundException;
import com.codebyjoao.recharge_api.repository.ClientRepository;
import com.codebyjoao.recharge_api.repository.PaymentRepository;
import com.codebyjoao.recharge_api.repository.RechargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargeService {

    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;
    private final RechargeRepository rechargeRepository;
    private final RabbitTemplate rabbitTemplate;

    public RechargeResponse create(CreateRechargeRequest req) {
        Client client = clientRepository.findById(req.clientId())
                .orElseThrow(() -> new NotFoundException("Client not found: " + req.clientId()));

        Payment payment = paymentRepository.findByIdAndClientId(req.paymentId(), req.clientId())
                .orElseThrow(() -> new NotFoundException("Payment not found: " + req.paymentId() + " for client: " + req.clientId()));

        Recharge recharge = Recharge.builder()
                .client(client)
                .payment(payment)
                .phone(req.phone())
                .amount(req.amount())
                .status(RechargeStatus.PENDING)
                .failureReason(null)
                .build();

        Recharge saved = rechargeRepository.save(recharge);

        RechargeRequestMessage msg = new RechargeRequestMessage(
                saved.getId(),
                client.getId(),
                payment.getId(),
                saved.getPhone(),
                saved.getAmount(),
                payment.getToken(),
                payment.getType().name()
        );

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RECHARGE_REQUEST_KEY, msg);

        return toResponse(saved);
    }

    public List<RechargeResponse> listByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException("Client not found: " + clientId);
        }

        return rechargeRepository.findAllByClient_IdOrderByIdDesc(clientId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyResult(Long rechargeId, RechargeStatus status, String failureReason) {
        Recharge recharge = rechargeRepository.findById(rechargeId)
                .orElseThrow(() -> new NotFoundException("Recharge not found: " + rechargeId));

        recharge.setStatus(status);
        recharge.setFailureReason(failureReason);
        rechargeRepository.save(recharge);
    }

    private RechargeResponse toResponse(Recharge r) {
        return new RechargeResponse(
                r.getId(),
                r.getClient().getId(),
                r.getPayment().getId(),
                r.getPhone(),
                r.getAmount(),
                r.getStatus(),
                r.getFailureReason(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
