#include <stdio.h>
#include <stdlib.h>

// Pilha de operadores da calculadora
struct stack {
    int array[10];                      // array que guarda os valores da stack
    int sp;                             // stack pointer - variavel que guarda a posicao do ultimo elemento presente na stack (topo)
};

// Funções de acesso direto à stack
// Colocar na stack
void push(struct stack *stack, int number) {
    if (stack->sp < 10) {
        stack->array[stack->sp] = number;                   // coloca o valor de number na posicao sp do array se sp<10
        stack->sp++;                                    // avanca uma posicao no array de modo a estar pronto para receber mais um valor na pilha
    } else {
        printf("Invalid input. Out of bounds. Type 'help' for available commands\n");
    }

}

// Retirar da stack (topo)
int pop(struct stack *stack) {
    stack->sp--;                                                // retrocede uma posicao no array de modo a avaliar o topo da stack
    return stack->array[stack->sp];                             // retorna o valor do topo da stack
}

// Print da stack
void print(struct stack *stack) {
    int number_of_elements = stack->sp;
    int index = 0;
    printf("Stack:\n");
    if (number_of_elements == 0) {
        printf("    (empty)\n");                   // se number_of_elements == 0 a stack esta vazia
    }
    while (index < number_of_elements) {
        printf("    %d\n",
               stack->array[index]);                      // da print do valor presente no array na posicao index
        index++;
    }

}

