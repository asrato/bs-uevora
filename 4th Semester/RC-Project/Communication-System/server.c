#include <stdio.h>
#include <string.h> //strlen
#include <stdlib.h>
#include <errno.h>
#include <unistd.h> //close
#include <arpa/inet.h> //close
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/time.h> //FD_SET, FD_ISSET, FD_ZERO macros

#define TRUE 1
#define FALSE 0
#define PORT 1337

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZE 20
#define MSG_SIZE 100


typedef struct Message {
    char *message;
    char *destination;
    int code;
    int sd;
} Message;

typedef struct OfflineFile {
    char *FileName;
    char *destination;
    char *sender;
    int sent;
} OfflineFile;

typedef struct OfflineMessage {
    char *message;
    char *destination;
    int sent;
    char *sender;
} OfflineMessage;


char *get_message() {
    char *message = malloc(sizeof(char) * MSG_SIZE + 1);
    int length;

    printf("\nMessage: ");
    scanf("%[^\n]%*c", message);

    length = strlen(message);

    while (length < 1 || length > MSG_SIZE) {

        printf("Message must have between 1 and %d characters!\nMessage: ", MSG_SIZE);
        scanf("%[^\n]%*c", message);

        length = strlen(message);
    }

    return message;
}

char bytefromtext(char *text) {
    char result = 0;
    for (int i = 0; i < 8; i++) {
        if (text[i] == '1') {
            result |= (1 << (7 - i));
        }
    }
    return result;
}

void turn_bit(FILE *ftpr, FILE *txt) {

    int c;

    if (ftpr == NULL) {
        printf("File not found\n");
    } else {
        printf("File found\n");

        do {
            c = fgetc(ftpr);
            for (int i = 0; i <= 7; i++) {
                if (c & (1 << (7 - i))) {
                    fputc('1', txt);
                } else {
                    fputc('0', txt);
                }
            }
        } while (c != EOF);

    }
    fputc('e', txt);
    return;
}

void turn_normal(char *nome) {

    FILE *pfile;
    FILE *image;
    char buf[8];
    char c;
    int j = 0;

    image = fopen(nome, "w"); //open an empty .bmp file to
    //write characters from the source image file
    pfile = fopen("test2.txt", "rb");

    if (pfile == NULL)
        printf("error");
    else {
        c = fgetc(pfile);

        while (c != EOF) {
            buf[j++] = c;
            if (j == 8) {
                fputc(bytefromtext(buf), image);
                j = 0;
            }
            c = fgetc(pfile);

        }

        fclose(pfile);
        fclose(image);
        printf("File Received\n");
        fflush(stdout);
    }

}

char *substr(const char *src, int m, int n) {
    // get the length of the destination string
    int len = n - m;

    // allocate (len + 1) chars for destination (+1 for extra null character)
    char *dest = (char *) malloc(sizeof(char) * (len + 1));

    // extracts characters between m'th and n'th index from source string
    // and copy them into the destination string
    for (int i = m; i < n && (*(src + i) != '\0'); i++) {
        *dest = *(src + i);
        dest++;
    }

    // null-terminate the destination string
    *dest = '\0';

    // return the destination string
    return dest - len;
}

Message decode(char *message) {
    Message msg;

    if (message[0] != '/') { // default
        msg.code = 0;  // messagem para todos
        msg.message = message;
        msg.destination = "@";
    } else {
        char *token = strtok(message, " ");
        int length = strlen(token);
        char *op = substr(token, 1, length);
        // tratar do comando
        printf("%s\n", op);
        if (strcmp(op, "msg") == 0) {
            msg.code = 1;

            token = strtok(NULL, " ");
            msg.destination = token;

            token = strtok(NULL, "");
            msg.message = token;
        } else if (strcmp(op, "file") == 0) {
            msg.code = 2;

            token = strtok(NULL, " ");
            if (strcmp(token, "all") == 0)
                msg.destination = "@";
            else
                msg.destination = token;

            token = strtok(NULL, "");
            msg.message = token;
        } else if (strcmp(op, "play") == 0) {
            msg.code = 3;

            token = strtok(NULL, " ");
            msg.message = token;

            token = strtok(NULL, "");
            msg.destination = token;
        } else if (strcmp(op, "username") == 0) {
            msg.code = 10;

            token = strtok(NULL, " ");
            msg.destination = token;

        } else if (strcmp(op, "list\n") == 0) {
            fflush(stdout);
            msg.code = 4;


        } else {
            msg.code = 100;
        }
    }
    printf("%d\n", msg.code);
    fflush(stdout);
    return msg;
}

