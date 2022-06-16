% estado -> e(coluna, linha)
estado_inicial(e(2, 7)).
estado_final(e(5, 1)).

% restrições
bloqueada(e(1,2)).
bloqueada(e(3,1)).
bloqueada(e(3,2)).
bloqueada(e(4,4)).
bloqueada(e(4,5)).
bloqueada(e(4,6)).
bloqueada(e(7,2)).

% operadores de estado -> op(estado_atual, operador, estado_seguinte, custo)
op(e(X, Y), cima, e(X, Y1), 1) :-
    Y > 1,
    Y1 is Y - 1,
    \+ bloqueada(e(X, Y1)).

op(e(X, Y), direita, e(X1, Y), 1) :-
    X < 7,
    X1 is X + 1,
    \+ bloqueada(e(X1, Y)).

op(e(X, Y), baixo, e(X, Y1), 1) :-
    Y < 7,
    Y1 is Y + 1,
    \+ bloqueada(e(X, Y1)).

op(e(X, Y), esquerda, e(X1, Y), 1) :-
    X > 1,
    X1 is X - 1,
    \+ bloqueada(e(X1, Y)).

% heurísticas
manhattan(e(A,B),C):-
	estado_final(e(X,Y)),
	X1 is abs(A - X), 
 	Y1 is abs(B - Y),
	C is X1 + Y1.

euclidiana(e(Ix,Iy),SOMA):-
	estado_final(e(Fx,Fy)),
	Dx is abs(Ix - Fx), 
 	Dy is abs(Iy - Fy),
	SOMA is round(sqrt(Dy ** 2 + Dx ** 2)).

h1(A, B) :- manhattan(A, B).
h2(A, B) :- euclidiana(A, B).

