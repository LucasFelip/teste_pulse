# Teste Pulse - Sistema de Checkout e Faturamento

## Visão Geral

Este projeto implementa uma **API RESTful** para um sistema de checkout e faturamento, com as seguintes funcionalidades:

- Gerenciamento de **clientes**, **endereços**, **produtos** e **transportadoras**.
- Criação e manipulação de **carrinhos de compras** e **pedidos**.
- Geração de **notas fiscais simuladas**.
- Emissão de **relatórios de pedidos por cliente**.
- Documentação interativa via Swagger/OpenAPI.

A aplicação segue uma arquitetura em camadas (Controller, Service, Domain, Repository) e utiliza tecnologias como **Spring Boot**, **JPA/Hibernate**, **Flyway**, **ModelMapper**, **HATEOAS**, **JUnit 5** e **Mockito**.

> **_NOTA:_**  A documentação com explicação de decições arquiteturais e detalhes de implementação está disponível na pasta `docs` do projeto.
> - [Visualizar PDF](docs/documentacao_tecnica.pdf)
> - [Download do código LaTeX](docs/documentacao_tecnica.zip)

## Decisões Arquiteturais
- Adoção da arquitetura em camadas (Controller, Service, Domain, Repository)
- Uso do padrão DTO + Assembler para separar modelos de entrada e saída
- ModelMapper para mapear objetos entre camadas
- Flyway para versionamento de banco

## Pré-requisitos

- Java 17
- Maven 3.6
- PostgreSQL

## Configuração de Ambiente

Crie um arquivo `.env` na raiz do projeto com as variáveis abaixo:

```dotenv
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/teste_pulse
DB_USER=postgres
DB_PASSWORD=password
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
FLYWAY_ENABLED=true
````

## Executando Localmente

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/LucasFelip/teste_pulse.git
   cd teste_pulse
   ```

2. **Configure o arquivo `.env`** (conforme mostrado acima).

3. **Compile o projeto**:

   ```bash
   mvn clean package -DskipTests
   ```

4. **Execute a aplicação**:

   ```bash
   java -jar target/teste-pulse.jar
   ```

5. **Acesse a API**:

    * Swagger UI: `http://localhost:8080/swagger-ui-custom.html`
    * Endpoints base: `http://localhost:8080/v1`

## Executando com Docker

A imagem da aplicação está disponível no Docker Hub:
`luscaferreira/teste_pulse`

### Utilizando `docker-compose`

1. Certifique-se de que o `.env` está configurado corretamente.
2. Execute o comando:

   ```bash
   docker-compose up --build
   ```

Serviços disponíveis:

* `db`: banco PostgreSQL na porta 5432
* `api`: exposta em `localhost:8080` após o banco estar saudável

### Utilizando a imagem pronta

Link da imagem: [luscaferreira/teste_pulse](https://hub.docker.com/r/luscaferreira/teste_pulse)

```bash
  docker pull luscaferreira/teste_pulse:latest
  docker-compose up -d
```

## Documentação Swagger

A documentação interativa dos endpoints está disponível em:

```
http://localhost:8080/swagger-ui-custom.html
```

## Exemplos de Chamadas

* **Criar carrinho**:

  ```bash
  curl -X POST "http://localhost:8080/v1/carrinhos?clienteId=1"
  ```

* **Adicionar item ao carrinho**:

  ```bash
  curl -X POST "http://localhost:8080/v1/carrinhos/1/itens" \
       -H "Content-Type: application/json" \
       -d '{"produtoId":3,"quantidade":2}'
  ```

* **Efetuar checkout**:

  ```bash
  curl -X POST "http://localhost:8080/v1/checkout" \
       -H "Content-Type: application/json" \
       -d '{"carrinhoId":1,"enderecoId":2,"transportadoraId":3,"formaPagamento":"PIX"}'
  ```

## Executando os Testes

A suíte de testes utiliza **JUnit 5** e **Mockito**, rodando com perfil `test` e banco H2 em memória:
```bash
    mvn test -Ptest
```
