#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define MEMORY_SIZE 200
#define QUEUE_SIZE 200
#define QUANTUM 3
#define IO 5

// Instruction Codes
#define ERR (-1)
#define ZER 0
#define CPY 1
#define DEC 2
#define FRK 3
#define JFW 4
#define JBK 5
#define DSK 6
#define JIZ 7
#define PRT 8
#define HLT 9

// Memory Management
int mem[MEMORY_SIZE];
int bit_vector[MEMORY_SIZE];
int free_space = MEMORY_SIZE;

int n_processes = 0;
int next_index = 0;
bool hasFork = false;

// Used to print the memory and the bit_vector arrays
void print_mem() {
    for (int i = 0; i < MEMORY_SIZE; i++) {
        printf("%d ", mem[i]);
    }
    printf("\n");
    for (int i = 0; i < MEMORY_SIZE; i++) {
        printf("%d ", bit_vector[i]);
    }
    printf("\n");
}

// Queues
int ready[QUEUE_SIZE];
int blocked[QUEUE_SIZE];

int rdysize = QUEUE_SIZE, rdyfront = -1, rdyrear = -1, rdyele = 0;
int blksize = QUEUE_SIZE, blkfront = -1, blkrear = -1, blkele = 0;

void enqueue(int value, int queue) {
    if (queue == 0) { //rdy
        if (rdyfront == -1) {
            rdyfront = rdyrear = 0;
            ready[rdyrear] = value;
            rdyele++;
        } else if (rdyrear == rdysize - 1 && rdyfront != 0) {
            rdyrear = 0;
            ready[rdyrear] = value;
            rdyele++;
        } else if (rdyrear == rdysize - 1 && rdyfront == 0) {

        } else {
            rdyrear++;
            ready[rdyrear] = value;
            rdyele++;
        }
    } else { //blk
        if (blkfront == -1) {
            blkfront = blkrear = 0;
            blocked[blkrear] = value;
            blkele++;
        } else if (blkrear == blksize - 1 && blkfront != 0) {
            blkrear = 0;
            blocked[blkrear] = value;
            blkele++;
        } else if (blkrear == blksize - 1 && blkfront == 0) {

        } else {
            blkrear++;
            blocked[blkrear] = value;
            blkele++;
        }
    }
}


int front(int queue) {
    if (queue == 0) { //rdy
        if (rdyele == 0) {
            return -1;
        }
        return ready[rdyfront];
    } else { //blk
        if (blkele == 0) {
            return -1;
        }
        return blocked[blkfront];
    }
}

int dequeue(int queue) {
    if (queue == 0) { //rdy
        if (rdyele == 0) {
            return -1;
        }
        int aux = ready[rdyfront];
        if (rdyfront == rdyrear) {
            rdyfront = -1;
            rdyrear = -1;
            rdyele = 0;
        } else if (rdyfront == rdysize - 1) {
            rdyfront = 0;
        } else {
            rdyfront++;
            rdyele--;
        }
        return aux;
    } else { //blk
        if (blkele == 0) {
            return -1;
        }
        int aux = blocked[blkfront];
        if (blkfront == blkrear) {
            blkfront = -1;
            blkrear = -1;
            blkele = 0;
        } else if (blkfront == blksize - 1) {
            blkfront = 0;
        } else {
            blkfront++;
            blkele--;
        }
        return aux;
    }
}

int printer(int queue) {
    int length = 0;
    if (queue == 0) { //rdy
        if (front(0) != -1) {
            for (int i = rdyrear; i != rdyfront; i--) {
                printf("P%d ", ready[i]);
                if (ready[i] <= 9)
                    length += 3;
                else
                    length += 4;
                if (i == 0) {
                    i = rdysize;
                }
            }
            if (rdyfront != -1) {
                printf("P%d", ready[rdyfront]);
                if (ready[rdyfront] <= 9)
                    length += 2;
                else
                    length += 3;
            }
        }

    } else {
        if (front(1) != -1) {
            for (int i = blkrear; i != blkfront; i--) {
                printf("P%d ", blocked[i]);
                if (i == 0) {
                    i = blksize;
                }
            }
            if (blkfront != -1)
                printf("P%d", blocked[blkfront]);

        }
    }
    return length;
}

enum STATE {
    PRE_NEW,
    NEW,
    READY,
    RUN,
    BLOCK,
    EXIT,
    FINISHED
};

typedef struct {
    int id;
    int initial;
    int index;
    int inst_start;
    int var_start;
    int instructions[198];
    int n_instructions;
    int n_vars;
    int total;
    enum STATE state;
} process_block;

