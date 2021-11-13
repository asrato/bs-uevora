//
// Created by André Rato & Afonso Alves on 23/12/19.
//

#include "2048.h"
#include <string.h>

int main() {
    FILE *ficheiro;
    char result;
    int tamanho;

    ficheiro = fopen("jogadas.txt", "r");
    if (ficheiro == NULL) {
        printf("Ficheiro inválido\n");
        return 1;
    }
    printf("Ficheiro aberto com sucesso\n");

    result = getc(ficheiro);
    tamanho = atoi(&result);
    printf("Tamanho do tabuleiro: %d\n", tamanho);

    int grelha[tamanho][tamanho];
    inicializar(tamanho, grelha, 0);

    int numero;
    result = getc(ficheiro);
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            while (result != '2' && result != '4') {
                result = getc(ficheiro);
            }
            numero = atoi(&result);
            grelha[i][j] = numero;
            result = getc(ficheiro);
        }
    }

    int mov = 0, pontos = 0;
    result = getc(ficheiro);
    while (!feof(ficheiro) && possivel_jogar(tamanho,grelha)==1) {
        if (result == 'D') {
            mov = direita(tamanho, grelha);
            pontos = pontos + mov;
        } else if (result == 'E') {
            mov = esquerda(tamanho, grelha);
            pontos = pontos + mov;
        } else if (result == 'C') {
            mov = cima(tamanho, grelha);
            pontos = pontos + mov;
        } else if (result == 'B') {
            mov = baixo(tamanho, grelha);
            pontos = pontos + mov;
        }
        result = getc(ficheiro);
    }

    printf("Número de combinações: %d\n", pontos);
    contagem_pecas(tamanho, grelha);

    fclose(ficheiro);
    return 0;
}