void write_file(char buffer[]) {
    int n;
    FILE *fp;
    char *filename = "recv.txt";
    printf("%s", buffer);
    fflush(stdout);

    fp = fopen(filename, "w");
    while (1) {
        fprintf(fp, "%s", buffer);
        bzero(buffer, 10240);
    }
    return;
}

void send_file(FILE *fp, int sockfd) {

    char data[1024] = {0};
    while (fgets(data, 1024, fp) != NULL) {
        fflush(stdout);
        if (send(sockfd, data, sizeof(data), 0) == -1) {
            perror("Error");
            exit(1);
        }
        fflush(stdout);
        bzero(data, SIZE);
    }
    printf("Message Sent\n");
    fflush(stdout);
    return;
}

int check_username(char *username, char *usernames[], int max) {
    for (int i = 0; i < max; i++) {
        if (strcmp(username, usernames[i]) == 0) {
            return i;
        }
    }
    return -1;
}

int delete_file(char *file_path) {
    return remove(file_path);
}

int main(int argc, char *argv[]) {
    int opt = TRUE;
    int master_socket, addrlen, new_socket, client_socket[30],
            max_clients = 30, activity, i, valread, sd, aux;
    int max_sd;
    struct sockaddr_in address;
    int a;
    OfflineMessage to_send[1000];
    OfflineFile file_to_send[1000];
    int next_message = 0;
    int next_file = 0;
    int max = 30;
    char *usernames[max];
    int checku;

    for (int o = 0; o < max; o++) {
        usernames[o] = "";
    }


    char buffer[1024]; //data buffefr of 1K

    //set of socket descriptors
    fd_set readfds;

    //a message
    char *message = "ECHO Daemon v1.0 \r\n";

    //initialise all client_socket[] to 0 so not checked
    for (i = 0; i < max_clients; i++) {
        client_socket[i] = 0;
    }

    //create a master socket
    if ((master_socket = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    //set master socket to allow multiple connections ,
    //this is just a good habit, it will work without this
    if (setsockopt(master_socket, SOL_SOCKET, SO_REUSEADDR, (char *) &opt,
                   sizeof(opt)) < 0) {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }

    //type of socket created
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    //bind the socket to localhost port 8888
    if (bind(master_socket, (struct sockaddr *) &address, sizeof(address)) < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    printf("Listener on port %d \n", PORT);

    //try to specify maximum of 3 pending connections for the master socket
    if (listen(master_socket, 3) < 0) {
        perror("listen");
        exit(EXIT_FAILURE);
    }

    //accept the incoming connection
    addrlen = sizeof(address);
    puts("Waiting for connections ...");

    while (TRUE) {
        //clear the socket set
        FD_ZERO(&readfds);

        //add master socket to set
        FD_SET(master_socket, &readfds);
        max_sd = master_socket;

        //add child sockets to set
        for (i = 0; i < max_clients; i++) {
            //socket descriptor
            sd = client_socket[i];

            //if valid socket descriptor then add to read list
            if (sd > 0)
                FD_SET(sd, &readfds);

            //highest file descriptor number, need it for the select function
            if (sd > max_sd)
                max_sd = sd;
        }

        //wait for an activity on one of the sockets , timeout is NULL ,
        //so wait indefinitely
        activity = select(max_sd + 1, &readfds, NULL, NULL, NULL);

        if ((activity < 0) && (errno != EINTR)) {
            printf("select error");
        }

        //If something happened on the master socket ,
        //then its an incoming connection
        if (FD_ISSET(master_socket, &readfds)) {
            if ((new_socket = accept(master_socket,
                                     (struct sockaddr *) &address, (socklen_t *) &addrlen)) < 0) {
                perror("accept");
                exit(EXIT_FAILURE);
            }

            //inform user of socket number - used in send and receive commands
            printf("New connection , socket fd is %d , ip is : %s , port : %d\n", new_socket,
                   inet_ntoa(address.sin_addr), ntohs(address.sin_port));


            //add new socket to array of sockets
            for (i = 0; i < max_clients; i++) {
                //if position is empty
                if (client_socket[i] == 0) {
                    client_socket[i] = new_socket;
                    printf("Adding to list of sockets as %d\n", i);

                    break;
                }
            }
        }

        //else its some IO operation on some other socket
        for (i = 0; i < max_clients; i++) {
            sd = client_socket[i];
            memset(buffer, 0, sizeof(buffer));

            if (FD_ISSET(sd, &readfds)) {

                //Check if it was for closing , and also read the
                //incoming message
                if ((valread = read(sd, buffer, sizeof(buffer))) == 0) {
                    //Somebody disconnected , get his details and print
                    getpeername(sd, (struct sockaddr *) &address, \
                        (socklen_t *) &addrlen);
                    printf("Host disconnected , ip %s , port %d \n",
                           inet_ntoa(address.sin_addr), ntohs(address.sin_port));

                    //Close the socket and mark as 0 in list for reuse
                    close(sd);
                    client_socket[i] = 0;
                    for (int j = 0; j < max_clients; j++) {
                        if (j != i) {
                            char disconnect[50];
                            strcpy(disconnect, usernames[i]);
                            strcat(disconnect, " has disconnected.");

                            sd = client_socket[j];

                            send(sd, disconnect, strlen(disconnect), 0);

                            buffer[valread] = '\0';
                        }
                    }
                    usernames[i] = "";

                }

                    //Echo back the message that came in
                else {
                    char au[1024];

                    memset(au, 0, sizeof(au));
                    strcpy(au, buffer);

                    Message msg = decode(buffer);
                    msg.sd = i;
                    if (msg.code == 0) {
                        for (int j = 0; j < max_clients; j++) {
                            if (j != i) {

                                strcpy(au, usernames[i]);
                                strcat(au, " to Everyone: ");
                                strcat(au, msg.message);

                                sd = client_socket[j];

                                send(sd, au, strlen(au), 0);

                                buffer[valread] = '\0';
                            }
                        }
                    } else if (msg.code == 1) {


                        fflush(stdout);
                        int checker = check_username(msg.destination, usernames, max);

                        if (checker != -1) {
                            sd = client_socket[checker];
                            strcpy(au, usernames[i]);
                            strcat(au, " to ");
                            strcat(au, usernames[checker]);
                            strcat(au, ": ");
                            strcat(au, msg.message);
                            send(sd, au, strlen(au), 0);

                            buffer[valread] = '\0';
                            memset(buffer, 0, sizeof(buffer));
                            fflush(stdout);
                        } else {

                            to_send[next_message].message = strdup(msg.message);

                            to_send[next_message].destination = strdup(msg.destination);

                            to_send[next_message].sender = strdup(usernames[i]);

                            to_send[next_message].sent = 0;
                            next_message++;
                        }


                    } else if (msg.code == 2) {
                        int checker = check_username(msg.destination, usernames, max);
                        if (checker != -1 || strcmp(msg.destination, "@") == 0) {

                            aux = client_socket[checker];

                            FILE *fp;
                            fp = fopen("recv.txt", "w");
                            fflush(stdout);


                            while (((buffer[strlen(buffer) - 1]) != 'e') ||
                                   (msg.message[strlen(msg.message) - 1]) != 'e') {
                                if (buffer[0] == 'e') {
                                    break;
                                }

                                memset(buffer, 0, sizeof(buffer));
                                valread = read(sd, buffer, 1024);

                                fprintf(fp, "%s", buffer);

                            }

                            char ch;

                            fclose(fp);
                            fp = fopen("recv.txt", "r");
                            fflush(stdout);
                            if ((ch = fgetc(fp)) == 'e') {
                                fclose(fp);
                            } else {

                                fclose(fp);
                                if (strcmp(msg.destination, "@") != 0 && aux != 0) {

                                    fp = fopen("recv.txt", "r");
                                    send(aux, au, 1024, 0);
                                    send_file(fp, aux);
                                    fclose(fp);
                                    delete_file("recv.txt");
                                } else {
                                    for (int j = 0; j < max_clients; j++) {
                                        if (j != i && client_socket[j] != 0) {
                                            fp = fopen("recv.txt", "r");
                                            aux = client_socket[j];
                                            ///  printf("123: %s\n", au);
                                            send(aux, au, 1024, 0);

                                            send_file(fp, aux);
                                            fclose(fp);

                                        }
                                    }
                                    delete_file("recv.txt");

                                }
                            }
                        } else {
                            int new_file = 1;
                            for (int j = 0; j < next_file; j++) {
                                if (strcmp(file_to_send[j].FileName, msg.message) == 0 && file_to_send[j].sent == 0) {
                                    new_file = 0;
                                }
                            }

                            file_to_send[next_file].sent = 0;
                            file_to_send[next_file].FileName = strdup(msg.message);
                            file_to_send[next_file].destination = strdup(msg.destination);
                            file_to_send[next_file].sender = strdup(usernames[i]);
                            next_file++;

                            FILE *fp;
                            fp = fopen("test2.txt", "w");

                            while (((buffer[strlen(buffer) - 1]) != 'e') ||
                                   (msg.message[strlen(msg.message) - 1]) != 'e') {
                                if (buffer[0] == 'e') {
                                    file_to_send[next_file - 1].sent = 1;
                                    break;
                                }
                                memset(buffer, 0, sizeof(buffer));
                                valread = read(sd, buffer, 1024);

                                fprintf(fp, "%s", buffer);

                            }


                            char ch;

                            fclose(fp);
                            fp = fopen("test2.txt", "r");
                            if ((ch = fgetc(fp)) == 'e') {
                                fclose(fp);
                            } else {

                                if (new_file == 1) {

                                    turn_normal(file_to_send[next_file - 1].FileName);


                                } else {
                                    next_file--;
                                    memset(au, 0, sizeof(au));
                                    fflush(stdout);
                                    strcpy(au, "Couldn't send Offline File\n");
                                    fflush(stdout);
                                    send(sd, au, 1024, 0);

                                }
                                fclose(fp);
                                delete_file("test2.txt");
                            }
                        }
                    } else if (msg.code == 10) {

                        if (check_username(msg.destination, usernames, max) != -1) {
                            // send 0
                            buffer[0] = '0';
                        } else {
                            // send 1
                            buffer[0] = '1';
                            char opa[20];
                            strcpy(opa, msg.destination);

                            usernames[i] = strdup(opa);

                            printf("Client %s in socket %d\n", usernames[i], i);
                        }

                        send(sd, buffer, sizeof(buffer), 0);

                        for (int j = 0; j < max_clients; j++) {
                            if (j != i) {
                                char connect[50];

                                memset(connect, 0, sizeof(connect));
                                strcpy(connect, usernames[i]);
                                strcat(connect, " has connected.\0");

                                sd = client_socket[j];

                                send(sd, connect, strlen(connect), 0);

                                buffer[valread] = '\0';
                            }
                        }

                        if (buffer[0] == '1') {

                            for (int j = 0; j < next_message; j++) {

                                if (strcmp(usernames[i], to_send[j].destination) == 0 && to_send[j].sent == 0) {

                                    to_send[j].sent = 1;
                                    sd = client_socket[i];
                                    strcpy(au, "(Offline): ");
                                    strcat(au, to_send[j].sender);
                                    strcat(au, " to ");
                                    strcat(au, usernames[i]);
                                    strcat(au, ": ");
                                    strcat(au, to_send[j].message);

                                    send(sd, au, sizeof(au), 0);

                                }
                            }


                            for (int j = 0; j < next_file; j++) {

                                if (strcmp(usernames[i], file_to_send[j].destination) == 0 &&
                                    file_to_send[j].sent == 0) {

                                    file_to_send[j].sent = 1;

                                    sd = client_socket[i];
                                    strcpy(au, "(Offline): ");
                                    strcat(au, file_to_send[j].sender);
                                    strcat(au, " to ");
                                    strcat(au, usernames[i]);
                                    strcat(au, ": ");
                                    strcat(au, file_to_send[j].FileName);
                                    send(sd, au, sizeof(au), 0);

                                    strcpy(au, "/file ");
                                    strcat(au, usernames[i]);
                                    strcat(au, " ");
                                    strcat(au, file_to_send[j].FileName);
                                    strcat(au, "\0");
                                    send(sd, au, sizeof(au), 0);

                                    printf("%s", au);
                                    fflush(stdout);
                                    FILE *fp;
                                    FILE *txt;
                                    fp = fopen(file_to_send[j].FileName, "rb");
                                    txt = fopen("test1.txt", "w");

                                    turn_bit(fp, txt);
                                    fclose(txt);

                                    txt = fopen("test1.txt", "r");

                                    send_file(txt, sd);
                                    fclose(txt);
                                    fclose(fp);
                                    delete_file("test1.txt");
                                    delete_file(file_to_send[j].FileName);
                                }
                            }

                        }
                        fflush(stdout);
                        memset(buffer, 0, sizeof(buffer));
                    } else if (msg.code == 4) {
                    

                        char stringX[1024];
                        strcpy(stringX, "Users Online:\n");
                        for (int j = 0; j < max_clients; j++) {
                            if (strcmp(usernames[j], "") != 0) {
                                strcat(stringX, usernames[j]);
                                strcat(stringX, "\n");

                            }

                        }
                        send(sd, stringX, sizeof(stringX), 0);


                    }
                }
            }
        }
    }

    return 0;
}