void free_process(process_block p[], int id) {
    p[id - 1].state = FINISHED;
    int beginning = p[id - 1].inst_start;
    for (int i = beginning; i < beginning + p[id - 1].total; i++) {
        bit_vector[i] = 0;
        mem[i] = 0;
    }

    free_space += p[id - 1].total;
}

int allocate_fork(process_block *p, int index_pai) {
    int i, j, k, segment = -1, index_inicio = -1, index_medio, index_final, process_size = p[index_pai].total;
    bool isAlocated = false;

    if (process_size > free_space) {
        return  -20;
    }

    p[n_processes].id = n_processes + 1;
    memcpy(p[n_processes].instructions, p[index_pai].instructions, 198);
    p[n_processes].n_instructions = p[index_pai].n_instructions;
    p[n_processes].n_vars = p[index_pai].n_vars;
    p[n_processes].total = p[index_pai].total;

    for (i = next_index; i < MEMORY_SIZE; i++) {
        if (bit_vector[i] == 0) {
            segment++;
            if (segment == 0) { // start again
                index_inicio = i;
                segment++;
            }
            if (segment == process_size) {
                next_index = index_inicio + process_size;
                // allocate
                index_medio = index_inicio + p[n_processes].n_instructions * 2;
                index_final = index_inicio + p[n_processes].total;
                p[n_processes].inst_start = index_inicio;
                p[n_processes].var_start = index_medio;
                p[n_processes].state = READY;
                p[n_processes].index = index_inicio;

                free_space -= process_size;

                for (j = index_inicio, k = 0; j < index_medio; j++, k++) {
                    bit_vector[j] = n_processes + 1;
                    mem[j] = p[n_processes].instructions[k];
                }

                for (j = index_medio; j < index_final; j++) {
                    bit_vector[j] = n_processes + 1;
                    mem[j] = 0;
                }

                isAlocated = true;

                if (index_inicio >= MEMORY_SIZE)
                    index_inicio = 0;

                n_processes++;
                for(j = 0; j < p[n_processes - 1].n_vars; j++) {
                    mem[p[n_processes - 1].var_start + j] = mem[p[index_pai].var_start + j];
                }

                return 20;
            }
        } else {
            segment = -1;
            index_inicio = -1;
        }
    }

    if (!isAlocated) {
        segment = -1;
        index_inicio = -1;

        for (i = 0; i < MEMORY_SIZE; i++) {
            if (bit_vector[i] == 0) {
                segment++;
                if (segment == 0) { // start again
                    index_inicio = i;
                    segment++;
                }
                if (segment == process_size) {
                    next_index = index_inicio + process_size;
                    // allocate
                    index_medio = index_inicio + p[n_processes].n_instructions * 2;
                    index_final = index_inicio + p[n_processes].total;
                    p[n_processes].inst_start = index_inicio;
                    p[n_processes].var_start = index_medio;
                    p[n_processes].state = READY;
                    enqueue(n_processes + 1, 0);
                    p[n_processes].index = index_inicio;

                    free_space -= process_size;

                    for (j = index_inicio, k = 0; j < index_medio; j++, k++) {
                        bit_vector[j] = n_processes + 1;
                        mem[j] = p[n_processes].instructions[k];
                    }

                    for (j = index_medio; j < index_final; j++) {
                        bit_vector[j] = n_processes + 1;
                        mem[j] = 0;
                    }

                    isAlocated = true;

                    if (index_inicio >= MEMORY_SIZE)
                        index_inicio = 0;

                    n_processes++;
                    for(j = 0; j < p[n_processes - 1].n_vars; j++) {
                        mem[p[n_processes - 1].var_start + j] = mem[p[index_pai].var_start + j];
                    }

                    return 20;
                }
            } else {
                segment = -1;
                index_inicio = -1;
            }
        }
    }

    return -20;
}

