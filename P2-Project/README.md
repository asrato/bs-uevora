# (In)valid Queens
Final project for the Programming II subject of the Computer Engineering Degree.

## 1. Introduction
&nbsp;&nbsp;&nbsp;The eight queens puzzle consists of placing eight queens on a chessboard none of which can be captured. In this case the queens are said to be in a valid configuration. Otherwise, that is, if any queen can capture another, you have an invalid configuration. For example, for an 8x8 board with 8 queens, the left setting is valid, while the one on the right is not  
![image](https://user-images.githubusercontent.com/58145573/139350021-62b2e633-8631-498a-af65-ddf8f2b9a65a.png) 
![image](https://user-images.githubusercontent.com/58145573/139349998-d2e8a86a-a3a9-401b-96d3-70c5bdbe36ee.png)  

## 2. Validator
&nbsp;&nbsp;&nbsp;Implement a program (called "validador") that check whether a diven configuration is valid. Your program must be implement by a class called `Validador`.  
&nbsp;&nbsp;&nbsp;The configuration is read from `System.in`and is given as a String of length 64 fromed only by the characters `D`of `-`, which results from concatenating the representations of the eight lines. *Strings with spaces must be accepted*, which will be immediately ignored, so that - for example - the input can be given as a line of 64 characters, or as a line with 8 blocks of 8 characters, separated by spaces.  
&nbsp;&nbsp;&nbsp;Your program should:  
* Accept *arguments on command line*, i.e. must look at the parameter `String[]` passado ao método `public static main void (String[] args)`.
* Read configuration from `System.in`. In varaint 1B you should continue reading settings until the end of the stream.  

### 2.1. Variant 1A: Single Validator
&nbsp;&nbsp;&nbsp;If there are no arguments (i.e. `args.length == 0`), the program should write a single line with the text `VALID` or `INVALID` according to the correspondent case.  
&nbsp;&nbsp;&nbsp;In this case, the program is excepted to read exactly on and only one configuration, for example:  
```
                               Input                             | Output
-----------------------------------------------------------------|---------
-D---------D---------D---------D--D-----D-------------D-----D--- |  VALID
-DD------------------D--------D---D------D------------D-------D- | INVALID
```  

### 2.2. Variant 1B: Filter Validator
&nbsp;&nbsp;&nbsp;If the program is called with an argument equal to the string "filter" this should repeatedly read settings from `System.in` and write in `System.out` only those that are valid, in the simplest format (one line of 64 characters per 8x8 boards configuration).  
&nbsp;&nbsp;&nbsp;Your program must acceept the following production options:  
* `random M Q N`: produces N random configurations of Q queens on boards of dimension MxM; M, Q and N are integers; you should check if `Q <= M`.  
* `all M(bonus)`: produces all possible boards of size MxM, with M queens.  

## 3. Resolution Standards
&nbsp;&nbsp;&nbsp;All classes must be defined without package.  
&nbsp;&nbsp;&nbsp;Your resolution of program 1 must meet the following conditions:  
* Implement the `Linha`, `Coluna`, `DiagonalAscendente`, `DiagonalDescendente` and `Tabuleiro` public classes;  
* Implement an abstract class, `Peca`, to describe whats in a position; this class must have at least two concrete subclasses, `Nada` and `Rainha`;  
* The numbering of the position of the pieces follows the assumption of coordinates (0,0) are in the upper left corner.  

&nbsp;&nbsp;&nbsp;Additionally, compliance with the following aspects will be valued:  
* `Peca` must have the methods and instance variables:  
   * `Peca(Tabuleiro tab, int linha, int coluna)`: constructor for a new `Peca` inside of the `Tabuleiro tab`, which will be directly positioned in the indicated row and column, and the `Peca` contained in that position, inside the board, will be updated;  
   * `int linha()`: method that returns the row where it is positioned;  
   * `int coluna()`: method that returns the column where it is positioned;  
   * `boolean podeIrPara(int linha, int coluna)`: method that verifies if a `Peca` can move to the `linha`/`coluna` position;  
   * `final boolean ataca(Peca vitima) { return podeIrPara(vitima.linha(), vitima.coluna()); }`: method which indicates whter the piece which it applies can attack the one indicated as argument; this method must be defined and impemented in the abstract class `Peca`;  
   * `boolean vazia()`: method that indicates wheter the `Peca` is `Nada` or other class.  
* `Tabuleiro` must have the methods:  
   * `Tabuleiro(String repr)`: constructor that has as argument a string representing a board configuration; it's assumed that `repr` has length MxM, where M is the number of rows and columns;  
   * `Peca peca(int linha, int coluna)`: method that returns the `Piece` that is in that position;  
   * `boolean ameacada(int linha, int coluna):` method that indicates if the position `linha`x`coluna` is threatened;  
   * `Linha linha (int linha)`: method that returns the "linha" `Linha`;  
   * `Coluna coluna (int coluna)`: method that returns the "coluna" `Coluna`;  
   * `DiagonalAscendente diagonalAscendente(int linha, int coluna)`: method that returns the ascending diagonal that passes through the `linha`x`coluna` position; 
   * `DiagonalDescendente diagonalDescendente(int linha, int coluna)`: method that returns the descending diagonal that passes through the `linha`x`coluna` position.  
* `Linha`, `Coluna`, `DiagonalAscendente` and `DiagonalDescendente` classes should implement `Fila` interface that requires the following methods:  
   * `int comprimento()`: return the size of the `Fila`;  
   * `int pecas()`: return the number of pieces (i.e. the number of occupied positions) in that `Fila`;  
   * `Peca peca(int pos) throws IndexOutOfBoundsException`: returns the `Peca` type that is in that position; if the position in invalid, throws an exception `IndexOutOfBoundsExcpetion`. 
