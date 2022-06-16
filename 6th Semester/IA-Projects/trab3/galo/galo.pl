% Each position can have:
% x -> player
% o -> ai entity
% v -> empty position

% Initial state
estado_inicial :- ([[v, v, v],
                   [v, v, v],
                   [v, v, v]]).

% Terminal
terminal(B) :- lines(B, _).
terminal(B) :- columns(B, _).
terminal(B) :- diagonals(B, _).
terminal(B) :- full_board(B).

lines([[X, X, X], [_, _, _], [_, _, _]], X) :- X \= v. % first line
lines([[_, _, _], [X, X, X], [_, _, _]], X) :- X \= v. % second line
lines([[_, _, _], [_, _, _], [X, X, X]], X) :- X \= v. % third line

columns([[X, _, _], [X, _, _], [X, _, _]], X) :- X \= v. % first column
columns([[_, X, _], [_, X, _], [_, X, _]], X) :- X \= v. % second column
columns([[_, _, X], [_, _, X], [_, _, X]], X) :- X \= v. % third column

diagonals([[X, _, _], [_, X, _], [_, _, X]], X) :- X \= v. % diagonal -> 1,1 2,2 3,3
diagonals([[_, _, X], [_, X, _], [X, _, _]], X) :- X \= v. % diagonal -> 3,1 2,2 1,3

full_board([L1, L2, L3]) :- \+ member(v, L1),
                            \+ member(v, L2),
                            \+ member(v, L3).

% Utility function (1->win, 0->draw, -1->lose)
valor(B, 1) :- lines(B, o).
valor(B, 1) :- columns(B, o).
valor(B, 1) :- diagonals(B, o).

valor(B, -1) :- lines(B, o).
valor(B, -1) :- columns(B, o).
valor(B, -1) :- diagonals(B, o).

valor(_, 0).

% Operation(State, Player, Play, Next State)
op1(E, J, play(X, Y), En) :- play_empty(E, J, X, Y, En).

play_empty([[v, P12, P13],
            [P21, P22, P23],
            [P31, P32, P33]],
           J,
           1, 1,
           [[J, P12, P13],
            [P21, P22, P23],
            [P31, P32, P33]]).
play_empty([[P11, v, P13],
            [P21, P22, P23],
            [P31, P32, P33]],
           J,
           1, 2,
           [[P11, J, P13],
            [P21, P22, P23],
            [P31, P32, P33]]).
play_empty([[P11, P12, v],
            [P21, P22, P23],
            [P31, P32, P33]],
           J,
           1, 3,
           [[P11, P12, J],
            [P21, P22, P23],
            [P31, P32, P33]]).
play_empty([[P11, P12, P13],
            [v, P22, P23],
            [P31, P32, P33]],
           J,
           2, 1,
           [[P11, P12, P13],
            [J, P22, P23],
            [P31, P32, P33]]).
play_empty([[P11, P12, P13],
            [P21, v, P23],
            [P31, P32, P33]],
           J,
           2, 2,
           [[P11, P12, P13],
            [P21, J, P23],
            [P31, P32, P33]]).
play_empty([[P11, P12, P13],
            [P21, P22, v],
            [P31, P32, P33]],
           J,
           2, 3,
           [[P11, P12, P13],
            [P21, P22, J],
            [P31, P32, P33]]).
play_empty([[P11, P12, P13],
            [P21, P22, P23],
            [v, P32, P33]],
           J,
           3, 1,
           [[P11, P12, P13],
            [P21, P22, P23],
            [J, P32, P33]]).
play_empty([[P11, P12, P13],
            [P21, P22, P23],
            [P31, v, P33]],
           J,
           3, 2,
           [[P11, P12, P13],
            [P21, P22, P23],
            [P31, J, P33]]).
play_empty([[P11, P12, P13],
            [P21, P22, P23],
            [P31, P32, v]],
           J,
           3, 3,
           [[P11, P12, P13],
            [P21, P22, P23],
            [P31, P32, J]]).