void allocate_next_fit(process_block *p, int index) {
    int i, j, k, segment = -1, index_inicio = -1, index_medio, index_final, process_size = p[index].total;
    bool isAlocated = false;

    if (process_size > free_space) {
        p[index].state = FINISHED;
        printf("> P%d failed <\n", index + 1);
        return;
    }

    for (i = next_index; i < MEMORY_SIZE; i++) {
        if (bit_vector[i] == 0) {
            segment++;
            if (segment == 0) { // start again
                index_inicio = i;
                segment++;
            }
            if (segment == process_size) {
                next_index = index_inicio + process_size;
                // allocate
                index_medio = index_inicio + p[index].n_instructions * 2;
                index_final = index_inicio + p[index].total;
                p[index].inst_start = index_inicio;
                p[index].var_start = index_medio;
                p[index].state = NEW;
                p[index].index = index_inicio;

                free_space -= process_size;

                for (j = index_inicio, k = 0; j < index_medio; j++, k++) {
                    bit_vector[j] = index + 1;
                    mem[j] = p[index].instructions[k];
                }

                for (j = index_medio; j < index_final; j++) {
                    bit_vector[j] = index + 1;
                    mem[j] = 0;
                }

                isAlocated = true;

                if (index_inicio >= MEMORY_SIZE)
                    index_inicio = 0;

                return;
            }
        } else {
            segment = -1;
            index_inicio = -1;
        }
    }

    if (!isAlocated) {
        segment = -1;
        index_inicio = -1;

        for (i = 0; i < MEMORY_SIZE; i++) {
            if (bit_vector[i] == 0) {
                segment++;
                if (segment == 0) { // start again
                    index_inicio = i;
                    segment++;
                }
                if (segment == process_size) {
                    next_index = index_inicio + process_size;
                    // allocate
                    index_medio = index_inicio + p[index].n_instructions * 2;
                    index_final = index_inicio + p[index].total;
                    p[index].inst_start = index_inicio;
                    p[index].var_start = index_medio;
                    p[index].state = NEW;
                    p[index].index = index_inicio;

                    free_space -= process_size;

                    for (j = index_inicio, k = 0; j < index_medio; j++, k++) {
                        bit_vector[j] = index + 1;
                        mem[j] = p[index].instructions[k];
                    }

                    for (j = index_medio; j < index_final; j++) {
                        bit_vector[j] = index + 1;
                        mem[j] = 0;
                    }

                    isAlocated = true;

                    if (index_inicio >= MEMORY_SIZE)
                        index_inicio = 0;

                    return;
                }
            } else {
                segment = -1;
                index_inicio = -1;
            }
        }
    }

    p[index].state = FINISHED;
    printf("> P%d failed <\n", index + 1);
}

void defragment(process_block p[], int n) {
    int a[MEMORY_SIZE]; // aux mem
    int b[MEMORY_SIZE]; // aux bit_vector
    int c[n];
    int index = 0, id_proc = 0;

    for (int i = 0; i < n; i++) {
        c[i] = 0;
    }

    for (int i = 0; i < MEMORY_SIZE; i++) {
        if (bit_vector[i] != 0) {
            if (c[bit_vector[i] - 1] == 0) {
                int u = bit_vector[i] - 1;
                int x = p[bit_vector[i] - 1].index;
                int y = p[bit_vector[i] - 1].inst_start;
                c[u] = x - y;
            }

            a[index] = mem[i];
            b[index] = bit_vector[i];
            index++;
        }
    }

    for (int i = index; i < MEMORY_SIZE; i++) {
        a[i] = 0;
        b[i] = 0;
    }

    for (int i = 0; i < MEMORY_SIZE; i++) {
        mem[i] = a[i];
        bit_vector[i] = b[i];

        if (id_proc != b[i] && b[i] != 0) {
            p[b[i] - 1].index = c[b[i] - 1] + i;
            p[b[i] - 1].inst_start = i;
            p[b[i] - 1].var_start = p[b[i] - 1].inst_start + p[b[i] - 1].n_instructions * 2;
            id_proc = b[i];
        }
    }
}

