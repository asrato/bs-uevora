//
// Created by André Rato & Afonso Alves on 21/12/19.
//

#ifndef UNTITLED7_2048_H
#define UNTITLED7_2048_H

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int random_de_array(int *numeros, int numero_elementos);

int possivel_colocar(int tamanho, int grelha[tamanho][tamanho]);

void colocar_numero(int tamanho, int grelha[tamanho][tamanho]);

int possivel_jogar(int tamanho, int grelha[tamanho][tamanho]);

void inicializar(int tamanho, int grelha[tamanho][tamanho], int n_elementos);

void mostrar(int tamanho, int grelha[tamanho][tamanho]);

void contagem_pecas(int tamanho, int grelha[tamanho][tamanho]);

int direita(int tamanho, int grelha[tamanho][tamanho]);

int esquerda(int tamanho, int grelha[tamanho][tamanho]);

int cima(int tamanho, int grelha[tamanho][tamanho]);

int baixo(int tamanho, int grelha[tamanho][tamanho]);

char jogada();

int numero();

#endif
