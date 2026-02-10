# Recharge System — Spring Boot (API + Platform) + RabbitMQ + PostgreSQL

Projeto com processamento **assíncrono** de recargas usando **RabbitMQ**, composto por dois serviços **Spring Boot**:

- **API** (`api/`): expõe endpoints REST, persiste dados no **PostgreSQL**, publica solicitações de recarga e consome resultados.
- **Platform** (`platform/`): serviço **mock** que consome solicitações de recarga e publica o resultado (sucesso/falha) no RabbitMQ.

---

## Visão geral

O sistema permite:

1. **Cadastrar clientes**
2. **Cadastrar métodos de pagamento** vinculados a um cliente
3. **Criar recargas**
   - A recarga nasce como `PENDING`
   - A API publica um evento no RabbitMQ
   - A Platform processa (mock) e devolve o resultado
   - A API atualiza para `SUCCESS` ou `FAILED`

---

## Arquitetura e fluxo

### Componentes

- **PostgreSQL**: persistência de `clients`, `payments`, `recharges`
- **RabbitMQ**: comunicação assíncrona entre `api` e `platform`
- **API**:
  - REST + validação de payloads
  - JPA + Flyway
  - Publicação em `recharge.requests`
  - Consumo de `recharge.results`
- **Platform**:
  - Consumo de `recharge.requests`
  - Processamento mock (regra simples)
  - Publicação em `recharge.results`

### Fluxo (alto nível)

1. `POST /api/v1/recharges`
   - API valida `clientId` e `paymentId`
   - Salva `recharge` no Postgres com status `PENDING`
   - Publica `RechargeRequestMessage` na exchange `recharge.exchange` com routing key `recharge.request`
2. Platform consome `recharge.requests`
   - Processa (mock) e gera `SUCCESS` ou `FAILED`
   - Publica `RechargeResultMessage` na exchange com routing key `recharge.result`
3. API consome `recharge.results`
   - Atualiza o status da recarga no Postgres

---

## Tecnologias

- **Java 21**
- **Spring Boot 3**
- Spring Web
- Spring Data JPA (Hibernate)
- Flyway (migrations)
- Spring AMQP (RabbitMQ)
- PostgreSQL
- Docker + Docker Compose
- Lombok
- OpenAPI / Swagger UI (Springdoc)

---

## Configuração

### Infra (Docker Compose)

O `docker-compose.yml` na raiz sobe:

- Postgres em `localhost:5432`
- RabbitMQ em `localhost:5672`
- (se configurado) RabbitMQ UI em `localhost:15672`

### Config da API

Arquivo: `api/src/main/resources/application.yml`

- Datasource (Postgres)
- Flyway
- RabbitMQ
- UTF-8 encoding forçado (para reduzir problemas de terminal no Windows)

### Config da Platform

Arquivo: `platform/src/main/resources/application.yml`

- RabbitMQ host/porta/credenciais

---

## Como rodar (Windows / Linux / macOS)

### 1) Clonar o repositório

```
git clone https://github.com/joao-vitorb/recharge-system.git
cd recharge-system
```

### 2) Subir tudo com Docker Compose (Postgres + RabbitMQ + API + Platform)

```
docker compose up -d --build
docker compose ps
```

#### Acessos

#### API: `http://localhost:8080`
#### RabbitMQ UI: `http://localhost:15672` (user/pass: guest / guest)

### 3) Ver logs (opcional)

```
docker compose logs -f api
```

#### Em outro terminal

```
docker compose logs -f platform
```

### 4) Parar tudo (opcional)

```
docker compose down
```

---

## Endpoints

### Clients
- POST /api/v1/clients
- GET /api/v1/clients
- GET /api/v1/clients/{id}
- PUT /api/v1/clients/{id}
- DELETE /api/v1/clients/{id}

### Payments (por cliente)
- POST /api/v1/clients/{clientId}/payments
- GET /api/v1/clients/{clientId}/payments
- DELETE /api/v1/clients/{clientId}/payments/{paymentId}

### Recharges
- POST /api/v1/recharges
- GET /api/v1/clients/{clientId}/recharges

---

## Testes rápidos

