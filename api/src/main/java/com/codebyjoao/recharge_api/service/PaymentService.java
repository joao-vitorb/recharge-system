package com.codebyjoao.recharge_api.service;

import com.codebyjoao.recharge_api.api.dto.CreatePaymentRequest;
import com.codebyjoao.recharge_api.api.dto.PaymentResponse;
import com.codebyjoao.recharge_api.domain.entity.Client;
import com.codebyjoao.recharge_api.domain.entity.Payment;
import com.codebyjoao.recharge_api.exception.NotFoundException;
import com.codebyjoao.recharge_api.repository.ClientRepository;
import com.codebyjoao.recharge_api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;

    public PaymentResponse create(Long clientId, CreatePaymentRequest req) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + clientId));

        Payment payment = Payment.builder()
                .client(client)
                .type(req.type())
                .token(req.token())
                .build();

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    public List<PaymentResponse> listByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException("Client not found: " + clientId);
        }
        return paymentRepository.findAllByClientId(clientId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long clientId, Long paymentId) {
        if (!paymentRepository.existsByIdAndClientId(paymentId, clientId)) {
            throw new NotFoundException("Payment not found: " + paymentId + " for client: " + clientId);
        }
        paymentRepository.deleteById(paymentId);
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getClient().getId(),
                p.getType(),
                p.getToken(),
                p.getCreatedAt()
        );
    }
}
