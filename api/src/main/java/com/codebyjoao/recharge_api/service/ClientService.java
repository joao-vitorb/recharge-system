package com.codebyjoao.recharge_api.service;

import com.codebyjoao.recharge_api.api.dto.ClientResponse;
import com.codebyjoao.recharge_api.api.dto.CreateClientRequest;
import com.codebyjoao.recharge_api.api.dto.UpdateClientRequest;
import com.codebyjoao.recharge_api.domain.entity.Client;
import com.codebyjoao.recharge_api.exception.NotFoundException;
import com.codebyjoao.recharge_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientResponse create(CreateClientRequest req) {
        Client client = Client.builder()
                .name(req.name())
                .phone(req.phone())
                .build();

        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }

    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found: " + id));
        return toResponse(client);
    }

    public Page<ClientResponse> list(Pageable pageable) {
        return clientRepository.findAll(pageable).map(this::toResponse);
    }

    public ClientResponse update(Long id, UpdateClientRequest req) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found: " + id));

        client.setName(req.name());
        client.setPhone(req.phone());

        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Client not found: " + id);
        }
        clientRepository.deleteById(id);
    }

    private ClientResponse toResponse(Client c) {
        return new ClientResponse(
                c.getId(),
                c.getName(),
                c.getPhone(),
                c.getCreatedAt());
    }
}
