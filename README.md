<h1 align="center">
  <img alt="banner" title="banner" src="banner.png" />
</h1>

# :computer: carteira-api
API REST com Spring Boot para um sistema de registro de transações no mercado financeiro.

Este projeto está sendo desenvolvido ao longo do Bootcamp Java Alura 2021.

### :bookmark_tabs: Tabela de conteúdos
* [Status do projeto](#status)
* [Tecnologias](#tecnologias)
* [Features](#features)
* [Pré-requisitos](#requisitos)
* [Executando a aplicação](#executando)
* [Testando a aplicação](#testando)
* [Licença](#licenca)

<a name="status"/></a>
### :hourglass: Status do projeto
Em desenvolvimento.

<a name="tecnologias"/></a>
### :hammer_and_wrench: Tecnologias

As seguintes ferramentas são utilizadas no desenvolvimento deste projeto:

- [Java](https://www.oracle.com/java/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
- [Spring Boot Starter Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation)
- [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
- [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test)
- [Spring Boot DevTools](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools)
- [Flyway](https://flywaydb.org/)
- [Lombok](https://projectlombok.org/)
- [ModelMapper](http://modelmapper.org/)
- [Maven](https://maven.apache.org/)
- [MySQL](https://www.mysql.com/)
- [Git](https://git-scm.com/)
- [Docker](http://modelmapper.org/)

<a name="features"/></a>
### :page_with_curl: Features
- [x] Cadastro de transações
- [x] Cadastro de usuários
- [x] Listagem de transações
- [x] Listagem de usuários
- [x] Relatório de itens da carteira de investimentos 

<a name="requisitos"/></a>
### :pencil: Pré-requisitos

Antes de começar, você precisa ter instalado em sua máquina as seguintes ferramentas:
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)

Além disso, você deve utilizar uma ferramenta para testar a API, como, por exemplo, o [Postman](https://www.postman.com/).

<a name="executando"/></a>
### :rocket: Executando a aplicação

```bash
# Clone este repositório
$ git clone https://github.com/antonioeloy/carteira-api.git

# Execute o container da aplicação
$ docker-compose up

# A aplicação iniciará na porta 8080
```

<a name="testando"/></a>
### :gear: Testando a aplicação

- <strong>GET localhost:8080/transacoes</strong> --> retorna a lista de transações (com paginação).
```json
{
    "content": [
        {
            "id": 1,
            "ticker": "CVCB3",
            "preco": 40.50,
            "quantidade": 120,
            "tipo": "COMPRA"
        },
        {
            "id": 2,
            "ticker": "ITUB4",
            "preco": 29.50,
            "quantidade": 80,
            "tipo": "COMPRA"
        },
        {
            "id": 3,
            "ticker": "BBDC4",
            "preco": 19.50,
            "quantidade": 50,
            "tipo": "COMPRA"
        }
    ],
    "pageable": {
        "sort": {
            "unsorted": true,
            "sorted": false,
            "empty": true
        },
        "offset": 0,
        "pageSize": 5,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 3,
    "size": 5,
    "number": 0,
    "sort": {
        "unsorted": true,
        "sorted": false,
        "empty": true
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
}
```

- <strong>POST localhost:8080/transacoes</strong> --> cadastra uma nova transação.
```json
{
    "ticker": "ITUB4",
    "preco": 29.67,
    "quantidade": 200,
    "data": "02/10/2021",
    "tipo": "VENDA",
    "usuarioId": 1
}
```

- <strong>GET localhost:8080/usuarios</strong> --> retorna a lista de usuários (com paginação).
```json
{
    "content": [
        {
            "id": 1,
            "nome": "Antonio Eloy",
            "login": "antonio.eloy"
        }
    ],
    "pageable": {
        "sort": {
            "unsorted": true,
            "sorted": false,
            "empty": true
        },
        "offset": 0,
        "pageSize": 5,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 5,
    "number": 0,
    "sort": {
        "unsorted": true,
        "sorted": false,
        "empty": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
}
```

- <strong>POST localhost:8080/usuarios</strong> --> cadastra um novo usuário.
```json
{
    "nome": "Antonio Eloy",
    "login": "antonio.eloy"
}
```

- <strong>GET localhost:8080/relatorios/carteira</strong> --> retorna o relatório dos itens da carteira de investimentos.
```json
[
    {
        "ticker": "CVCB3",
        "quantidade": 120,
        "percentual": 0.48
    },
    {
        "ticker": "ITUB4",
        "quantidade": 80,
        "percentual": 0.32
    },
    {
        "ticker": "BBDC4",
        "quantidade": 50,
        "percentual": 0.2
    }
]
```

<a name="licenca"/></a>
### :copyright: Licença

Este projeto está licenciado nos termos da licença MIT.