int operate(process_block p[], int id) {
    int index = p[id - 1].index;
    int code = mem[index];
    int value = mem[index + 1];
    int dest, to_print = 0;

    if (index >= p[id - 1].var_start || index < p[id - 1].inst_start) {
        free_process(p, id);
        return -1;
    }

    switch (code) {
        case ZER:
            mem[p[id - 1].var_start] = value;
            p[id - 1].index += 2;
            break;
        case CPY:
            if (value > 0) {
                mem[p[id - 1].var_start + value] = mem[p[id - 1].var_start];
                p[id - 1].index += 2;
            } else {
                to_print = -2;
                free_process(p, id);
            }
            break;
        case DEC:
            if (value >= 0) {
                mem[p[id - 1].var_start + value]--;
                p[id - 1].index += 2;
            } else {
                to_print = -2;
                free_process(p, id);
            }
            break;
        case FRK:
            to_print = allocate_fork(p, id - 1);
            p[id - 1].index += 2;
            if (to_print == 20) {
                p[n_processes - 1].index += p[id - 1].index - p[id - 1].inst_start;
                mem[p[id - 1].var_start + value] = n_processes;
                mem[p[n_processes - 1].var_start + value] = 0;
            }
            break;
        case JFW:
            dest = index + value * 2;
            if (dest >= p[id - 1].var_start || dest < p[id - 1].inst_start) {
                to_print = -1;
                free_process(p, id);
            } else {
                p[id - 1].index = dest;
            }
            break;
        case JBK:
            dest = index - value * 2;
            if (dest >= p[id - 1].var_start || dest < p[id - 1].inst_start) {
                to_print = -1;
                free_process(p, id);
            } else {
                p[id - 1].index = dest;
            }
            break;
        case DSK:
            to_print = 10;
            break;
        case JIZ:
            dest = index + 4;
            if (mem[p[id - 1].var_start + value] == 0) {
                if (dest >= p[id - 1].var_start || dest < p[id - 1].inst_start) {
                    to_print = -1;
                    free_process(p, id);
                } else
                    p[id - 1].index = dest;
            } else {
                p[id - 1].index += 2;
            }
            break;
        case PRT:
            if (value >= 0) {
                to_print = 1;
                p[id - 1].index += 2;
            } else {
                to_print = -2;
                free_process(p, id);
            }
            break;
        case HLT:
            p[id - 1].state = EXIT;
            to_print = 0;
            break;
        case ERR:
            to_print = -5;
        default:
            break;
    }
    return to_print;
}

int get_code(char *instruction) {
    int code = -1;
    if (strcmp(instruction, "ZER") == 0)
        code = ZER;
    else if (strcmp(instruction, "CPY") == 0)
        code = CPY;
    else if (strcmp(instruction, "DEC") == 0)
        code = DEC;
    else if (strcmp(instruction, "FRK") == 0)
        code = FRK;
    else if (strcmp(instruction, "JFW") == 0)
        code = JFW;
    else if (strcmp(instruction, "JBK") == 0)
        code = JBK;
    else if (strcmp(instruction, "DSK") == 0)
        code = DSK;
    else if (strcmp(instruction, "JIZ") == 0)
        code = JIZ;
    else if (strcmp(instruction, "PRT") == 0)
        code = PRT;
    else if (strcmp(instruction, "HLT") == 0)
        code = HLT;
    else
        code = ERR;

    return code;
}

int get_greater(int inst_code, int value, int prev_greater) {
    switch (inst_code) {
        case CPY:
        case DEC:
        case FRK:
        case JIZ:
        case PRT:
            if (prev_greater < value)
                prev_greater = value;
            break;
        default:
            break;
    }
    return prev_greater;
}

FILE *open_file(char *nome) {
    FILE *pointer = fopen(nome, "r");
    return pointer;
}

int get_n_process(FILE *pointer) {
    char *line;
    size_t size = 0;
    int counter = 0;

    if (pointer == NULL)
        return -1;

    while (getline(&line, &size, pointer) != -1) {
        char *function = strtok(line, " ");
        if (strcmp(function, "INI") == 0) {
            counter++;
        } else if (strcmp(function, "FRK") == 0) {
            hasFork = true;
        }
    }

    rewind(pointer);

    return counter;
}

void read_file(FILE *apontador, process_block processos[]) {
    if (apontador == NULL)
        return;

    int greater_var, i = -1, j = -1, counter;
    char *line;
    size_t size = 0;

    while (getline(&line, &size, apontador) != -1) {
        if (strcmp(line, "\n") != 0) {
            char *function = strtok(line, " ");
            if (strcmp(function, "INI") == 0) {
                i++;
                if (i > 0) {
                    processos[i - 1].n_instructions = counter;
                    processos[i - 1].n_vars = greater_var + 1;
                    processos[i - 1].total = processos[i - 1].n_vars + processos[i - 1].n_instructions * 2;
                    processos[i - 1].state = PRE_NEW;
                    j = -1;
                }
                greater_var = 0;
                counter = 0;
                int initial = atoi(strtok(NULL, " "));
                processos[i].id = i + 1;
                processos[i].initial = initial;
                processos[i].index = 0;
            } else {
                j++;
                counter++;
                int inst_code = get_code(function);
                int value = atoi(strtok(NULL, " "));
                greater_var = get_greater(inst_code, value, greater_var);
                processos[i].instructions[j] = inst_code;
                j++;
                processos[i].instructions[j] = value;
            }
        }
    }


    processos[i].n_instructions = counter;
    processos[i].n_vars = greater_var + 1;
    processos[i].total = processos[i].n_vars + processos[i].n_instructions * 2;
    processos[i].state = PRE_NEW;
}

