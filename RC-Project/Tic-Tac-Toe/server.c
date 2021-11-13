#include <stdio.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>

#define MAX 2
#define PORT 1337
#define SA struct sockaddr

// prints the board
void print_ttt(char ttt[]) {
	int i,l = 0;

  	for (i = 0; i < 9; i++) {
	    if (ttt[i] == 2) printf(" %d ", i+1);
	    else if (ttt[i] == 0) printf (" X ");
	    else if (ttt[i] == 1) printf (" O ");

	    if (i == 2 || i == 5 || i == 8) printf("\n");
	    if (i == 2 || i == 5) printf("---+---+---\n");
	    if (i == 0 || i == 1 || i == 3 || i == 4 || i == 6 || i == 7) printf("|");
  	}

  	printf("\n");
}

// tries to make a play
int try_play(char ttt[], int spot, char mark) {
  	if (ttt[spot] == 2) { // blank spot, can play
    	ttt[spot] = mark;
    	return 1;
  	} else {
   	 	return 0;
  	}
}

// checks if someone won
int check_win(char ttt[]) {
  	int who = -1;

  	if (
      	//lines
      	((who = ttt[0]) == ttt[1] && ttt[1] == ttt[2] && who != 2) ||
      	((who = ttt[3]) == ttt[4] && ttt[4] == ttt[5] && who != 2) ||
      	((who = ttt[6]) == ttt[7] && ttt[7] == ttt[8] && who != 2) ||
      	//columns
      	((who = ttt[0]) == ttt[3] && ttt[3] == ttt[6] && who != 2) ||
      	((who = ttt[1]) == ttt[4] && ttt[4] == ttt[7] && who != 2) ||
      	((who = ttt[2]) == ttt[5] && ttt[5] == ttt[8] && who != 2) ||
      	//diagonals
      	((who = ttt[0]) == ttt[4] && ttt[4] == ttt[8] && who != 2) ||
      	((who = ttt[2]) == ttt[4] && ttt[4] == ttt[6] && who != 2)
      	) {
      	return who;
    } else return -1;
}

// gets a valid move
char get_move(char ttt[]) {
  	char move = 0;
  	char buf[10];

  	while (move < '1' || move > '9') {
    	printf("[O] Your move? -> ");
    	fgets(buf, 5, stdin);
    	move = buf[0];

    	if (!try_play(ttt, move - '1', 1)) {
    	  	printf("Spot taken, choose another!\n");
      		move = 0;
    	}
  	}

  	return move;
}

// checks if is possible to play
int is_possible_to_play(char ttt[]) {
	for (int i = 0; i < 9; i++)
		if (ttt[i] == 2)
			return 1;
	
	return 0;
}

// check if client send a valid play
int is_valid(char ttt[], int spot) {
	if (ttt[spot] == 2 || (spot > 0 && spot < 10)) { // blank spot, can play
    	return 1;
  	} else {
   	 	return 0;
  	}
}

// function designed for game communication between client and server
void func(int sockfd, char ttt[]) {
	char buff[MAX];
	int n;
	printf("\n");

	// infinite loop for chat
	for (;;) {
		// player
		bzero(buff, sizeof(buff));

		// read the message from client and copy it in buffer
		read(sockfd, buff, sizeof(buff));

		// recebe character do cliente
		int client_play = buff[0] - 49;

		bzero(buff, sizeof(buff));

		if (is_valid(ttt, client_play) == 1) {

			int try = try_play(ttt, client_play, 0);
			print_ttt(ttt);

			if (check_win(ttt) == 0) {
				printf("Client won!\n");
				break;
			} else if (check_win(ttt) == 1) {
				printf("Server won!\n");
				break;
			}

			if (is_possible_to_play(ttt) == 0) {
				printf("Game tied!\n");
				break;
			}		
			
			// server
			buff[0] = get_move(ttt);
			int server_play = buff[0] - 49;
			print_ttt(ttt);

			// and send that buffer to client
			write(sockfd, buff, sizeof(buff));
			// vencedor?
			if (check_win(ttt) == 0) {
				printf("Client won!\n");
				break;
			} else if (check_win(ttt) == 1) {
				printf("Server won!\n");
				buff[0] = 'W';
				write(sockfd, buff, sizeof(buff));
				break;
			}

			if (is_possible_to_play(ttt) == 0) {
				printf("Game tied!\n");
				buff[0] = 'D';
				write(sockfd, buff, sizeof(buff));
				break;
			}

		} else {
			buff[0] = 'X';
			write(sockfd, buff, sizeof(buff));
		}
		
	}
}

// main function
int main() {
	// board initialization
	char tictactoe[9] = {2,2,2,2,2,2,2,2,2};

	int sockfd, connfd, len;
	struct sockaddr_in servaddr, cli;

	// socket create and verification
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd == -1) {
		printf("Socket creation failed...\n");
		exit(0);
	}
	else
		printf("Socket successfully created...\n");
	bzero(&servaddr, sizeof(servaddr));

	// assign IP, PORT
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port = htons(PORT);

	// binding newly created socket to given IP and verification
	if ((bind(sockfd, (SA*)&servaddr, sizeof(servaddr))) != 0) {
		printf("Socket bind failed...\n");
		exit(0);
	}
	else
		printf("Socket successfully binded...\n");

	// now server is ready to listen and verification
	if ((listen(sockfd, 5)) != 0) {
		printf("Listen failed...\n");
		exit(0);
	}
	else
		printf("Server listening...\n");
	len = sizeof(cli);

	// accept the data packet from client and verification
	connfd = accept(sockfd, (SA*)&cli, &len);
	if (connfd < 0) {
		printf("Server accept failed...\n");
		exit(0);
	}
	else
		printf("Server accept the client...\n");

	// game communication function
	func(connfd, tictactoe);

	// after the game close the socket
	close(sockfd);

	return 1;
}
