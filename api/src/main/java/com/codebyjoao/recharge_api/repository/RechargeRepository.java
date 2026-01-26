package com.codebyjoao.recharge_api.repository;

import com.codebyjoao.recharge_api.domain.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RechargeRepository extends JpaRepository<Recharge, Long> {

    List<Recharge> findAllByClient_IdOrderByIdDesc(Long clientId);
}
