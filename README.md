# Desafio de Ganho de Capital em Java

Este é um projeto Java desenvolvido como parte de um desafio para calcular impostos sobre operações de compra e venda de ações.

## Decisões Técnicas e Arquiteturais

O projeto foi desenvolvido seguindo uma abordagem simples de aplicativo Java, sem a utilização de frameworks ou bibliotecas externas além das fornecidas pelo Java padrão. Optou-se por essa abordagem para manter o projeto o mais simples possível e focar nos conceitos fundamentais de programação Java.

A arquitetura segue uma estrutura básica de separação de responsabilidades, onde a lógica de negócios é implementada na classe `CalculadoraImpostoAcoes`, enquanto a entrada e saída são tratadas no método `main`. Além disso, foi utilizado um modelo de dados simples com a classe `OperacaoAcoes` para representar operações de compra e venda de ações.

## Justificativa para o Uso de Frameworks ou Bibliotecas

Nenhuma biblioteca externa foi utilizada neste projeto, uma vez que os requisitos do desafio podem ser facilmente atendidos utilizando apenas as bibliotecas padrão do Java. Isso também simplifica o processo de compilação e execução do projeto.

## Compilação e Execução do Projeto

Para compilar o projeto, você pode utilizar o Maven. Certifique-se de ter o Maven instalado e configurado em sua máquina. Depois, execute o seguinte comando na raiz do projeto:

```bash
mvn clean compile
```


Isso irá compilar o código-fonte do projeto.

Para executar o projeto, você pode usar o comando abaixo:

```bash
mvn exec:java -Dexec.mainClass="code.challenge.CalculadoraImpostoAcoes"
```

Isso executará o programa, que solicitará a entrada do usuário no console e imprimirá a saída correspondente.

## Execução dos Testes da Solução

Os testes podem ser executados com o seguinte comando:

```bash
mvn test
```

Isso executará todos os testes unitários disponíveis no projeto e exibirá os resultados no console.