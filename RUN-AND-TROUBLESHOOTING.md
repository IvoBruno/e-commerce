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
- [4. Running the Tests](#4-running-the-tests)
- [5. Troubleshooting and Common Errors](#5-troubleshooting-and-common-errors)
  - [Error 1: PostgreSQL Connection Refused](#error-1-postgresql-connection-refused)
  - [Error 2: Release 25 is Not Found in the System](#error-2-release-25-is-not-found-in-the-system)
  - [Error 3: Project Configuration Is Not Up-To-Date with pom.xml](#error-3-project-configuration-is-not-up-to-date-with-pomxml)
  - [Error 4: Port 8080 Was Already in Use](#error-4-port-8080-was-already-in-use)
  - [Error 5: Conflicting Bean Definitions / Stale Target Classes](#error-5-conflicting-bean-definitions--stale-target-classes)
  - [Error 6: Permission Denied for ./mvnw](#error-6-permission-denied-for-mvnw)
  - [Error 7: Flyway Schema History or Checksum Mismatch](#error-7-flyway-schema-history-or-checksum-mismatch)

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

#### Step 3: Configure Database Credentials

Credentials default to `postgres / postgres`. To override them without changing source code, export environment variables:

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

---

### 3. Running the Application

Always run commands from the `application/` directory:

```bash
cd application
./mvnw spring-boot:run
```

Once started, the server will be available at:
`http://localhost:8080`

Test an endpoint using curl:

```bash
curl http://localhost:8080/api/categories
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

<a name="português-brasil"></a>

## Português (Brasil)

Este guia fornece instruções detalhadas para compilar, executar e testar a aplicação de e-commerce, além de passos práticos de resolução para problemas comuns em tempo de execução, compilação e IDE.

### Sumário

- [1. Pré-requisitos](#1-pré-requisitos)
- [2. Configuração do Banco de Dados](#2-configuração-do-banco-de-dados)
- [3. Executando a Aplicação](#3-executando-a-aplicação)
- [4. Executando os Testes](#4-executando-os-testes)
- [5. Resolução de Problemas e Erros Comuns](#5-resolução-de-problemas-e-erros-comuns)
  - [Erro 1: Conexão Recusada no PostgreSQL](#erro-1-conexão-recusada-no-postgresql)
  - [Erro 2: Release 25 is Not Found in the System](#erro-2-release-25-is-not-found-in-the-system)
  - [Erro 3: Project Configuration Is Not Up-To-Date with pom.xml](#erro-3-project-configuration-is-not-up-to-date-with-pomxml-1)
  - [Erro 4: Porta 8080 Já em Uso](#erro-4-porta-8080-já-em-uso)
  - [Erro 5: Conflito de Beans / Classes Antigas em Target](#erro-5-conflito-de-beans--classes-antigas-em-target)
  - [Erro 6: Permissão Negada em ./mvnw](#erro-6-permissão-negada-em-mvnw)
  - [Erro 7: Falha de Checksum ou Histórico no Flyway](#erro-7-falha-de-checksum-ou-histórico-no-flyway)

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

#### Passo 3: Configurar Credenciais de Acesso

O padrão definido é `postgres / postgres`. Para alterar via terminal sem modificar código:

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
```

---

### 3. Executando a Aplicação

Sempre execute os comandos a partir da pasta `application/`:

```bash
cd application
./mvnw spring-boot:run
```

Após o boot, o servidor estará respondendo em:
`http://localhost:8080`

Para testar um endpoint via terminal:

```bash
curl http://localhost:8080/api/categories
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
