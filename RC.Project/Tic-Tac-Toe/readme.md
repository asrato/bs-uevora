
# TIC-TAC-TOE

*[PT]* ***TIC-TAC-TOE*** entre cliente e servidor, utilizando o protocolo TCP(Transmission Control Protocol).


## Como Compilar e Correr

* **Servidor:**
```
# compilar
gcc server.c -o server
# correr
./server
```

* **Ciente:**
```
# compilar
gcc client-c -o client 
# correr
./client
```

## Servidor e Cliente
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Inicialmente, o servidor tenta criar uma *socket* verificando a sua criação. De seguida, é feita a ligação entre o ip e a porta com a *socket* criada. Por fim, o servidor fica à espera de alguma conexão.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Assim que um cliente se ligar ao mesmo ip e porta, o servidor aceita a sua conexão, dando início a um novo jogo.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Após o começo do jogo, o cliente escolhe uma posição do tabuleiro para jogar, sendo verificada a sua validade e enviada para o servidor. Os tabuleiros do cliente e do servidor são ambos atualizados. De seguida o servidor escolhe uma posição do tabuleiro para jogar, sendo verificada a sua validade e enviada para o cliente. Ambos os tabuleiros são atualizados.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Isto acontece enquanto o jogo for jogável, ou seja, até algum dos jogadores (cliente ou servidor) ganhar ou até todas as posições estiverem ocupadas.

## Anti-cheating
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; De modo a impedir a criação de clientes *"batoteiros"*, foram utilizadas algumas estratégias:  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **->** caso o cliente envie uma jogada inválida (posição preenchida ou outro caractere não correspondente a uma posição), o servidor envia um *X* para o cliente;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **->** se o servidor ganhar, este envia um *W* para o cliente;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **->** caso o tabuleiro fique cheio e o jogo termine empatado, o servidor envia um *D* para o cliente.

---

*[EN]* ***TIC-TAC-TOE*** between client and server, using TCP(Transmission Control Protocol).


## How to Compile and Run

* **Server:**
```
# compilar
gcc server.c -o server
# correr
./server
```

* **Cient:**
```
# compilar
gcc client-c -o client 
# correr
./client
```

## Server and Client
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Initially, the server tries to create a *socket* verifying its existance. Then, a connection between the IP, PORT and the created socket is established. Lastly, the server ends up waiting for a connection.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; As soon as a client connects to the right IP and PORT, the server accepts his connection, starting a new game.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; After the beginning of the game, the client chooses a board position to play, being verified its validity and, after that, sent to the server. Both client and server boards are refreshed. Then, the server chooses a board position to play, being verified its validity and sent to the client. Both client and server boards are refreshed.   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; This whole process happens while the game is playable, i. e., until one of the players (client or server) won or until all board positions are occupied.  

## Anti-cheating
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; In order to prevent the creation of cheating-clients, some strategies were used.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **->** in case of the client sends an invalid play (invalid position or invalid character), the server sends an *X* to the client;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **->** if the server wins the game, he sends a *W* to the client;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **->** in case of the board becomes full and the game ends tied, the server sends a *D* to the client.  

---

## License
[André Rato](https://github.com/andresrato9) for [Redes de Computadores](https://www.moodle.uevora.pt/2021/course/view.php?id=1691) @ 2021
