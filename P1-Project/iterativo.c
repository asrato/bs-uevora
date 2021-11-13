//
// Created by André Rato & Afonso Alves on 21/12/19.
//

#include "2048.h"

void main() {
    int tamanho, mov = 0;
    int pontos = 0;
    char resp;
    tamanho = numero();
    int grelha[tamanho][tamanho];
    inicializar(tamanho, grelha, 2);
    mostrar(tamanho, grelha);
    resp = jogada();
    while (possivel_jogar(tamanho, grelha) == 1 && resp != 'F') {
        if (resp == 'D') {
            mov = direita(tamanho, grelha);
            colocar_numero(tamanho, grelha);
            pontos = pontos + mov;
        } else if (resp == 'E') {
            mov = esquerda(tamanho, grelha);
            colocar_numero(tamanho, grelha);
            pontos = pontos + mov;
        } else if (resp == 'C') {
            mov = cima(tamanho, grelha);
            colocar_numero(tamanho, grelha);
            pontos = pontos + mov;
        } else if (resp == 'B') {
            mov = baixo(tamanho, grelha);
            colocar_numero(tamanho, grelha);
            pontos = pontos + mov;
        }
        mostrar(tamanho, grelha);
        if (possivel_jogar(tamanho, grelha) == 1) {
            resp = jogada();
        }
    }

    printf("Número de combinações: %d\n", pontos);
    contagem_pecas(tamanho, grelha);
}
