# Desafio Luizalabs

## Descrição
Integração de um sistema legado através de um arquivo de pedidos desnormalizado.

## Tabela de Conteúdos
1. [Instalação](#instalação)
2. [Uso](#uso)
3. [Exemplos](#exemplos)
4. [Características](#características)
5. [Testes](#testes)
6. [Status do Projeto](#status)

## Instalação
Instruções para execução
```bash
./gradlew bootRun
```

## Uso
A integração via arquivo de pedidos é realizada através de uma chamada API, exemplo:
```bash
curl --request POST \
    --url http://localhost:8080/v1/orders/upload \
    --header 'content-type: multipart/form-data' \
    --form file=
```

Após conclusão do processo é possível realizar chamadas à API para consultar dados, exemplo:

Todos os pedidos:
```bash
curl --request GET \
    --url http://localhost:8080/v1/orders
```
Por ID de pedido:
```bash
curl --request GET \
    --url 'http://localhost:8080/v1/orders?startDate=2020-03-01&endDate=2023-12-31'
```
## Exemplos

Consulta por id de pedido
```
{
    "user_id": 1,
    "name": "Sammie Baumbach",
    "orders": [
        {
            "order_id": 4,
            "total": 3575.24,
            "date": "2021-06-21",
            "products": [
                {
                    "product_id": 3,
                    "value": 1596.36
                },
                {
                    "product_id": 4,
                    "value": 297.09
                },
                {
                    "product_id": 4,
                    "value": 1681.79
                }
            ]
        }
    ]
}
```

## Características
Nste projeto foi utilizado o framework reativo WebFlux baseado no projeto Reactor, biblioteca de programação reativa.

Pensando na simplicidade, o armazenamento dos dados foi feito com H2, um banco de dados escrito em Java e o conector R2DBC, que fornece uma API não-bloqueante totalmente reativa para bancos relacionais.

## Testes
Instruções para execução dos testes
```bash
./gradlew test
```

## Status do Projeto
Do ponto de vista técnico a aplicação está totalmente funcional e performática.
Dentro do contexto reativo ainda é possível explorar ajustes na manipulação de listas e controle da bufferização para arquivos maiores.
Tratamento de erros e validações adicionais também podem garantir uma experiência ainda mais confiável ao serviço.
