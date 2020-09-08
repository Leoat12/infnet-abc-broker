# ABC Broker

## Introdução

Simples "home broker" com gráfico de uma ação para trabalho de pós-graduação. 

## Utilização

### Requisitos

- Java 8+
- Node 10.13+

### Execução das aplicações

Para executar as aplicações em modo de desenvolvimento basta rodar os comandos abaixo em 
linhas de comando diferentes. 

#### API

Da pasta raiz do projeto:

```shell script
$ mvn clean package
$ java -jar .\target\abc-broker-0.0.1-SNAPSHOT.jar
```

A API estará rodando na porta 8080, curl para teste:

```shell script
curl http://localhost:8080/api/stock
```

#### UI

Da pasta raiz do projeto:

```shell script
$ cd ui/abc-broker-ui
$ npm install
$ npm run start
```

A UI poderá ser vista no endereço `http://localhost:4200`.
