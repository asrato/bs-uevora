% estado_inicial(Estado)
% estado_final(Estado)

% :- dynamic(fechado/1).
:- dynamic(maxNL/1).
:- dynamic(nos/1).

maxNL(0).
nos(0).

limpa:- % retractall(fechado(A)),
    retractall(maxNL(A)),
    retractall(nos(A)),
    asserta(maxNL(0)),
    asserta(nos(0)).
  
inc:- retract(nos(N)), N1 is N+1, asserta(nos(N1)).


actmax(N):- maxNL(N1), N1 >= N,!.
actmax(N):- retract(maxNL(_N1)), asserta(maxNL(N)).

% representacao dos operadores
% op(Eact,OP,Eseg,Custo)
% representacao dos nos
% no(Estado,no_pai,OperadorCusto,Profundidade)

pesquisa(Problema,Alg):-
  consult(Problema),
  limpa,
  estado_inicial(S0),
  pesquisa(Alg,[no(S0,[],[],0,0)],Solucao),
  nos(Ns),maxNL(NL),
  write(nos(visitados(Ns),lista(NL))),
  escreve_seq_solucao(Solucao).

pesquisa(iterativa,Ln,Sol):- pesquisa_it(Ln,Sol,1).
pesquisa(largura,Ln,Sol):- pesquisa_largura(Ln,Sol).
pesquisa(profundidade,Ln,Sol):- pesquisa_profundidade(Ln,Sol).

expande(no(E,Pai,Op,C,P),L):-
    findall(no(En,no(E,Pai,Op,C,P),Opn,Cnn,P1),
        (op(E,Opn,En,Cn),
            P1 is P+1,
            Cnn is Cn+C),
        L).

expandePl(no(_E,_Pai,_Op,_C,P),[],Pl):- Pl =< P, !.
expandePl(no(E,Pai,Op,C,P),L,_):-
    findall(no(En,no(E,Pai,Op,C,P),Opn,Cnn,P1),
        (op(E,Opn,En,Cn),
            P1 is P+1,
            Cnn is Cn+C),
        L).

insere_fim([],L,L).
insere_fim(L,[],L).
insere_fim(R,[A|S],[A|L]):- insere_fim(R,S,L).

% pesquisa em largura
% pesquisa_largura([],_):- !,fail.
pesquisa_largura([no(E,Pai,Op,C,P)|_],no(E,Pai,Op,C,P)):-
    inc,
    estado_final(E).
pesquisa_largura([E|R],Sol):-
    inc, %asserta(fechado(E)),
    expande(E,Lseg), %esc(E),
    insere_fim(Lseg,R,Resto),
    length(Resto,N),
    actmax(N),
    pesquisa_largura(Resto,Sol).

% pesquisa em profundidade
pesquisa_profundidade([no(E,Pai,Op,C,P)|_],no(E,Pai,Op,C,P)):-
    inc,
    estado_final(E).
pesquisa_profundidade([E|R],Sol):-
    inc, %asserta(fechado(E)),
    expande(E,Lseg), %esc(E),
    insere_fim(R,Lseg,Resto),
    length(Resto,N),
    actmax(N),
    pesquisa_profundidade(Resto,Sol).

% pesquisa iterativa
pesquisa_it(Ln,Sol,P):- pesquisa_pLim(Ln,Sol,P).
pesquisa_it(Ln,Sol,P):- P1 is P+1, pesquisa_it(Ln,Sol,P1).

pesquisa_pLim([E|R],Sol,Pl):-
    inc,
    expandePl(E,Lseg,Pl), %esc(E),
    insere_fim(R,Lseg,Resto), 
    length(Resto,N),
    actmax(N),
    pesquisa_pLim(Resto,Sol,Pl).
pesquisa_pLim([no(E,Pai,Op,C,P)|_],no(E,Pai,Op,C,P),_):- 
    estado_final(E).

% predicados de escrita
escreve_seq_solucao(no(E,Pai,Op,Custo,Prof)):-
    write(custo(Custo)),
    nl,
    write(profundidade(Prof)),
    nl,
    escreve_seq_accoes(no(E,Pai,Op,_,_)).
    
escreve_seq_accoes([]).
escreve_seq_accoes(no(E,Pai,Op,_,_)):-
    escreve_seq_accoes(Pai),
    write(e(Op,E)),
    nl.

esc(A):- write(A), nl.