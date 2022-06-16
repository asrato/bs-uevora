:- dynamic(fechado/1).
:- dynamic(maxNL/1).
:- dynamic(nos/1).

maxNL(0).
nos(0).

limpa:- 
    retractall(fechado(A)),
    retractall(maxNL(A)),
    retractall(nos(A)),
    asserta(maxNL(0)),
    asserta(nos(0)).

inc:-
    retract(nos(N)),
    N1 is N+1,
    asserta(nos(N1)).

actmax(N):- maxNL(N1), N1 >= N,!.
actmax(N):- retract(maxNL(_N1)), asserta(maxNL(N)).

% estado_inicial(Estado)
% estado_final(Estado)

% representacao dos operadores
% op(Eact,OP,Eseg,Custo)
% representacao dos nos
% no(Estado,no_pai,Operador,Custo,H+C,Profundidade)
pesquisa(Problema,Alg):-
    consult(Problema),
    estado_inicial(S0),
    limpa,
    pesquisa(Alg,[no(S0,[],[],0,1,0)],Solucao),
    escreve_seq_solucao(Solucao),
    nos(Ns),
    maxNL(NL),
    write(nos(visitados(Ns),lista(NL))).

pesquisa(a,E,S):- pesquisa_a(E,S).
pesquisa(g,E,S):- pesquisa_g(E,S).

expande(no(E,Pai,Op,C,HC,P),L):- 
    findall(no(En,no(E,Pai,Op,C,HC,P),Opn,Cnn,HCnn,P1),
        (op(E,Opn,En,Cn), 
            \+ fechado(no(En,_,_,_,_,_)),
            P1 is P+1,
            Cnn is Cn+C,
            h2(En,H), 
            HCnn is Cnn+H),
    L).

expande_g(no(E,Pai,Op,C,HC,P),L):-
    findall(no(En,no(E,Pai,Op,C,HC,P),Opn,Cnn,H,P1),
        (op(E,Opn,En,Cn),
            \+ fechado(no(En,_,_,_,_,_)),
            P1 is P+1, 
            Cnn is Cn+C, 
            h2(En,H)), 
    L).

menor_no(no(_,_,_,_,N,_), no(_,_,_,_,N1,_)):- N < N1.

insereE_ord(A,[],[A]).
insereE_ord(A,[A1|L],[A,A1|L]):- menor_no(A,A1),!.
insereE_ord(A,[A1|L], [A1|R]):- insereE_ord(A,L,R).

insere_ord([],L,L).
insere_ord([A|L],L1,L2):-
    insereE_ord(A,L1,L3),
    insere_ord(L,L3,L2).

% pesquisa a*
% pesquisa_a([],_):- !,fail.
pesquisa_a([no(E,Pai,Op,C,HC,P)|_],no(E,Pai,Op,C,HC,P)):-
    estado_final(E),
    inc.
pesquisa_a([E|R],Sol):-
    inc,
    asserta(fechado(E)),
    expande(E,Lseg), %esc(E),
    insere_ord(Lseg,R,Resto),
    length(Resto,N), 
    actmax(N),
    pesquisa_a(Resto,Sol).

% pesquisa gready
% pesquisa_g([],_):- !,fail.
pesquisa_g([no(E,Pai,Op,C,HC,P)|_],no(E,Pai,Op,C,HC,P)):-
    estado_final(E).
pesquisa_g([E|R],Sol):- 
    inc, 
    asserta(fechado(E)), 
    expande_g(E,Lseg), %esc(E),
    insere_ord(Lseg,R,Resto),
    length(Resto,N),
    actmax(N),
    pesquisa_g(Resto,Sol).

% predicados de escrita
escreve_seq_solucao(no(E,Pai,Op,Custo,_HC,Prof)):-
    write(custo(Custo)),
    nl,
    write(profundidade(Prof)),
    nl,
    escreve_seq_accoes(no(E,Pai,Op,_,_,_)).

escreve_seq_accoes([]).
escreve_seq_accoes(no(E,Pai,Op,_,_,_)):-
    escreve_seq_accoes(Pai),
    write(e(Op,E)),
    nl.

esc(A):- write(A), nl.