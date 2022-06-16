play :- estado_inicial(Ei),
        play(Ei).

get_play(X, Y) :- write('Line: '),
                  read(X),
                  X > 0,
                  X <= 3,
                  write('Column: '),
                  read(Y),
                  Y > 0,
                  Y <= 3.

ai_play(Ei) :- terminal(Ei), write('Player Won!').
ai_play(Ei) :- minmax_decidir(Ei, play(X, Y)),
               write(play(X, Y)), nl,
               joga_vazio(Ei, o, X, Y, En),
               play(En).

play(Ei) :- terminal(Ei), print_board(Ei), write('AI Entity Won!').
play(Ei) :- printBoard(Ei),
            get_play(X, Y),
            play_empty(Ei, x, X, Y, En),
            print_board(En),
            ai_play(En).

print_board([]).
print_board([H | T]) :-
    write(H), nl,
    print_board(T).

% Minmax
minmax_decidir(Ei, terminou) :- terminal(Ei).
minmax_decidir(Ei, Opf) :-
    findall(Vc-Op, (op1(Ei, o, Op, Es), minmax_valor(Es, Vc, 1)), L),
    escolhe_max(L, Opf).

minmax_valor(Ei, V, _) :-
    terminal(Ei),
    valor(Ei, V).
minmax_valor(Ei, Val, P) :-
    P1 is P + 1, player(P1, J),
    findall(V1, (op1(Ei, J, _, Es), minmax_valor(Es, V1, P1)), V),
    select_value(V, P, Val).

% first player is the AI entity
player(P, x) :- X is P mod 2, X = 0.
player(P, o) :- X is P mod 2, X = 1.

select_value(V, P, Val) :-
    X is P mod 2, X = 0, !,
    get_max(V, Val).
select_value(V, _ Val) :-
    get_min(V, Val).

get_max([A|R], Val) :- get_max(R, A, Val).
get_max([], A, A).
get_max([A|R], X, Val) :- A < X, !, get_max(R, X, Val).
get_max([A|R], _, Val) :- get_max(R, A, Val).

get_min([A|R], Val) :- get_min(R, A, Val).
get_min([], A, A).
get_min([A|R], X, Val) :- A > X, !, get_min(R, X, Val).
get_min([A|R], _, Val) :- get_min(R, A, Val).
