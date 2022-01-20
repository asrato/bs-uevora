# Trabalho Prático de Programação III
# 2021/2022

## Introdução
O StrimkoTM é um puzzle lógico com números. Para as regras deste jogo consulte por exemplo https://en.wikipedia.org/wiki/Strimko.

Pretende-se com este trabalho a implementação em Prolog de um resolvedor de puzzles Strimko.

## Implementação
De um modo geral, a implementação deve ler o puzzle a partir de um ficheiro, resolvê-lo e apresentar a solução. Para testar, deve existir um predicado go/1 que tem como argumento o nome do ficheiro com o puzzle.

## Ficheiro de Input
O ficheiro de input deve ter na primeira linha a dimensão do puzzle (2 ≤ n ≤ 9). Nas seguintes n linhas deve estar a representação das streams, indicando, para cada elemento da matriz n×n, a que stream pertence.

Nas linhas seguintes devem estar as posições pré-preenchidas do puzzle. Por exemplo, ao puzzle da figura 1 deve corresponder o seguinte ficheiro:
```
4
1 2 2 4
2 1 4 2
3 4 1 3
4 3 3 1
2 2 3
2 3 2
3 3 1
```

![image](https://user-images.githubusercontent.com/58145573/150316511-ed8d0cbc-0c68-4ef2-87f8-ae0a7053e732.png)
Figura 1: Exemplo de puzzle Strimko



## Output
Para o exemplo acima, depois de encontrada a solução, esta deverá ser apresentada como uma tabela, do seguinte modo:
```
4 2 3 1
1 3 2 4
2 4 1 3
3 1 4 2
```

## Elaboração

* O grupo de trabalho não pode ter mais do que 3 elementos
* Haverá discussão do trabalho em data a anunciar
* O relatório deverá conter a identificação dos elementos do grupo e uma descrição sucinta do funcionamento do trabalho para cada um dos itens do enunciado. Deve ser também indicado qual o interpretador de Prolog utilizado (SWI-Prolog ou GNU Prolog)
* O trabalho será classificado entre 0 e 20 valores e os factores de avaliação serão os seguintes:
   * clareza dos algoritmos implementados
   * correcção do código
   * legibilidade do código (tamanho das linhas, indentação e comentários)
   * qualidade do relatório
* O código tem de funcionar em SWI-Prolog e/ou GNU Prolog

## Entrega
O trabalho deve ser entregue via moodle até ao final do dia 16 de janeiro de 2022, pelo elemento do grupo com o número menor.

O formato de entrega será um ficheiro .zip ou .tar.gz que ao descomprimir crie uma directoria lxxx_lyyy_lzzz em que lxxx, lyyy e lzzz são os números dos elementos do grupo, por ordem crescente. Dentro dessa directoria deverão estar o código (ficheiro( strimko.pl) e o relatório (ficheiro .pdf)

## Anexo
![image](https://user-images.githubusercontent.com/58145573/150316456-e105439b-8bf3-4ec0-b938-9a02da9a738e.png)
