# Execution and Troubleshooting Guide

[English](#english) | [Português (Brasil)](#português-brasil)

---

<a name="english"></a>

## English

This guide provides step-by-step instructions for building, running, and testing the e-commerce application, along with troubleshooting steps for common runtime, compilation, and IDE issues.

### Table of Contents

- [1. Prerequisites](#1-prerequisites)
- [2. Database Setup](#2-database-setup)
- [3. Running the Application](#3-running-the-application)
  - [Option A: Running with Docker Compose (Recommended)](#option-a-running-with-docker-compose-recommended)
  - [Option B: Running Locally with Maven](#option-b-running-locally-with-maven)
  - [Testing Endpoints and Authentication (curl)](#testing-endpoints-and-authentication-curl)
- [4. Running the Tests](#4-running-the-tests)
- [5. Troubleshooting and Common Errors](#5-troubleshooting-and-common-errors)
  - [Error 1: PostgreSQL Connection Refused](#error-1-postgresql-connection-refused)
  - [Error 2: Release 25 is Not Found in the System](#error-2-release-25-is-not-found-in-the-system)
  - [Error 3: Project Configuration Is Not Up-To-Date with pom.xml](#error-3-project-configuration-is-not-up-to-date-with-pomxml)
  - [Error 4: Port 8080 Was Already in Use](#error-4-port-8080-was-already-in-use)
  - [Error 5: Conflicting Bean Definitions / Stale Target Classes](#error-5-conflicting-bean-definitions--stale-target-classes)
  - [Error 6: Permission Denied for ./mvnw](#error-6-permission-denied-for-mvnw)
  - [Error 7: Flyway Schema History or Checksum Mismatch](#error-7-flyway-schema-history-or-checksum-mismatch)
  - [Error 8: Git Index Smaller Than Expected](#error-8-git-index-smaller-than-expected)
  - [Error 9: Null Type Safety Warning in SecurityConfig](#error-9-null-type-safety-warning-in-securityconfig)
  - [Error 10: 401 Unauthorized or 403 Forbidden](#error-10-401-unauthorized-or-403-forbidden)

---

### 1. Prerequisites

Ensure your development environment meets the following requirements:

- **Java 21 JDK** installed: Verify by running `java -version`.
- **PostgreSQL 16** installed and running on port `5432`.
- Git installed.

---

### 2. Database Setup

The application connects to a PostgreSQL database named `ecommerce_db`.

#### Step 1: Create Database

Run the PostgreSQL CLI:

```bash
psql -U postgres -c "CREATE DATABASE ecommerce_db;"
```

#### Step 2: Initialize Database Schemas (Optional / Manual Setup)

If Flyway is enabled (`spring.flyway.enabled=true`), migrations run automatically. If setting up manually:

```bash
psql -U postgres -d ecommerce_db -f 01_create_database.sql
psql -U postgres -d ecommerce_db -f 02_functions_triggers_views.sql
```

#### Step 3: Configure Database Credentials & Secrets

Passwords and credentials are kept in a `.env` file at the project root (ignored by Git):

1. Copy the example file:
   ```bash
   cp .env.example .env
   ```
2. Edit `.env` to set your desired passwords and JWT secrets:
   ```properties
   POSTGRES_DB=ecommerce_db
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=your_secure_password
   HOST_DB_PORT=5433
   DB_USERNAME=postgres
   DB_PASSWORD=your_secure_password
   JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
   JWT_EXPIRATION_MS=86400000
   ```

---

### 3. Running the Application

#### Option A: Running with Docker Compose (Recommended)

Runs both the Spring Boot API and PostgreSQL database in containers with automated database initialization:

```bash
docker compose up --build -d
```

- **API Base URL**: `http://localhost:8080`
- **PostgreSQL Host Port**: `localhost:5433` (mapped to container port `5432` to avoid conflict with native host PostgreSQL)
- **View Logs**: `docker compose logs -f api`
- **Stop Containers**: `docker compose down`
- **Reset Database**: `docker compose down -v`

#### Option B: Running Locally with Maven

Always run commands from the `application/` directory:

```bash
cd application
./mvnw spring-boot:run
```

Once started, the server will be available at:
`http://localhost:8080`

#### Testing Endpoints and Authentication (curl)

##### 1. Register a New User

Register a client account (`ROLE_CLIENT`):

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "Password123!",
    "cpf": "12345678901",
    "role": "ROLE_CLIENT"
  }'
```

Register an administrator account (`ROLE_ADMIN`):

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com",
    "password": "AdminPassword123!",
    "cpf": "98765432100",
    "role": "ROLE_ADMIN"
  }'
```

##### 2. Authenticate and Obtain JWT Token

Send user credentials to `/api/auth/login`:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "AdminPassword123!"
  }'
```

Response contains the bearer token and profile details:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "Admin User",
  "email": "admin@example.com",
  "role": "ROLE_ADMIN"
}
```

##### 3. Access Public Endpoints (No Token Required)

```bash
curl -X GET "http://localhost:8080/api/categories?page=0&size=10"
curl -X GET "http://localhost:8080/api/products?page=0&size=10"
```

##### 4. Access Protected Endpoints with Bearer Token

Send the token via the `Authorization` header:

```bash
curl -X GET "http://localhost:8080/api/orders/user/1" \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

##### 5. Access Admin-Restricted Endpoints

Creating categories or products requires `ROLE_ADMIN`:

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ADMIN_JWT_TOKEN>" \
  -d '{
    "name": "Electronics",
    "description": "Electronic items and gadgets"
  }'
```

---

### 4. Running the Tests

The project includes isolated unit tests for domain entities and application services (using Mockito):

#### Run Entire Test Suite:

```bash
cd application
./mvnw test
```

#### Run a Single Test Class:

```bash
cd application
./mvnw test -Dtest=OrderApplicationServiceTest
```

#### Run Tests with Clean Target Build:

```bash
cd application
./mvnw clean test
```

---

### 5. Troubleshooting and Common Errors

#### Error 1: PostgreSQL Connection Refused

**Symptom**:

```text
org.postgresql.util.PSQLException: The connection attempt failed: Connection to localhost:5432 refused
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource'
```

**Cause**: The local PostgreSQL service is either not running or blocked by a firewall.
**Easiest Fix**:

1. Check if the PostgreSQL service is active:
   ```bash
   sudo systemctl status postgresql
   ```
2. Start the service:
   ```bash
   sudo systemctl start postgresql
   ```
3. Or launch a quick local PostgreSQL instance via Docker:
   ```bash
   docker run -d --name ecommerce-postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=ecommerce_db postgres:16
   ```

---

#### Error 2: Release 25 is Not Found in the System

**Symptom**:

```text
The project was not built due to "release 25 is not found in the system". Fix the problem, then try refreshing this project.
```

**Cause**: The project was originally configured for Java 25, but your system has Java 21 installed. The IDE language server cached the old version.
**Easiest Fix**:

1. Confirm `pom.xml` specifies `<java.version>21</java.version>`.
2. In VS Code:
   - Press `Ctrl + Shift + P`.
   - Type **`Java: Clean Java Language Server Workspace`** and click **Restart and Delete**.
3. In IntelliJ IDEA:
   - Click the Maven panel on the right and click **Reload All Maven Projects** (circular arrows icon).

---

#### Error 3: Project Configuration Is Not Up-To-Date with pom.xml

**Symptom**:
A warning flag appears on line 1 of `pom.xml` stating `Project configuration is not up-to-date with pom.xml, requires an update`.
**Cause**: `pom.xml` was modified on disk while VS Code was open, and the IDE's internal classpath has not been synchronized yet.
**Easiest Fix**:

1. Click on line 1 of `pom.xml`.
2. Press `Ctrl + .` (or click the lightbulb icon).
3. Select **Update project configuration**.

---

#### Error 4: Port 8080 Was Already in Use

**Symptom**:

```text
Web server failed to start. Port 8080 was already in use.
```

**Cause**: A previously running instance of the application or another service is occupying port 8080.
**Easiest Fix**:

1. Kill the process occupying port 8080:
   ```bash
   fuser -k 8080/tcp
   ```
2. Or change the port in `application/src/main/resources/application.properties`:
   ```properties
   server.port=8081
   ```

---

#### Error 5: Conflicting Bean Definitions / Stale Target Classes

**Symptom**:

```text
ConflictingBeanDefinitionException: Annotation-specified bean name 'categoryController' for bean class [...] conflicts with existing, non-compatible bean definition
```

**Cause**: Classes were deleted or refactored into new packages, but obsolete `.class` files still exist in the `target/classes` folder.
**Easiest Fix**:
Clean the target build directory:

```bash
cd application
./mvnw clean compile
```

---

#### Error 6: Permission Denied for ./mvnw

**Symptom**:

```bash
bash: ./mvnw: Permission denied
```

**Cause**: The Maven wrapper script lost executable permissions.
**Easiest Fix**:
Restore execution permission:

```bash
chmod +x application/mvnw
```

---

#### Error 7: Flyway Schema History or Checksum Mismatch

**Symptom**:

```text
FlywayValidateException: Validate failed: Migrations have failed validation
```

**Cause**: A migration script was edited after it had already been executed in the database.
**Easiest Fix**:

1. If in development, repair Flyway schema history table:
   ```bash
   cd application
   ./mvnw flyway:repair
   ```
2. Or drop and recreate the development database:
   ```bash
   psql -U postgres -c "DROP DATABASE ecommerce_db;"
   psql -U postgres -c "CREATE DATABASE ecommerce_db;"
   ```

---

#### Error 8: Git Index Smaller Than Expected

**Symptom**:

```text
fatal: .git/index: index file smaller than expected
```

VS Code repeatedly displays git status errors on file changes (`git status -z -uall`).

**Cause**: The `.git/index` staging file became corrupted or truncated to 0 bytes, typically caused by a sudden system restart, IDE crash, or killed process during index writing.
**Easiest Fix**:

1. Remove the corrupted index file and rebuild it from repository HEAD:
   ```bash
   rm -f .git/index
   git reset
   ```
2. In VS Code, reload the window to clear the internal Git extension cache:
   - Press `Ctrl + Shift + P`.
   - Run **`Developer: Reload Window`**.

---

#### Error 9: Null Type Safety Warning in SecurityConfig

**Symptom**:

```text
Null type safety: parameter 'this' provided via method descriptor Customizer<CsrfConfigurer<HttpSecurity>>.customize(CsrfConfigurer<HttpSecurity>) needs unchecked conversion to conform to '@NonNull AbstractHttpConfigurer<CsrfConfigurer<HttpSecurity>,HttpSecurity>'
```

**Cause**: Eclipse JDT / Java Language Server strict null analysis flags method references such as `AbstractHttpConfigurer::disable` because bytecode lacks explicit `@NonNull` annotations on generic receivers.
**Easiest Fix**:
Replace method references with explicit lambdas in `SecurityConfig.java`:

```java
// Replace:
.csrf(AbstractHttpConfigurer::disable)
.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

// With:
.csrf(csrf -> csrf.disable())
.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

---

#### Error 10: 401 Unauthorized or 403 Forbidden

**Symptom**:

HTTP 401:

```json
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Full authentication is required to access this resource"
}
```

or HTTP 403:

```json
{
  "type": "about:blank",
  "title": "Forbidden",
  "status": 403,
  "detail": "Access Denied"
}
```

**Cause**:

- **401 Unauthorized**: Missing or expired `Authorization: Bearer <token>` header, or token signature validation failed.
- **403 Forbidden**: The authenticated user's assigned role lacks sufficient permissions (e.g., a `ROLE_CLIENT` attempting an administrative route like `POST /api/categories` or `PUT /api/orders/{id}/status`).

**Easiest Fix**:

1. Authenticate via `POST /api/auth/login` to obtain a fresh JWT token.
2. Verify the request header format is exactly `Authorization: Bearer <token>`.
3. Check role requirements in the README.md Endpoints and Access Control section. Log in with a user possessing `ROLE_ADMIN` for administrative endpoints.

---

<a name="português-brasil"></a>

## Português (Brasil)

Este guia fornece instruções detalhadas para compilar, executar e testar a aplicação de e-commerce, além de passos práticos de resolução para problemas comuns em tempo de execução, compilação e IDE.

### Sumário

- [1. Pré-requisitos](#1-pré-requisitos)
- [2. Configuração do Banco de Dados](#2-configuração-do-banco-de-dados)
- [3. Executando a Aplicação](#3-executando-a-aplicação)
  - [Opção A: Executando via Docker Compose (Recomendado)](#opção-a-executando-via-docker-compose-recomendado)
  - [Opção B: Executando Localmente com Maven](#opção-b-executando-localmente-com-maven)
  - [Testando Endpoints e Autenticação (curl)](#testando-endpoints-e-autenticação-curl)
- [4. Executando os Testes](#4-executando-os-testes)
- [5. Resolução de Problemas e Erros Comuns](#5-resolução-de-problemas-e-erros-comuns)
  - [Erro 1: Conexão Recusada no PostgreSQL](#erro-1-conexão-recusada-no-postgresql)
  - [Erro 2: Release 25 is Not Found in the System](#erro-2-release-25-is-not-found-in-the-system)
  - [Erro 3: Project Configuration Is Not Up-To-Date with pom.xml](#erro-3-project-configuration-is-not-up-to-date-with-pomxml-1)
  - [Erro 4: Porta 8080 Já em Uso](#erro-4-porta-8080-já-em-uso)
  - [Erro 5: Conflito de Beans / Classes Antigas em Target](#erro-5-conflito-de-beans--classes-antigas-em-target)
  - [Erro 6: Permissão Negada em ./mvnw](#erro-6-permissão-negada-em-mvnw)
  - [Erro 7: Falha de Checksum ou Histórico no Flyway](#erro-7-falha-de-checksum-ou-histórico-no-flyway)
  - [Erro 8: Arquivo de Índice do Git Menor que o Esperado](#erro-8-arquivo-de-índice-do-git-menor-que-o-esperado)
  - [Erro 9: Aviso de Null Type Safety no SecurityConfig](#erro-9-aviso-de-null-type-safety-no-securityconfig)
  - [Erro 10: 401 Unauthorized ou 403 Forbidden](#erro-10-401-unauthorized-ou-403-forbidden)

---

### 1. Pré-requisitos

Certifique-se de que seu ambiente possui:

- **Java 21 JDK** instalado: Verifique executando `java -version`.
- **PostgreSQL 16** instalado e ativo na porta `5432`.
- Git instalado.

---

### 2. Configuração do Banco de Dados

A aplicação conecta-se a um banco de dados PostgreSQL denominado `ecommerce_db`.

#### Passo 1: Criar o Banco de Dados

Via terminal do PostgreSQL:

```bash
psql -U postgres -c "CREATE DATABASE ecommerce_db;"
```

#### Passo 2: Executar Scripts Iniciais (Opcional / Manual)

Se o Flyway estiver ativo (`spring.flyway.enabled=true`), as migrações rodam automaticamente. Caso queira rodar manualmente:

```bash
psql -U postgres -d ecommerce_db -f 01_create_database.sql
psql -U postgres -d ecommerce_db -f 02_functions_triggers_views.sql
```

#### Passo 3: Configurar Credenciais e Senhas de Acesso

As senhas e configurações de banco de dados ficam armazenadas em um arquivo `.env` na raiz do projeto (ignorado pelo Git):

1. Copie o arquivo de exemplo:
   ```bash
   cp .env.example .env
   ```
2. Edite o `.env` para definir suas senhas seguras e chaves JWT:
   ```properties
   POSTGRES_DB=ecommerce_db
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=sua_senha_segura
   HOST_DB_PORT=5433
   DB_USERNAME=postgres
   DB_PASSWORD=sua_senha_segura
   JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
   JWT_EXPIRATION_MS=86400000
   ```

---

### 3. Executando a Aplicação

#### Opção A: Executando via Docker Compose (Recomendado)

Inicia a API Spring Boot e o banco PostgreSQL em containers com inicialização automática dos esquemas SQL:

```bash
docker compose up --build -d
```

- **URL Base da API**: `http://localhost:8080`
- **Porta do PostgreSQL no Host**: `localhost:5433` (mapeada para a porta interna `5432` do container para evitar conflito com instâncias nativas do PostgreSQL na máquina)
- **Visualizar Logs**: `docker compose logs -f api`
- **Parar Containers**: `docker compose down`
- **Resetar Banco de Dados**: `docker compose down -v`

#### Opção B: Executando Localmente com Maven

Sempre execute os comandos a partir da pasta `application/`:

```bash
cd application
./mvnw spring-boot:run
```

Após o boot, o servidor estará respondendo em:
`http://localhost:8080`

#### Testando Endpoints e Autenticação (curl)

##### 1. Cadastrar um Novo Usuário

Cadastrar conta de cliente (`ROLE_CLIENT`):

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "Password123!",
    "cpf": "12345678901",
    "role": "ROLE_CLIENT"
  }'
```

Cadastrar conta de administrador (`ROLE_ADMIN`):

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com",
    "password": "AdminPassword123!",
    "cpf": "98765432100",
    "role": "ROLE_ADMIN"
  }'
```

##### 2. Autenticar e Obter Token JWT

Envie as credenciais para `/api/auth/login`:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "AdminPassword123!"
  }'
```

A resposta retornará o token Bearer e detalhes do usuário:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "Admin User",
  "email": "admin@example.com",
  "role": "ROLE_ADMIN"
}
```

##### 3. Acessar Endpoints Públicos (Sem Token)

```bash
curl -X GET "http://localhost:8080/api/categories?page=0&size=10"
curl -X GET "http://localhost:8080/api/products?page=0&size=10"
```

##### 4. Acessar Endpoints Protegidos com Token Bearer

Envie o token no cabeçalho `Authorization`:

```bash
curl -X GET "http://localhost:8080/api/orders/user/1" \
  -H "Authorization: Bearer <SEU_TOKEN_JWT>"
```

##### 5. Acessar Endpoints Restritos a Administrador

Criar categorias ou produtos requer `ROLE_ADMIN`:

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_ADMIN>" \
  -d '{
    "name": "Eletrônicos",
    "description": "Artigos e dispositivos eletrônicos"
  }'
```

---

### 4. Executando os Testes

O projeto conta com testes unitários isolados para o domínio e para os serviços de aplicação (com Mockito):

#### Executar Todos os Testes:

```bash
cd application
./mvnw test
```

#### Executar uma Classe Específica de Teste:

```bash
cd application
./mvnw test -Dtest=OrderApplicationServiceTest
```

#### Executar Limpando Artefatos Anteriores:

```bash
cd application
./mvnw clean test
```

---

### 5. Resolução de Problemas e Erros Comuns

#### Erro 1: Conexão Recusada no PostgreSQL

**Sintoma**:

```text
org.postgresql.util.PSQLException: The connection attempt failed: Connection to localhost:5432 refused
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource'
```

**Causa**: O serviço local do PostgreSQL não está em execução ou a porta 5432 está inacessível.
**Solução Mais Fácil**:

1. Verifique o status do serviço:
   ```bash
   sudo systemctl status postgresql
   ```
2. Inicie o serviço:
   ```bash
   sudo systemctl start postgresql
   ```
3. Alternativamente, suba uma instância via Docker:
   ```bash
   docker run -d --name ecommerce-postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=ecommerce_db postgres:16
   ```

---

#### Erro 2: Release 25 is Not Found in the System

**Sintoma**:

```text
The project was not built due to "release 25 is not found in the system". Fix the problem, then try refreshing this project.
```

**Causa**: O projeto continha versão 25 originalmente, mas a máquina possui Java 21 instalado. O Language Server da IDE manteve o cache antigo.
**Solução Mais Fácil**:

1. Verifique se o `pom.xml` possui `<java.version>21</java.version>`.
2. No VS Code:
   - Pressione `Ctrl + Shift + P`.
   - Digite **`Java: Clean Java Language Server Workspace`** e clique em **Restart and Delete**.
3. No IntelliJ IDEA:
   - Abra a aba Maven à direita e clique no botão de recarregar (ícone de setas circulares).

---

#### Erro 3: Project Configuration Is Not Up-To-Date with pom.xml

**Sintoma**:
Aparece uma flag na linha 1 do `pom.xml` indicando `Project configuration is not up-to-date with pom.xml, requires an update`.
**Causa**: O arquivo `pom.xml` foi editado externamente e o classpath interno do VS Code precisa ser sincronizado.
**Solução Mais Fácil**:

1. Clique na linha 1 do `pom.xml`.
2. Pressione `Ctrl + .` (ou clique no ícone de lâmpada).
3. Selecione **Update project configuration**.

---

#### Erro 4: Porta 8080 Já em Uso

**Sintoma**:

```text
Web server failed to start. Port 8080 was already in use.
```

**Causa**: Outra instância do backend ou outro programa está executando na porta 8080.
**Solução Mais Fácil**:

1. Encerre o processo ativo na porta 8080:
   ```bash
   fuser -k 8080/tcp
   ```
2. Ou mude a porta em `application/src/main/resources/application.properties`:
   ```properties
   server.port=8081
   ```

---

#### Erro 5: Conflito de Beans / Classes Antigas em Target

**Sintoma**:

```text
ConflictingBeanDefinitionException: Annotation-specified bean name 'categoryController' for bean class [...] conflicts with existing, non-compatible bean definition
```

**Causa**: Classes foram renomeadas ou movidas para novos pacotes, mas arquivos `.class` antigos continuam na pasta `target/classes`.
**Solução Mais Fácil**:
Limpe os binários antigos compilando do zero:

```bash
cd application
./mvnw clean compile
```

---

#### Erro 6: Permissão Negada em ./mvnw

**Sintoma**:

```bash
bash: ./mvnw: Permission denied
```

**Causa**: O script do wrapper Maven perdeu permissão de execução.
**Solução Mais Fácil**:
Restaure a permissão:

```bash
chmod +x application/mvnw
```

---

#### Erro 7: Falha de Checksum ou Histórico no Flyway

**Sintoma**:

```text
FlywayValidateException: Validate failed: Migrations have failed validation
```

**Causa**: Um script de migração foi alterado após já ter sido aplicado no banco de dados.
**Solução Mais Fácil**:

1. Em ambiente de desenvolvimento, repare a tabela do Flyway:
   ```bash
   cd application
   ./mvnw flyway:repair
   ```
2. Ou recrie o banco de dados de desenvolvimento:
   ```bash
   psql -U postgres -c "DROP DATABASE ecommerce_db;"
   psql -U postgres -c "CREATE DATABASE ecommerce_db;"
   ```

---

#### Erro 8: Arquivo de Índice do Git Menor que o Esperado

**Sintoma**:

```text
fatal: .git/index: index file smaller than expected
```

O VS Code exibe mensagens repetitivas de erro de status do Git ao alterar arquivos (`git status -z -uall`).

**Causa**: O arquivo de índice de staging `.git/index` foi corrompido ou truncado para 0 bytes, geralmente decorrente de reinício abrupto do sistema, encerramento forçado da IDE ou cancelamento de processos concorrentes do Git durante a escrita.
**Solução Mais Fácil**:

1. Exclua o arquivo de índice corrompido e reconstrua-o a partir do HEAD:
   ```bash
   rm -f .git/index
   git reset
   ```
2. No VS Code, recarregue a janela para limpar o cache da extensão do Git:
   - Pressione `Ctrl + Shift + P`.
   - Execute **`Developer: Reload Window`**.

---

#### Erro 9: Aviso de Null Type Safety no SecurityConfig

**Sintoma**:

```text
Null type safety: parameter 'this' provided via method descriptor Customizer<CsrfConfigurer<HttpSecurity>>.customize(CsrfConfigurer<HttpSecurity>) needs unchecked conversion to conform to '@NonNull AbstractHttpConfigurer<CsrfConfigurer<HttpSecurity>,HttpSecurity>'
```

**Causa**: A análise estrita de tipos nulos do Eclipse JDT / Java Language Server sinaliza referências de método como `AbstractHttpConfigurer::disable` porque o bytecode de bibliotecas de terceiros carece de anotações `@NonNull` explícitas em parâmetros de receptores genéricos.
**Solução Mais Fácil**:
Substitua as referências de método por expressões lambda explícitas em `SecurityConfig.java`:

```java
// Em vez de:
.csrf(AbstractHttpConfigurer::disable)
.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

// Utilize:
.csrf(csrf -> csrf.disable())
.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

---

#### Erro 10: 401 Unauthorized ou 403 Forbidden

**Sintoma**:

HTTP 401:

```json
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Full authentication is required to access this resource"
}
```

ou HTTP 403:

```json
{
  "type": "about:blank",
  "title": "Forbidden",
  "status": 403,
  "detail": "Access Denied"
}
```

**Causa**:

- **401 Unauthorized**: Cabeçalho `Authorization: Bearer <token>` ausente, inválido ou expirado.
- **403 Forbidden**: O perfil autenticado do usuário não tem permissão para acessar a rota (por exemplo, um usuário `ROLE_CLIENT` tentando acessar uma operação administrativa como `POST /api/categories` ou `PUT /api/orders/{id}/status`).

**Solução Mais Fácil**:

1. Autentique-se via `POST /api/auth/login` para receber um novo token JWT válido.
2. Certifique-se de que o cabeçalho esteja no formato `Authorization: Bearer <token>`.
3. Consulte a tabela de controle de acesso no README.md (seção Endpoints e Controle de Acesso) e autentique-se com um usuário que possua `ROLE_ADMIN` para rotas administrativas.
