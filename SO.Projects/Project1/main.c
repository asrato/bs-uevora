#include <stdio.h>
#define SIZE 1000
#define RR 0
#define VRR 1

typedef struct {
    int now;                                                                                                           // index of the program sub-cycle that is currently in
    int state;
    int start;                                                                                                          // starting instant of time
    int cycle[11];
} program;  // pcb

int instant = 0, number_of_programs = 0;
program programs[11];

int ready_queue[SIZE], ready_front = -1, ready_tail = -1;                                                               // ready queue --> 0
int block_queue[SIZE], block_front = -1, block_tail = -1;                                                               // block queue --> 1
int aux_queue[SIZE], aux_front = -1, aux_tail = -1;                                                                     // aux queue   --> 2

int front(int queue_number) {                                                                                           // returns the element on the first position of the queue
    if (queue_number == 0) {                                                                                            // ready queue
        if (ready_front != -1)
            return ready_queue[ready_front];
    } else if (queue_number == 1) {                                                                                     // block queue
        if (block_front != -1)
            return block_queue[block_front];
    } else if (queue_number == 2) {                                                                                     // aux queue
        if (aux_front != -1)
            return aux_queue[aux_front];
    }
    return -1;
}

void enqueue(int value, int queue_number) {                                                                             // places the element passed as argument on the last position of the queue
    if (queue_number == 0) {                                                                                            // ready queue
        if (ready_tail == SIZE - 1)
            return;

        if (ready_front == -1)
            ready_front = 0;

        ready_tail++;
        ready_queue[ready_tail] = value;
    } else if (queue_number == 1) {                                                                                     // block queue
        if (block_tail == SIZE - 1)
            return;

        if (block_front == -1)
            block_front = 0;
        block_tail++;
        block_queue[block_tail] = value;
    } else if (queue_number == 2) {                                                                                     // aux queue
        if (aux_tail == SIZE - 1)
            return;

        if (aux_front == -1)
            aux_front = 0;

        aux_tail++;
        aux_queue[aux_tail] = value;
    }
}

int dequeue(int queue_number) {                                                                                         // returns the element in the first position of the queue and removes it
    int aux;
    if (queue_number == 0) {                                                                                            // ready queue
        if (ready_front == -1)
            return -1;

        aux = ready_queue[ready_front];
        ready_front++;

        if (ready_front > ready_tail) {
            ready_front = -1;
            ready_tail = -1;
        }

        return aux;
    } else if (queue_number == 1) {                                                                                     // block queue
        if (block_front == -1)
            return -1;

        aux = block_queue[block_front];
        block_front++;

        if (block_front > block_tail) {
            block_front = -1;
            block_tail = -1;
        }

        return aux;
    } else if (queue_number == 2) {                                                                                     // aux queue
        if (aux_front == -1)
            return -1;

        aux = aux_queue[aux_front];
        aux_front++;

        if (aux_front > aux_tail) {
            aux_front = -1;
            aux_tail = -1;
        }

        return aux;
    }
    return -1;
}