// Funções de acesso não direto à stack
// Função SOMA "+"
void some(struct stack *stack) {
    if(stack->sp>1) {                                           // numero minimo de operandos na pilha = 2
        int arg1 = pop(stack);                                      // retira o elemento do topo da stack e guarda-o em arg1
        int arg2 = pop(stack);                                      // retira o elemento do topo da stack e guarda-o em arg2
        push(stack, arg2 + arg1);                            // coloca o valor de arg2+arg1 no topo da stack
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função SUBTRAÇÃO "-"
void subtraction(struct stack *stack) {
    if(stack->sp>1) {                                          // numero minimo de operandos na pilha = 2
        int arg1 = pop(stack);                                  // retira o elemento do topo da stack e guarda-o em arg1
        int arg2 = pop(stack);                                  // retira o elemento do topo da stack e guarda-o em arg2
        push(stack, arg2 - arg1);                        // coloca o valor de arg2-arg1 no topo da stack
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função PRODUTO "*"
void product(struct stack *stack) {
    if(stack->sp>1) {                                   // numero minimo de operandos na pilha = 2
        int arg1 = pop(stack);                                             // retira o elemento do topo da stack e guarda-o em arg1
        int arg2 = pop(stack);                                             // retira o elemento do topo da stack e guarda-o em arg2
        push(stack, arg2 * arg1);                                   // coloca o valor de arg2*arg1 no topo da stack
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função DIVISÃO "/"
void quotient(struct stack *stack) {
    if(stack->sp>1) {                                       // numero minimo de operandos na pilha = 2                                    
        int arg1 = pop(
                stack);                                            // retira o elemento do topo da stack e guarda-o em agr1
        int arg2 = pop(
                stack);                                            // retira o elemento do topo da stack e guarda-o em agr2
        if (arg1 !=
            0) {                                                   // se o arg1 (divisor) != 0 coloca o valor de arg2/arg1 no topo da stack
            push(stack, arg2 / arg1);
    } else {
            printf("Error! Division by 0 it's impossible!\n");           // se arg1 == 0 apresenta uma mensagem de erro e volta a colocar os dois argumentos (arg1
            push(stack, arg2);                                             // e arg2) de volta no topo da stack, pela ordem em que se encontravam
            push(stack, arg1);
        }
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função NEGATIVO "neg"
void neg(struct stack *stack) {
    if(stack->sp>0) {                       // numero minimo de operandos na pilha = 1
        int arg = pop(stack);                         // retira o elemento do topo da stack e guarda-o em arg
        push(stack, -arg);                             // coloca o simetrico do valor do arg no topo da stack
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função TROCA "swap"
void swap(struct stack *stack) {
    if(stack->sp>1) {                           // numero minimo de operandos na pilha = 2
        int arg1 = pop(stack);                                                // retira o elemento do topo da stack e guarda-o em agr1
        int arg2 = pop(stack);                                                // retira o elemento do topo da stack e guarda-o em agr2
        push(stack, arg1);                                                     // coloca o valor do arg1 no topo da stack
        push(stack, arg2);                                                     // coloca o valor do arg2 no topo da stack
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função LIMPAR "clear"
void clear(struct stack *stack) {
    stack->sp = 0;                             // coloca o sp (stack pointer) a 0, tornando a stack com um numero de elementos = 0
}

// Função CLONE "dup"
void dup(struct stack *stack) {
    if(stack->sp>0) {                               // numero minimo de operandos na pilha = 1
        int arg = pop(stack);                               // retira o elemento do topo da stack e guarda-o na variavel arg
        push(stack, arg);                                    // adiciona o valor da variavel arg a stack
        push(stack, arg);                                    // volta a adicionar o mesmo valor a stack
    } else {
        printf("Not enought values in the stack\n");
    }
}

// Função REMOVER TOPO "drop"
void drop(struct stack *stack) {
    if (stack->sp > 0) {                    // numero minimo de operandos na pilha = 1
        int arg = pop(stack);         // retira o elemento do topo da stack e guarda-o numa variavel que nunca e usada
    } else {
        printf("Invalid input. Out of bounds. Type 'help' for available commands\n");
    }
}

// Função PROCESSAR INPUT "process_input"
int process_input(char input[32], struct stack *stack) {        // leitura e chamada das funcoes das operacoes
    int number = 0, run = 1, aux = 0;
    for (int i = 0; run; i++) {
        if (input[i] > 47 &&
            input[i] < 58) {                               // conversao dos caracteres numericos para numeros inteiros
            number = number * 10 + (input[i] - 48);
        aux = 1;
        } else if (input[i] == ' ') {                                           // separacao dos operandos e operadores
            if (aux == 1) {
                push(stack, number);
            }
            number = 0;
            aux = 0;
        } else if (input[i] ==
                   0) {                                             // se caractere == 0, run = 0 (detecao do final da input)
            if (aux == 1) {
                push(stack, number);
            }
            run = 0;
        } else if (input[i] == '+') {                                              // se caractere == '+' executa a funcao some
        aux = 0;
        some(stack);
    } else if (input[i] ==
                   '-') {                                           // se caractere == '-' executa a funcao subtraction
    aux = 0;
    subtraction(stack);
} else if (input[i] ==
                   '*') {                                            // se caractere == '*' executa a funcao product
aux = 0;
product(stack);
} else if (input[i] ==
                   '/') {                                                   // se caractere == '/' executa a funcao quotient
aux = 0;
quotient(stack);
} else if (input[i] == 'n' && input[i + 1] == 'e' &&
                   input[i + 2] == 'g') {           // se a sequencia de caracteres == "neg" executa a funcao neg
neg(stack);
i+=2;
} else if (input[i] == 's' && input[i + 1] == 'w' && input[i + 2] == 'a' && input[i + 3] ==
                                                                                    'p') {                    // se a sequencia de caracteres == "swap" executa a funcao swap
aux = 0;
swap(stack);
i+=3;
} else if (input[i] == 'c' && input[i + 1] == 'l' && input[i + 2] == 'e' && input[i + 3] == 'a' &&
                   input[i + 4] == 'r') {          // se a sequencua de caracteres == "clear" executa a funcao clear
aux = 0;
clear(stack);
i+=4;
} else if (input[i] == 'o' && input[i + 1] == 'f' && input[i + 2] ==
                                                             'f') {                       // se a sequencia de caracteres == "off" a funcao retorna 1
return 1;
} else if (input[i] == 'd' && input[i + 1] == 'u' && input[i + 2] ==
                                                             'p') {                      // se a sequencia de caracteres == "dup" executa a funcao dup
aux = 0;
dup(stack);
i+=2;
} else if (input[i] == 'd' && input[i + 1] == 'r' && input[i + 2] == 'o' &&
                   input[i + 3] == 'p') {           // se a sequencia de caracteres == "drop" executa a funcao drop
drop(stack);
i+=3;
} else if (input[i] == 'h' && input[i + 1] == 'e' && input[i + 2] == 'l' && input[i + 3] ==
                                                                                    'p') {                 // se a sequencia de caracteres == "help" da print a todos os comandos disponiveis
printf("\n     + | Adicao (operador binario)\n");
printf("     - | Subtracao (operador binario)\n");
printf("     * | Multiplicacao (operador binario)\n");
printf("     / | Divisao (operador binario)\n");
printf("   neg | Calcula o simetrico (operador unario\n");
printf("  swap | Troca a posicao dos dois operandos do topo da pilha\n");
printf("   dup | Duplica (clone) o operando do topo da stack\n");
printf("  drop | Elimina o operando do topo da stack\n");
printf(" clear | Limpa toda a stack\n");
printf("   off | Desliga a calculadora\n");
printf("\n");
i+=3;
} else {
    printf("Unkown command. Type 'help' for available commands\n");
    break;
}
}
print(stack);
return 0;
}

void main() {
    char input[32];                  // array de caracteres na qual a input do utilizador é armazenada
    int off;                                                // variavel de controlo para encerrar a calculadora
    struct stack *stack = malloc(
            sizeof(struct stack));                        // declaracao e alocagem da memoria da struct stack
    stack->sp = 0;                                      // declaracao e inicializaçcao do stack pointer da struct
    printf("**************************************************\n");
    printf("**** RPN - Reverse Polish Notation Calculator ****\n");
    printf("** Andre Rato (45517) && Jose Alexandre (45223) **\n");
    printf("**** Arquitetura de Sistemas e Computadores 1 ****\n");
    printf("**************************************************\n");
    printf("Type 'help' for available commands\n");
    print(stack);
    printf("->");
    scanf("%[^\n]", input);                              // rececao da input
    while (1) {                                      // enquanto nao houver ordem contraria continua a execucao da calculadora
        off = process_input(input, stack);
        if (off) {
            break;                              // interrupção do ciclo while
        }
        printf("->");
        scanf(" %[^\n]", input);
    }
    printf("Bye!");
    free(stack);                              // liberta o espaço da memória que foi alocado para "stack"
}