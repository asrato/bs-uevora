# 2048
Final project for the Programming I subject of the Computer Engineering Degree. 

## 1. Introduction
&nbsp;&nbsp;&nbsp;2048 is a puzzle game for one player. Its main goal is to slide pieces numbered in a grid in order to combine them until you create a piece with the number 2048. It's an addictive game and many people spend many hoursplaying this game (instead of working, study or even sleep!).  
&nbsp;&nbsp;&nbsp;Pieces can be moved in four directions: Down, Up, Right and Left. They are combined when they are adjacent and have the same number. In this case, they are replaced for a new piece whose value is the sum of the two initial pieces.

## 2. Project Description
&nbsp;&nbsp;&nbsp;The project consists of developing aaplications to play 2048 in two different modes:  
* In **iterative mode**, the initial grid is filled in two random positions with the number 2 or 4. In each turn:  
  1. the player chooses a direction (B - Down, C - Up, D - Right, and E - Left);
  2. the grid is updated according to the game rules;
  3. a new number 2 or 4 is placed in an empty position.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The game ends when the user chooses F of when it is not possible to combine more pieces; at that line, the total number of pieces combined during the game counting the number of pieces with each number still on the board (in order growing).

* In the **automatic mode**, the initial grud (fully filled) and moves are read of a text file and the program displays the total number of pieces matched during the gameand counting after the last move.  

&nbsp;&nbsp;&nbsp;For example, for the dimension 4 board bellow, we have the following sequence of configurations when the moves are (in order): B, D, and B.  
```
2  2  4  2      -  -  -  -      -  -  -  -      -  -  -  -  
4  2  2  4  =>  2  2  4  2  =>  -  4  4  2  =>  -  -  4  2
2  2  2  2      4  4  2  4      -  8  2  4      -  4  2  4
2  4  2  2      4  4  4  4      -  -  8  8      -  8  8  8
```

## 3. Development
&nbsp;&nbsp;&nbsp;The game must be implemented in C language and be accompanied by a PDF report.  
&nbsp;&nbsp;&nbsp;For the development of the project, assume that the maximum size of the board is 10\*10 squares. It must implement the following functions:  
* `int baixo (int grelha[][], int sz)`: this function updates the grid when choosing B (Down); the function returns the number of combined pieces.
* `int cima (int grelha[][], int sz)`: this function updates the grid when choosing C (Up); the function returns the number of combined pieces.
* `int direita (int grelha[][], int sz)`: this function updates the grid when choosing D (Right); the function returns the number of combined pieces.
* `int esquerda (int grelha[][], int sz)`: this function updates the grid when choosing E (Left); the function returns the number of combined pieces.
* `int jogada (int grelha[][], int sz,char sentido)`: this function executes a play, returning the number of combined pieces; it should use the previous functions.
* `void mostrar (int grelha[][], int sz)`: this function displays the current grid configuration on the screen.  

&nbsp;&nbsp;&nbsp;You must develop a separate program for each operating mode, but re-using the common function to both operating modes. For such it's suggested to use the following files:
* `2048.h`, with all common function protoypes;
* `2048.c`, with the implementation of functions;
* `iterativo.c`, with the iterative mode;
* `automatico.c`, with the automatic mode.

&nbsp;&nbsp;&nbsp;The command `gcc -c 2048.c`generates an object file called `2048.o`.
&nbsp;&nbsp;&nbsp;The command `gcc -p iterativo 2048.o iterativo.o`binds both object files (20480.o and iterativo.o) e creates the executable file "iterativo".

### 3.1. Iterative Mode
&nbsp;&nbsp;&nbsp;This mode implements the "official" game. It works like this:  
1. the user is asked fo the grid size `N`;
2. a grid of size N\*N is displayed on the screen with the digits 2 or 4 in two random positions;
3. iteratively, the player chooses a direction, and the current grid is displayed on the screen plus a new digit (2 or 4) in a free position; the game ends when it's not possible to do any more movements ot when the user chooses F;
4. At that time, the total number of combined pieces and the number of pieces in the final grid of each digit are displayed (see automatic mode);

&nbsp;&nbsp;&nbsp;To generate the digits and the position where they are placed, use the function `int rand()` from the `stdlib.h` library. This function generates a random integer between 0 and `RAND MAX`. The % operator (rest of integer division) is used in the function `rand()`to get values at different intervals.

### 3.2. Automatic Mode
&nbsp;&nbsp;&nbsp;In automatic mode, the initial grid id completely filled, with all the information read from a file. The input information contains:
* a line with grid size N;
* N lines with N digits each;
* a line with the K moves.

&nbsp;&nbsp;&nbsp;The answer displayed on the screen contains the total number of combined pieces and the number of remaining pieces of each number.  
&nbsp;&nbsp;&nbsp;For example, for the file with the following information:
```
4
2 2 4 2
4 2 2 4
2 2 2 2
2 4 2 2
B D B E C
```
should be displayed on the screen
```
Combined pieces: 11
Contagem: 0 2 2 1
```