### 1) Criar cliente

#### Windows (PowerShell)
```powershell
Invoke-RestMethod -Method POST `
  -Uri "http://localhost:8080/api/v1/clients" `
  -ContentType "application/json; charset=utf-8" `
  -Body '{"name":"Joao","phone":"31999999999"}'
```

#### Linux/macOS (bash/zsh)
```bash
curl -X POST "http://localhost:8080/api/v1/clients" \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{"name":"Joao","phone":"31999999999"}'
```

### 2) Criar payment (clientId = 1)

#### Windows (PowerShell)
```powershell
Invoke-RestMethod -Method POST `
  -Uri "http://localhost:8080/api/v1/clients/1/payments" `
  -ContentType "application/json" `
  -Body '{"type":"PIX","token":"pix_key_123"}'
```

#### Linux/macOS (bash/zsh)
```bash
curl -X POST "http://localhost:8080/api/v1/clients/1/payments" \
  -H "Content-Type: application/json" \
  -d '{"type":"PIX","token":"pix_key_123"}'
```

### 3) Criar recarga (PENDING → SUCCESS/FAILED)

#### Windows (PowerShell)
```powershell
Invoke-RestMethod -Method POST `
  -Uri "http://localhost:8080/api/v1/recharges" `
  -ContentType "application/json" `
  -Body '{"clientId":1,"paymentId":1,"phone":"31999999999","amount":50.00}'
```

#### Linux/macOS (bash/zsh)
```bash
curl -X POST "http://localhost:8080/api/v1/recharges" \
  -H "Content-Type: application/json" \
  -d '{"clientId":1,"paymentId":1,"phone":"31999999999","amount":50.00}'
```

#### A resposta inicial tende a voltar com `status: PENDING`

### 4) Listar recargas (clientId = 1)

#### Windows (PowerShell)
```powershell
Invoke-RestMethod -Method GET `
  -Uri "http://localhost:8080/api/v1/clients/1/recharges"
```

#### Linux/macOS (bash/zsh)
```bash
curl "http://localhost:8080/api/v1/clients/1/recharges"
```

### 5) Testar falha (mock da Platform)

#### Windows (PowerShell)
```powershell
Invoke-RestMethod -Method POST `
  -Uri "http://localhost:8080/api/v1/recharges" `
  -ContentType "application/json" `
  -Body '{"clientId":1,"paymentId":1,"phone":"31999999999","amount":500.00}'
```

#### Linux/macOS (bash/zsh)
```bash
curl -X POST "http://localhost:8080/api/v1/recharges" \
  -H "Content-Type: application/json" \
  -d '{"clientId":1,"paymentId":1,"phone":"31999999999","amount":500.00}'
```

#### Depois liste de novo e verifique `FAILED` + `failureReason`

---

## RabbitMQ (filas e exchange)

#### O projeto usa um Direct Exchange:

- Exchange: recharge.exchange
- Queue: recharge.requests
- Queue: recharge.results
- Routing Keys:
    - recharge.request → recharge.requests
    - recharge.result → recharge.results

### Contratos de mensagem (resumo)

#### Request (API → Platform):

- rechargeId
- clientId
- paymentId
- phone
- amount
- paymentToken
- paymentType

#### Result (Platform → API):

- rechargeId
- status (SUCCESS | FAILED)
- failureReason

---

## Banco de dados e migrations (Flyway)

- Migrações em: api/src/main/resources/db/migration/
- Flyway executa automaticamente ao subir a API.
- Entidades persistidas:
    - Client
    - Payment
    - Recharge

---

## Estrutura do repositório

```
recharge-system/
  api/
    src/main/java/com/codebyjoao/recharge_api/
      api/controller/
      api/dto/
      config/
      domain/entity/
      exception/
      repository/
      service/
    src/main/resources/
      application.yml
      db/migration/
    pom.xml
  platform/
    src/main/java/com/codebyjoao/recharge_platform/
      config/
      dto/
      service/
    src/main/resources/
      application.yml
    pom.xml
  docker-compose.yml
  README.md
```

---

## Autor

#### Desenvolvido por *João Borges* para demonstração técnica.