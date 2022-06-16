% Initial State
estado_inicial(e(0, 3, 1, 2)).

% Terminal
terminal(e(0, 0, 0, 0)).

% Utility Function
% If the depth is odd, the computer lost and the player wons
valor(E, -1, P) :- terminal(E), R is P mod 2, R = 1.
% If the depth is even, the computer wons and the player lost
valor(E, 1, P) :- terminal(E), R is P mod 2, R = 0. 

% op1(E, Jogada, Es)
% E  -> Current State
% Es -> Next State 
op1(e(N1,N2,N3,N4),ret(1,N),e(N11,N2,N3,N4)) :- number(1,N), N11 is N1 - N, N11 >= 0.

op1(e(N1,N2,N3,N4),ret(2,N),e(N1,N22,N3,N4)) :- number(1,N), N22 is N2 - N, N22 >= 0.

op1(e(N1,N2,N3,N4),ret(3,N),e(N1,N2,N33,N4)) :- number(1,N), N33 is N3 - N, N33 >= 0.

op1(e(N1,N2,N3,N4),ret(4,N),e(N1,N2,N3,N44)) :- number(1,N), N44 is N4 - N, N44 >= 0.

maximum(7).


% Generates all numbers in backtracking until the max is reached
number(N,N).
number(L,N1) :- maximum(M), L<M, L1 is L+1, number(L1,N1).