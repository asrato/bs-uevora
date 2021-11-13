#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>

#define MAX 2
#define PORT 1337
#define SA struct sockaddr

// prints the board
void print_ttt(char ttt[]) {
  	int i, l = 0;

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
    	printf("[X] Your move? -> ");
    	fgets(buf, 5, stdin);
    	move = buf[0];

    	if (!try_play(ttt, move - '1', 0)) {
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


// function designed for game communication between client and server
void func(int sockfd, char ttt[]) {
	char buff[MAX];
	int n;
	printf("\n");
	print_ttt(ttt);
	for (;;) {
		// player
		bzero(buff, sizeof(buff));
		
		buff[0] = get_move(ttt);

		int client_play = buff[0] - 49;
		print_ttt(ttt);

		write(sockfd, buff, sizeof(buff));

		// vencedor?
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

		bzero(buff, sizeof(buff));

		read(sockfd, buff, sizeof(buff));

		int server_play = buff[0] - 49;
		int try = try_play(ttt, server_play, 1);
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

	}
}

// main function
int main() {
	// board initialization
	char tictactoe[9] = {2,2,2,2,2,2,2,2,2};

	int sockfd, connfd;
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
	servaddr.sin_addr.s_addr = inet_addr("127.0.0.1");
	servaddr.sin_port = htons(PORT);

	// connect the client socket to server socket
	if (connect(sockfd, (SA*)&servaddr, sizeof(servaddr)) != 0) {
		printf("Connection with the server failed...\n");
		exit(0);
	}
	else
		printf("Connected to the server..\n");

	// game communication function
	func(sockfd, tictactoe);

	// after the game close the socket
	close(sockfd);

	return 1;
}
