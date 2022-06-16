% estado -> p(agente-coluna, agente-linha, caixa-coluna, caixa-linha)
estado_inicial(p(2, 7, 2, 6)).
estado_final(p(_, _, 5, 1)).

% restrições
bloqueada(p(1,2)).
bloqueada(p(3,1)).
bloqueada(p(3,2)).
bloqueada(p(4,4)).
bloqueada(p(4,5)).
bloqueada(p(4,6)).
bloqueada(p(7,2)).

lim(X, Y) :- 
    X =< 7,
    X >= 1,
    Y =< 7,
    Y >= 1.

iguais(A, A).

% operadores de estado -> op(estado_atual, operador, estado_seguinte, custo)
op(p(X, Y, P, Q), cima, p(X, Y1, P, Q1), 1) :-
    Y1 is Y - 1,
    (iguais(p(X, Y1), p(P, Q)) -> 
        (
            Q1 is Q - 1,
            lim(P, Y1),
            \+ bloqueada(p(P, Q1))
        );
        (
            Q1 is Q,
            lim(X, Y1),
            \+ bloqueada(p(X, Y1))
        )
    ).
op(p(X,Y,P,Q),direita,p(X1,Y,P1,Q),1) :-
    X1 is X+1,
    (iguais(p(X1,Y),p(P,Q)) ->
        (
            P1 is P+1,
            lim(P1,Q),
            lim(X1,Y),
            \+ bloqueada(p(P1,Q))
        );
        (
            P1 is P,
            lim(X1,Y),
            \+ bloqueada(p(X1,Y))
        )
    ).
op(p(X,Y,P,Q),baixo,p(X,Y1,P,Q1),1) :-
    Y1 is Y+1,
    (iguais(p(X,Y1),p(P,Q)) ->
        (
            Q1 is Q+1,
            lim(P,Q1),
            lim(X,Y1),
            \+ bloqueada(p(P,Q1))
        );
        (
            Q1 is Q,
            lim(X,Y1),
            \+ bloqueada(p(X,Y1))
        )
    ).
op(p(X,Y,P,Q),esquerda,p(X1,Y,P1,Q),1) :-
    X1 is X-1,
    (iguais(p(X1,Y),p(P,Q)) ->
        (
            P1 is P-1,
            lim(P1,Q),
            lim(X1,Y),
            \+ bloqueada(p(P1,Q))
        );
        (
            P1 is P,
            lim(X1,Y),
            \+ bloqueada(p(X1,Y))
        )
    ).

% heurística
dist_estados(p(_,_,Ix,Iy),SOMA):-
	estado_final(p(_,_,Fx,Fy)),
	Dx is abs(Ix - Fx), 
 	Dy is abs(Iy - Fy),
	SOMA is Dx + Dy.

dist_y(p(_,_, _,Iy),SOMA):-
	estado_final(p(_,_,_,Fy)),
	Dy is abs(Iy - Fy), 
	SOMA is Dy.

h1(A, B) :- dist_estados(A, B).
h2(A, B) :- dist_y(A, B).