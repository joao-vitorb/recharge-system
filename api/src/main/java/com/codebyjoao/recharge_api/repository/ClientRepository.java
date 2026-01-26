package com.codebyjoao.recharge_api.repository;

import com.codebyjoao.recharge_api.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
