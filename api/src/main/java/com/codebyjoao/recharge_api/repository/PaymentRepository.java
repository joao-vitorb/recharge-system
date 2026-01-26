package com.codebyjoao.recharge_api.repository;

import com.codebyjoao.recharge_api.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByClientId(Long clientId);

    Optional<Payment> findByIdAndClientId(Long id, Long clientId);

    boolean existsByIdAndClientId(Long id, Long clientId);
}
