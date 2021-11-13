# Fãs de Policiais
First project for the Databases subject of the Computer Engineering Degree. 

## Objective
&nbsp;&nbsp;&nbsp;It's intended to develop a database to manage information from a social network of detective books fans. TO manage the network it's required to represent data about: members, books, book genres, friends and likes.  
&nbsp;&nbsp;&nbsp;About the members it's intended to register: name, birth country, city where they live and birth date. Members have friends and it's in relationship of `friends` that friendship is represented between two members (this relationship mustn't contain symmetrical tuples).  
&nbsp;&nbsp;&nbsp;For each member, the books hey read and liked are represented in the relationship `likes`. Information about the books on the network is represented in the `book` relation that representes a book with the title and ISBN. In the `gender` relation, they represent themselves the genre (detective, drama, romance, ...) of each book. All books have one or more genre.  
&nbsp;&nbsp;&nbsp;The authorsip list has information about the author, the books, etc.  
&nbsp;&nbsp;&nbsp;The `author` relationship represents the information about each author: name, a unique code, country and where was born.  

```
membro(Nome, IdMemb, Pais, Cidade, DataNasc)
amigo(IdMemb, IdMemb)
gosta(IdMemb,ISBN)
livro(ISBN,Titulo)
genero(ISBN,Genero)
autoria(ISBN,Coda)
autor(Coda,Nome,Pais)
```
