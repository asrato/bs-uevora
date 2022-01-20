:- use_module(library(clpfd)).
%UTILS

%ESCREVER_LINHA_VAZIA
escreve(File):- append(File), write(' '), told.
 
%REMOVER_ULTIMO_ELEMENTO_DE_LISTA
withoutLast([_], []).
withoutLast([X|Xs], [X|WithoutLast]) :- withoutLast(Xs, WithoutLast).
 
%LER_TABULEIRO
readN(File, Result, Strs, Pos):-
    open(File, read, Stream),
    read_line_to_codes(Stream, Line),
    atom_codes(R, Line),
    atom_number(R, Result),
    lerStreams(Stream, Strs, Result),
    N1 is Result + 99,
    lerPosicoes(Stream, Pos, N1),
    close(Stream), !.
 
%LER_STREAMS
lerStreams(Stream, Lista, N):- lerLinha(Stream, Lista, N), !.
 
%LER_POSICOES
lerPosicoes(Stream, Lista, N) :- lerLinha(Stream, Lista, N), !.
 
%LER_N_LINHAS
lerLinha(_, _, 0).
lerLinha(Stream, [H|T], N):-
    read_line_to_codes(Stream, Line),
    (at_end_of_stream(Stream) -> !;
    atom_codes(A, Line),
    atomic_list_concat(As, ' ', A),
    maplist(atom_number, As, H),
    N1 is N-1, lerLinha(Stream, T, N1)).
 
%LER_FICHEIRO
carregaFicheiro(File, Streams, Predefinidas) :-
        escreve(File),
        readN(File, _, Streams, X),
        withoutLast(X, Predefinidas), !.
 
%IMPRIMIR_MATRIZ        
imprimeMatriz([], _, _).
imprimeMatriz([H|T], N, 0):- writeln(" "), imprimeMatriz([H|T], N, N).
imprimeMatriz([H|T], N, In):- write(H), write(" "), In1 is In-1, imprimeMatriz(T, N, In1).
 
%COLOCAR_POSICOES_INICIAIS
colocaNumero(Matriz, [Linha,Coluna,Valor]) :- nth1(Linha, Matriz, Linha1), element(Coluna, Linha1, Valor).
 
%ENCONTRAR_POR_INDICE 
procurarPorIndice([], _Matriz, Xs, Xs).
procurarPorIndice([I|Is], Matriz, Xs0, [XI|Xs]) :- element(I,Matriz,XI), procurarPorIndice(Is, Matriz, Xs0, Xs).
 
%ASSEGURAR_QUE_TODAS_OS_NUMEROS_DAS_STREAMS_SAO_DIFERENTES
streams(Matriz,Streams,S) :- length(Matriz, N), findall(Indice, (between(1,N,Indice), element(Indice,Streams,S)), Indices), procurarPorIndice(Indices, Matriz, [], Valores), all_different(Valores).
 
strimko(File) :-
        %LER_FICHEIRO
        carregaFicheiro(File,Streams,Predefinidas), 

        length(Streams, N),
        length(X, N),
        %CRIAR_TABULEIRO_E_VERIFICAR_LINHAS_E_COLUNAS
        maplist(same_length(X), X),
        append(X, Vs), Vs ins 1..N,
        maplist(all_distinct, X),
        transpose(X, Columns),
        maplist(all_distinct, Columns),
        flatten(X,Vars),
        flatten(Streams, Sflatten),
        maplist(colocaNumero(X),Predefinidas),
        numlist(1,N,List),
        %VERIFICAR_STREAMS 
        maplist(streams(Vars,Sflatten), List),
        %POPULAR_TABULEIRO   
        labeling([], Vars),
        imprimeMatriz(Vars, N, N).
 
%CORRER_PROGRAMA
go(File) :- strimko(File), !.