void runner(process_block p[]) {
    int std_out = 0, value, instant = -1, id_running = 0, quantum = QUANTUM, io = IO, length;

    printf(" T  | stdout | READY                             | RUN  | BLOCKED\n");

    for (int i = 0; i < n_processes; i++) {
        if (p[i].initial == 0) {
            allocate_next_fit(p, i);
        }
    }

    while (instant < 100) {
        value = 0;
        instant++;

        printf("%3d |", instant); //print do instante

        if (front(1) != -1) {
            if (io == 0) {
                p[front(1) - 1].state = READY;
                enqueue(dequeue(1), 0);
                io = IO;
            }
            io--;
        }

        for (int i = 0; i < n_processes; i++) {
            if (p[i].state == EXIT) {
                free_process(p, i + 1);
                id_running = 0;
            }

        }

        if (id_running != 0) {
            quantum--;
            if (quantum == 0) {
                enqueue(id_running, 0);
                p[id_running - 1].state = READY;
                id_running = 0;
            } else {
                value = operate(p, id_running);
                if (value == 1) {
                    int var = mem[p[id_running - 1].index - 1];
                    std_out = mem[var + p[id_running - 1].var_start];
                }
            }

        }

        for (int i = 0; i < n_processes; i++) {
            if (p[i].state == NEW && p[i].initial != instant) {
                enqueue(i + 1, 0);
                p[i].state = READY;
            } else if (p[i].state == PRE_NEW && p[i].initial == instant) {
                allocate_next_fit(p, i);
            }
        }

        if (id_running == 0 && front(0) != -1) {
            id_running = dequeue(0);
            quantum = QUANTUM;
            p[id_running - 1].state = RUN;
            value = operate(p, id_running);
            if (value == 1) {
                int var = mem[p[id_running - 1].index - 1];
                std_out = mem[var + p[id_running - 1].var_start];
            }
        }

        if (value != 1) {
            printf("        | "); // print do stdout
        } else {
            printf(" %-6d | ", std_out);
        }
        length = printer(0);

        for(int i = length + 2; i < 35; i++) {
            printf(" ");
        }

        printf(" |");
        if (id_running == 0)
            printf("      |"); // Print do running proces
        else {
            printf("  P%d  |", id_running);
        }

        printf(" ");
        length = printer(1);  //Print da blocked queue

        if (value == 10) {
            p[id_running - 1].state = BLOCK;
            p[id_running - 1].index += 2;
            enqueue(id_running, 1);
            if (front(0) != -1) {
                id_running = dequeue(0);
                p[id_running - 1].state = RUN;
                quantum = QUANTUM;
            } else {
                id_running = 0;
            }
        }

        printf("\n");

        if (value == -2) {
            printf("> Can't find variable - P%d terminated <\n", id_running);
        } else if (value == -1) {
            printf("> Segmentation fault - P%d terminated <\n", id_running);
        } else if (value == -20) {
            printf("> Fork failed <\n");
        } else if (value == 20) {
            printf("> Fork created : P%d <\n", n_processes);

            enqueue(n_processes, 0);
        }

        if (id_running != 0) {
            if (p[id_running - 1].state == FINISHED)
                id_running = 0;
        }

        int n = 0;

        for (int i = 0; i < n_processes; i++) {
            if (p[i].state != FINISHED)
                n++;
        }

        if (instant == 50) {
            defragment(p, n_processes);
            printf("> MEMORY WAS DEFRAGMENTATED <\n");
        }

        if (n == 0) {
            if (value == -1 || value == -2)
                printf("%3d |        |                                   |      | \n", instant + 1);
            printf("> All processes have been terminated <\n");
            return;
        }

        if (value == -5) {
            printf("> Invalid instruction! P%d terminated <\n", id_running);
            free_process(p, id_running);
            id_running = 0;
        }
    }
    printf("> Simulation has been finished <\n");
}

int main() {
    char *nome = "../input/input1.txt";
    int size;
    FILE *pointer = open_file(nome);

    if (pointer == NULL) {
        printf("Couldn't found file!!\n");
        return 1;
    } else
        printf("File opened successfully!\n");


    n_processes = get_n_process(pointer);

    if (hasFork) {
        size = n_processes + 99;
    } else {
        size = n_processes;
    }

    process_block processes[size];

    read_file(pointer, processes);

    runner(processes);
}