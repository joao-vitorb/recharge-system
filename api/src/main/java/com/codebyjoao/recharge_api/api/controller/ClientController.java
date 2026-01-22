package com.codebyjoao.recharge_api.api.controller;

import com.codebyjoao.recharge_api.api.dto.ClientResponse;
import com.codebyjoao.recharge_api.api.dto.CreateClientRequest;
import com.codebyjoao.recharge_api.api.dto.UpdateClientRequest;
import com.codebyjoao.recharge_api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> create(
            @Valid @RequestBody CreateClientRequest req,
            UriComponentsBuilder uriBuilder) {
        ClientResponse created = clientService.create(req);

        var location = uriBuilder
                .path("/api/v1/clients/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(clientService.list(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequest req) {
        return ResponseEntity.ok(clientService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
