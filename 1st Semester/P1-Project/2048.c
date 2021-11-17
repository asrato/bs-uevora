//
// Created by André Rato & Afonso Alves on 21/12/19.
//

#include "2048.h"

int random_de_array(int *numeros, int numero_elementos) {
    int pos = rand() % numero_elementos;
    return numeros[pos];
}

int possivel_colocar(int tamanho, int grelha[tamanho][tamanho]) {
    int possiveis = 0;
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            if (grelha[i][j] == 0) possiveis++;
        }
    }
    return possiveis;
}

void colocar_numero(int tamanho, int grelha[tamanho][tamanho]) {
    srand(time(NULL));
    if (possivel_colocar(tamanho, grelha) != 0) {
        int i, j;
        int numeros[2] = {2, 4};
        int array[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        i = random_de_array(array, tamanho);
        j = random_de_array(array, tamanho);
        while (grelha[i][j] != 0) {
            i = random_de_array(array, tamanho);
            j = random_de_array(array, tamanho);
        }
        grelha[i][j] = random_de_array(numeros, 2);
    }
}

int possivel_jogar(int tamanho, int grelha[tamanho][tamanho]) {
    int i, j;
    for (i = 0; i < tamanho; i++) {
        for (j = 0; j < tamanho; j++) {
            if (grelha[i][j] == 0 || (grelha[i][j] == grelha[i][j - 1] && j - 1 >= 0) ||
                (grelha[i][j] == grelha[i][j + 1] && j + 1 < tamanho - 1) ||
                (grelha[i][j] == grelha[i - 1][j] && i - 1 >= 0) ||
                (grelha[i][j] == grelha[i + 1][j] && i + 1 < tamanho - 1)) {
                return 1;
            }
        }
    }
    return 0;
}

void inicializar(int tamanho, int grelha[tamanho][tamanho], int n_elementos) {
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            grelha[i][j] = 0;
        }
    }
    for (int i = 0; i < n_elementos; i++) colocar_numero(tamanho, grelha);
}

void mostrar(int tamanho, int grelha[tamanho][tamanho]){
    printf("\n");
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            if (grelha[i][j] == 0) {
                char caracter = '-';
                printf("%8c", caracter);
            } else {
                printf("%8d", grelha[i][j]);
            }
        }
        printf("\n");
        printf("\n");
        printf("\n");

    }
}

void contagem_pecas(int tamanho, int grelha[tamanho][tamanho]){
    int dois = 0, quatro = 0, oito = 0, deza6 = 0, trintae2 = 0, sessentae4 = 0, centoe28 = 0, duzentose56 = 0, quinhentose12 = 0, mile24 = 0, doismile48 = 0;
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            if (grelha[i][j] == 2) {
                dois++;
            } else if (grelha[i][j] == 4) {
                quatro++;
            } else if (grelha[i][j] == 8) {
                oito++;
            } else if (grelha[i][j] == 16) {
                deza6++;
            } else if (grelha[i][j] == 32) {
                trintae2++;
            } else if (grelha[i][j] == 64) {
                sessentae4++;
            } else if (grelha[i][j] == 128) {
                centoe28++;
            } else if (grelha[i][j] == 256) {
                duzentose56++;
            } else if (grelha[i][j] == 512) {
                quinhentose12++;
            } else if (grelha[i][j] == 1024) {
                mile24++;
            } else if (grelha[i][j] == 2048) {
                doismile48++;
            }
        }
    }
    printf("Peças restantes no tabuleiro:\n");
    if (dois != 0)
        printf("- Peças 2: %d\n", dois);
    if (quatro != 0)
        printf("- Peças 4: %d\n", quatro);
    if (oito != 0)
        printf("- Peças 8: %d\n", oito);
    if (deza6 != 0)
        printf("- Peças 16: %d\n", deza6);
    if (trintae2 != 0)
        printf("- Peças 32: %d\n", trintae2);
    if (sessentae4 != 0)
        printf("- Peças 64: %d\n", sessentae4);
    if (centoe28 != 0)
        printf("- Peças 128: %d\n", centoe28);
    if (duzentose56 != 0)
        printf("- Peças 256: %d\n", duzentose56);
    if (quinhentose12 != 0)
        printf("- Peças 512: %d\n", quinhentose12);
    if (mile24 != 0)
        printf("- Peças 1024: %d\n", mile24);
    if (doismile48 != 0)
        printf("- Peças 2048: %d\n", doismile48);
}

int direita(int tamanho, int grelha[tamanho][tamanho]){
    int aux1 = 1, aux2 = 1, contador = 0;
    while (aux1 > 0) {
        aux1 = 0;
        for (int i = tamanho - 1; i >= 0; i--) {
            for (int j = tamanho - 1; j >= 0; j--) {
                if (grelha[i][j] == 0 && grelha[i][j - 1] != 0 && j - 1 >= 0) {
                    grelha[i][j] = grelha[i][j - 1];
                    grelha[i][j - 1] = 0;
                    aux1++;
                }
            }
        }
    }
    for (int i = tamanho - 1; i >= 0; i--) {
        for (int j = tamanho - 1; j >= 0; j--) {
            if (grelha[i][j] == grelha[i][j - 1] && grelha[i][j] != 0 && j - 1 >= 0) {
                grelha[i][j] = 2 * grelha[i][j];
                grelha[i][j - 1] = 0;
                contador++;
            }
        }
    }
    while (aux2 > 0) {
        aux2 = 0;
        for (int i = tamanho - 1; i >= 0; i--) {
            for (int j = tamanho - 1; j >= 0; j--) {
                if (grelha[i][j] == 0 && grelha[i][j - 1] != 0 && j - 1 >= 0) {
                    grelha[i][j] = grelha[i][j - 1];
                    grelha[i][j - 1] = 0;
                    aux2++;
                }
            }
        }
    }
    return contador;
}

