#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>

#define SIZE 1024

#define STDIN 0

#define PORT 1337

typedef struct Message {
    char *message;
    char *destination;
    int code;
} Message;

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

void send_file(FILE *fp, int sockfd) {
    char data[SIZE] = {0};
    while (fgets(data, SIZE, fp) != NULL) {
        if (send(sockfd, data, sizeof(data), 0) == -1) {
            perror("Error");
            exit(1);
        }
        bzero(data, SIZE);
    }
    printf("File Sent\n");
    return;
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

        } else if (strcmp(op, "exit\n") == 0) {
            msg.code = -1;
        } else if (strcmp(op, "help\n") == 0) {
            msg.code = -2;
        } else if (strcmp(op, "list\n") == 0) {
            msg.code = 4;
        } else {
            msg.code = 100;
        }
    }

    return msg;
}

char *get_username() {
    char *string = malloc(sizeof(char) * 20 + 1);
    int length;

    printf("Username: ");
    scanf("%[^\n]%*c", string);

    length = strlen(string);

    while (length < 1 || length > 20 || strchr(string, ' ') != NULL) {

        printf("Username must have between 1 and 20 characters and can't have space characters!\nUsername: ");
        scanf("%[^\n]%*c", string);

        length = strlen(string);
    }

    return string;
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

char bytefromtext(char *text) {
    char result = 0;
    for (int i = 0; i < 8; i++) {
        if (text[i] == '1') {
            result |= (1 << (7 - i));
        }
    }
    return result;
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

int delete_file(char *file_path) {
    return remove(file_path);
}

int main(int argc, char const *argv[]) {

    int sock = 0, valread;
    struct sockaddr_in serv_addr;
    fd_set readfds;
    fd_set writefds;
    int connectFD;
    int ret;
    int check_username = 0;
    char source[1024] = "/username ";
    Message decoded_message;
    char *username;

    char buffer[1024] = {0};
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        printf("\n Socket creation error \n");
        return -1;
    }

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    // Convert IPv4 and IPv6 addresses from text to binary form
    if (inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr) <= 0) {
        printf("\nInvalid address/ Address not supported \n");
        return -1;
    }

    if (connect(sock, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        printf("\nConnection Failed \n");
        return -1;
    }

    int sd = sock;

    int login = 0;
    char send_user[] = "/username ";

    while (login == 0) {
        memset(send_user, 0, sizeof(send_user));
        strcpy(send_user, "/username ");
        username = get_username();
        int aux = sizeof(username) + sizeof(send_user);
        strcat(send_user, username);
        // send username to server

        send(sd, send_user, aux, 0);
        memset(send_user, 0, sizeof(send_user));
        ret = recv(sd, (char *) buffer, sizeof(buffer), 0);

        if (buffer[0] == '1')
            login = 1;
        else {
            printf("Username already chosen! Pick another one...\n");
        }

    }
    printf("Logged in as %s\n", username);
    printf("Enter /help for a list of Commands\n");
    fflush(stdout);

    while (1) {
        FD_ZERO(&readfds);
        FD_ZERO(&writefds);

        FD_SET(STDIN, &readfds);
        FD_SET(sock, &readfds);

        connectFD = sock;


        select(sock + 1, &readfds, &writefds, NULL, NULL);
        int sd = sock;

        if (FD_ISSET(sd, &readfds) != 0) {
            memset(buffer, 0, sizeof(buffer));
            ret = recv(sd, (char *) buffer, sizeof(buffer), 0);
            fflush(stdout);
            char buffer_um[1024];
            Message msg = decode(buffer);
            msg.message[strlen(msg.message) - 1] = '\0';
            strcpy(buffer_um, msg.message);

            if (ret > 0 && msg.code == 0) {
                printf("%s\n", buffer);
                fflush(stdout);
                memset(buffer, 0, sizeof(buffer));
            } else if (msg.code == 2) {

                FILE *fp;
                fp = fopen("test2.txt", "w");
                while (((buffer[strlen(buffer) - 1]) != 'e') || (msg.message[strlen(msg.message) - 1]) != 'e') {

                    memset(buffer, 0, sizeof(buffer));
                    valread = read(sd, buffer, 1024);
                    fprintf(fp, "%s", buffer);

                }

                fclose(fp);
                turn_normal(buffer_um);
                memset(buffer, 0, sizeof(buffer));
                memset(buffer_um, 0, sizeof(buffer_um));
                if (delete_file("test2.txt")) {
                    printf("Error deleting recv file\n");
                }
            }
        } else if (FD_ISSET(STDIN, &readfds) != 0) {

            char msg[1024];
            memset(msg, 0, sizeof(msg));
            fgets(msg, sizeof(msg), stdin);

            send(sd, msg, sizeof(msg), 0);
            decoded_message = decode(msg);
            if (decoded_message.code == 2) {
                decoded_message.message[strlen(decoded_message.message) - 1] = '\0';
                FILE *ftpr;
                FILE *txt;

                ftpr = fopen(decoded_message.message, "rb");
                if (ftpr == NULL) {
                    printf("File not found\n");
                    memset(msg, 0, sizeof(msg));
                    msg[0] = 'e';
                    send(sd, msg, sizeof(msg), 0);
                } else {

                    txt = fopen("test1.txt", "w");

                    turn_bit(ftpr, txt);


                    fclose(txt);

                    fclose(ftpr);

                    txt = fopen("test1.txt", "r");
                    send_file(txt, sd);
                    fflush(stdout);
                    if (delete_file("test1.txt")) {
                        printf("Error deleting recv file\n");
                    }
                }
            } else if (decoded_message.code == -1) {
                printf("Goodbye!\n");
                fflush(stdout);
                exit(0);
            } else if (decoded_message.code == -2) {
                printf("Available commands:\nPrivate Message: /msg username 'message'\nMessage to Everyone: 'message'\nSend File to Everyone: /file all 'File Name' \nSend Private File: /file username 'File Name'\nList of Online Users: /list\n");

            } else if (decoded_message.code == 100) {
                printf("Invalid Command\n");
                fflush(stdout);

            }
        }
        fflush(stdout);
    }
    return 0;
}