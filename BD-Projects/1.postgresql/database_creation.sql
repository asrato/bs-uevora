create database Fas_de_Policiais with
	owner = admin
	encoding = 'UTF8'
	connection limit = -1;
	

create table membro (
	Nome varchar(255),
	IdMemb varchar(50),
	Pais varchar(50),
	Cidade varchar(50),
	DataNasc date,

    primary key (IdMemb)
);


create table amigo (
	IdMemb1 varchar(50),
	IdMemb2 varchar(50),

	foreign key (IdMemb1) references membro(IdMemb) on delete restrict,
	foreign key (IdMemb2) references membro(IdMemb) on delete restrict,

	primary key(IdMemb1, IdMemb2)
);


create table livro (
	ISBN char(17),
	Titulo varchar(255),

    primary key (ISBN)
);


create table gosta (
	IdMemb varchar(50),
	ISBN char(17),

	foreign key (IdMemb) references membro on delete restrict,
	foreign key (ISBN) references livro on delete restrict,

	primary key (IdMemb, ISBN) 
);


create table genero (
	ISBN char(17),
	Genero varchar(20),

	foreign key (ISBN) references livro on delete restrict,

	primary key (ISBN, Genero)
);


create table autor (
	CodA char(3),
	Nome varchar(255),
	Pais varchar(50),
    primary key (CodA)
);


create table autoria (
	ISBN char(17),
	CodA char(3),

	foreign key (ISBN) references livro on delete restrict,
	foreign key (CodA) references autor on delete restrict,
    
	primary key (ISBN, CodA)
);	