void state_change(int program_id, int is_vrr) {                                                                         // Makes a change from a state to another to the program passed as argument
    int currently_running;
    int aux;
    switch (programs[program_id].state) {
        case 0:                                                                                                         // Exit
            programs[program_id].state = 6;                                                                             // EXT --> --- (finished program)

            break;
        case 1:                                                                                                         // Ready
            dequeue(0);
            programs[program_id].state = 2;                                                                             // RDY --> RUN
            programs[program_id].now++;

            break;
        case 2:                                                                                                         // Run
            if (is_vrr == 0) {                                                                                          // Round Robin Standard (RR)
                if (programs[program_id].cycle[programs[program_id].now + 1] == 0)
                    programs[program_id].state = 0;                                                                     // RUN --> EXT
                else {
                    enqueue(program_id, 1);
                    programs[program_id].state = 3;                                                                     // RUN --> BLK
                    programs[program_id].now++;
                }

                aux = front(0);

                if (aux != -1) {
                    dequeue(0);
                    programs[aux].state = 2;                                                                            // RDY --> RUN
                    programs[aux].now++;
                }
            } else if (is_vrr == 1) {                                                                                   // Virtual Round Robin (VRR)
                if (programs[program_id].cycle[programs[program_id].now + 1] == 0)
                    programs[program_id].state = 0;                                                                     // RUN --> EXT
                else {
                    enqueue(program_id, 1);
                    programs[program_id].state = 3;                                                                     // RUN --> BLK
                    programs[program_id].now++;
                }

                aux = front(2);

                if (aux != -1) {
                    dequeue(2);
                    programs[aux].state = 2;                                                                            // AUX --> RUN
                    programs[aux].now++;
                } else {
                    if (front(0) != -1) {
                        aux = front(0);
                        dequeue(0);
                        programs[aux].state = 2;                                                                        // RDY --> RUN
                        programs[aux].now++;
                    }
                }
            }

            break;

        case 3:                                                                                                         // Block
            if (is_vrr == 0) {                                                                                          // Round Robin Standard (RR)
                aux = dequeue(1);

                if (aux != -1) {
                    programs[aux].state = 1;                                                                            // BLK --> RDY
                    enqueue(aux, 0);
                }
            } else if (is_vrr == 1) {                                                                                   // Virtual Round Robin (VRR)
                aux = dequeue(1);

                if (aux != -1) {
                    programs[aux].state = 7;                                                                            // BLK --> AUX
                    enqueue(aux, 2);
                }
            }

            break;
        case 4:                                                                                                         // (not created program)
            programs[program_id].state = 5;                                                                             //     --> NEW

            break;
        case 5:                                                                                                          // New
            currently_running = 0;
            for (int i = 0; i < number_of_programs; i++) {
                if (programs[i].state == 2)
                    currently_running=1;
            }
            if(currently_running==0){
                programs[program_id].state = 2;
                programs[program_id].now++;
            } else{
                programs[program_id].state = 1;                                                                         // NEW --> RDY
                enqueue(program_id, 0);
            }

            break;
        case 7:                                                                                                         // Aux
            if (front(2) != -1) {
                aux = dequeue(2);
                programs[aux].state = 2;                                                                                // AUX --> RUN
                programs[aux].now++;
            } else {
                dequeue(0);
                programs[program_id].state = 2;                                                                         // RDY --> RUN
                programs[program_id].now++;
            }

            break;
        default:
            break;
    }
}

void quantum_change(int program_id, int is_vrr) {                                                                       // makes the change from RUN to RDY when a program has spent all of its quantum time in the programor
    int ready = front(0);
    int aux = front(2);

    if (is_vrr == 0) {                                                                                                  // Round Robin Standard (RR)
        if (ready != -1) {
            dequeue(0);
            programs[ready].state = 2;                                                                                  // RDY --> RUN
            programs[ready].now++;
            enqueue(program_id, 0);
            programs[program_id].state = 1;                                                                             // RUN --> RDY
            programs[program_id].now--;
        }
    } else if (is_vrr == 1) {                                                                                           // Virtual Round Robin (VRR)
        if (aux != -1 || ready != -1) {
            if (aux != -1) {
                dequeue(2);
                programs[aux].state = 2;                                                                                // AUX --> RUN
                programs[aux].now++;
            } else {
                dequeue(0);
                programs[ready].state = 2;                                                                              // RDY --> RUN
                programs[ready].now++;
            }
            enqueue(program_id, 0);
            programs[program_id].state = 1;                                                                             // RUN --> RDY
            programs[program_id].now--;
        }
    }
}