int esquerda(int tamanho, int grelha[tamanho][tamanho]){
    int aux1 = 1, aux2 = 1, contador = 0;
    while (aux1 > 0) {
        aux1 = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (grelha[i][j] == 0 && grelha[i][j + 1] != 0 && j + 1 < tamanho) {
                    grelha[i][j] = grelha[i][j + 1];
                    grelha[i][j + 1] = 0;
                    aux1++;
                }
            }
        }
    }
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            if (grelha[i][j] == grelha[i][j + 1] && grelha[i][j] != 0 && j + 1 < tamanho) {
                grelha[i][j] = 2 * grelha[i][j];
                grelha[i][j + 1] = 0;
                contador++;
            }
        }
    }
    while (aux2 > 0) {
        aux2 = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (grelha[i][j] == 0 && grelha[i][j + 1] != 0 && j + 1 < tamanho) {
                    grelha[i][j] = grelha[i][j + 1];
                    grelha[i][j + 1] = 0;
                    aux2++;
                }
            }
        }
    }
    return contador;
}

int cima(int tamanho, int grelha[tamanho][tamanho]){
    int aux1 = 1, aux2 = 1, contador = 0;
    while (aux1 > 0) {
        aux1 = 0;
        for (int j = tamanho - 1; j >= 0; j--) {
            for (int i = 0; i < tamanho; i++) {
                if (grelha[i][j] == 0 && grelha[i + 1][j] != 0 && i + 1 < tamanho) {
                    grelha[i][j] = grelha[i + 1][j];
                    grelha[i + 1][j] = 0;
                    aux1++;
                }
            }
        }
    }
    for (int i = 0; i < tamanho; i++) {
        for (int j = 0; j < tamanho; j++) {
            if (grelha[i][j] == grelha[i + 1][j] && grelha[i][j] != 0 && i + 1 < tamanho) {
                grelha[i][j] = 2 * grelha[i][j];
                grelha[i + 1][j] = 0;
                contador++;
            }
        }
    }
    while (aux2 > 0) {
        aux2 = 0;
        for (int j = tamanho - 1; j >= 0; j--) {
            for (int i = 0; i < tamanho; i++) {
                if (grelha[i][j] == 0 && grelha[i + 1][j] != 0 && i + 1 < tamanho) {
                    grelha[i][j] = grelha[i + 1][j];
                    grelha[i + 1][j] = 0;
                    aux2++;
                }
            }
        }
    }
    return contador;
}

int baixo(int tamanho, int grelha[tamanho][tamanho]){
    int aux1 = 1, aux2 = 1, contador = 0;
    while (aux1 > 0) {
        aux1 = 0;
        for (int j = tamanho - 1; j >= 0; j--) {
            for (int i = tamanho - 1; i >= 0; i--) {
                if (grelha[i][j] == 0 && grelha[i - 1][j] != 0 && i - 1 >= 0) {
                    grelha[i][j] = grelha[i - 1][j];
                    grelha[i - 1][j] = 0;
                    aux1++;
                }
            }
        }
    }
    for (int i = tamanho - 1; i > 0; i--) {
        for (int j = 0; j < tamanho; j++) {
            if (grelha[i][j] == grelha[i - 1][j] && grelha[i][j] != 0 && i - 1 >= 0) {
                grelha[i][j] = 2 * grelha[i][j];
                grelha[i - 1][j] = 0;
                contador++;
            }
        }
    }
    while (aux2 > 0) {
        aux2 = 0;
        for (int j = tamanho - 1; j >= 0; j--) {
            for (int i = tamanho - 1; i >= 0; i--) {
                if (grelha[i][j] == 0 && grelha[i - 1][j] != 0 && i - 1 >= 0) {
                    grelha[i][j] = grelha[i - 1][j];
                    grelha[i - 1][j] = 0;
                    aux2++;
                }
            }
        }
    }
    return contador;
}

char jogada(){
    char resp;
    printf("\nJogada: ");
    scanf(" %c", &resp);
    while (resp != 'D' && resp != 'E' && resp != 'B' && resp != 'C' && resp != 'F') {
        printf("\nJogada invalida");
        printf("\nJogada: ");
        scanf(" %c", &resp);
    }
    return resp;
}

int numero(){
    int tamanho;
    printf("Tamanho do tabuleiro (entre 2 e 10): ");
    scanf("%d", &tamanho);
    while (tamanho < 2 || tamanho > 10) {
        printf("Tamanho inválido\n");
        printf("Tamanho do tabuleiro (entre 2 e 10): ");
        scanf("%d", &tamanho);
    }
    return tamanho;
}