void run(int quantum_time, int is_vrr) {                                                                                // simulates the running of the 5 state model and prints every instant's information
    for (int i = 0; i < number_of_programs; i++)
        if (programs[i].start == 0)
            programs[i].state = 5;

    instant = 1;
    int quantum_unity = quantum_time;

    while (instant != 0) {
        int is_running = 0;
        int number_of_executing_programs = 0;
        int running_program;

        printf(" %4d |", instant);

        for (int i = 0; i < number_of_programs; i++) {

            if ((programs[i].state == 2) || (front(1) == i && programs[i].state == 3))                                  // If program is running or program is blocked --> decrement program block cycle
                programs[i].cycle[programs[i].now]--;

            switch (programs[i].state) {
                case 0:
                    printf("  EXT  |");

                    break;
                case 1:
                    printf("  RDY  |");

                    break;
                case 2:
                    printf("  RUN  |");

                    is_running = 1;
                    quantum_unity--;
                    running_program = i;

                    break;
                case 3:
                    printf("  BLK  |");

                    break;
                case 4:
                    printf("       |");

                    break;
                case 5:
                    printf("  NEW  |");

                    break;
                case 6:
                    printf("  ---  |");

                    break;
                case 7:
                    printf("  AUX  |");

                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < number_of_programs; i++) {

            if (programs[i].state == 0)                                                                                 // If program is exiting --> terminate the program
                state_change(i, is_vrr);


            if (programs[i].cycle[programs[i].now] == 0 && programs[i].state == 2) {                                    // If program cycle is 0 and program state is running --> change the state of the program
                if (is_vrr == 0) {                                                                                      // Round Robin Standard (RR)
                    if (front(0) == -1)                                                                                 // If ready queue is empty
                        is_running = 0;
                } else if (is_vrr == 1) {                                                                               // Virtual Round Robin (VRR)
                    if (front(2) == -1)                                                                                 // If aux queue is empty
                        if (front(0) == -1)                                                                             // If ready queue is empty
                            is_running = 0;
                }

                state_change(i, is_vrr);
                quantum_unity = quantum_time;
            }

            if (programs[i].cycle[programs[i].now] == 0 && programs[i].state == 3)                                      // If program cycle is 0 and program state is blocked --> change the state of the program
                state_change(i, is_vrr);

            if (programs[i].state != 6)                                                                                 // If program isn't finished --> increment number_of_executing_programs variable
                number_of_executing_programs++;
        }

        printf("\n");

        if (is_vrr == 0) {                                                                                              // Round Robin Standard (RR)
            if (is_running == 0 && front(0) != -1)                                                                      // If no programs are running and ready queue isn't empty --> change the state of the queue program
                state_change(front(0), is_vrr);

        } else if (is_vrr == 1) {                                                                                       // Virtual Round Robin (VRR)
            if (is_running == 0 && front(2) != -1)                                                                      // If no programs are running and aux queue isn't empty --> change the state of the queue program
                state_change(front(2), is_vrr);
            else if (is_running == 0 && front(0) != -1)                                                                 // If no programs are running and ready queue isn't empty --> change the state of the queue program
                state_change(front(0), is_vrr);
        }

        if (quantum_unity == 0) {                                                                                       // If the quantum cycle ends --> change running program and reset quantum cycle
            quantum_change(running_program, is_vrr);
            quantum_unity = quantum_time;
        }

        for (int i = 0; i < number_of_programs; i++) {
            if (programs[i].state == 5)                                                                                 // If program is new --> put the program in ready queue
                state_change(i, is_vrr);

            if (programs[i].start == instant && programs[i].state == 4)                                                 // If program isn't created --> create the program
                state_change(i, is_vrr);
        }

        instant++;
        if (number_of_executing_programs == 0) {                                                                        // If there are no programs executing
            printf(" %4d |", instant);

            for (int i = 0; i < number_of_programs; i++)
                printf("  ---  |");

            instant = 0;
        }

    }

    printf("\nALL PROGRAMS HAVE TERMINATED.");
}

void create(int rows, int columns, int array_of_programs[rows][columns], int quantum_time, int is_vrr) {               // Responsible for creating every program along with its info
    number_of_programs = rows;

    printf(" Inst |");

    for (int i = 0; i < rows; i++) {

        if (i > 8)
            printf("  P%d  |", i + 1);
        else
            printf("  P0%d  |", i + 1);

        programs[i].now = 0;
        programs[i].state = 4;

        for (int j = 0; j < columns; j++)
            programs[i].cycle[j] = array_of_programs[i][j];

        programs[i].start = programs[i].cycle[0];
    }

    instant = 0;

    printf("\n");

    run(quantum_time, is_vrr);
}

int main() {
    int programas[5][10] = {
            {0, 3, 1, 2, 2, 4, 1, 1, 1, 1 },
            {1, 2, 4, 2, 4, 2, 0, 0, 0, 0 },
            {3, 1, 6, 1, 6, 1, 6, 1, 0, 0 },
            {3, 6, 1, 6, 1, 6, 1, 6, 0, 0 },
            {5, 9, 1, 9, 0, 0, 0, 0, 0, 0 }
    };
    int number_of_rows = sizeof(programas) / sizeof(programas[0]);
    int number_of_columns = sizeof(programas[0]) / sizeof(programas[0][0]);

    int quantum_time = 3;
    int run_mode = RR;

    create(number_of_rows, number_of_columns, programas, quantum_time, run_mode);

    return 0;
}