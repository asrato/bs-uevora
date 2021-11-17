--1.
    --membro(Nome, IdMemb, Pais, Cidade, DataNasc):
        --» Chaves Primárias: 		    IdMemb;
        --» Chaves Candidatas:		    IdMemb;
        --» Chaves Estrangeiras:		Não tem;

    --amigo(IdMemb1, IdMemb2):
        --» Chaves Primárias:		    (IdMemb1, IdMemb2);
        --» Chaves Candidatas:		    (IdMemb1, IdMemb2);
        --» Chaves Estrangeiras:		IdMemb1, IdMemb2;

    --livro(ISBN, Titulo):
        --» Chaves Primárias:		    ISBN;
        --» Chaves Candidatas:		    ISBN;
        --» Chaves Estrangeiras:		Não tem;

    --gosta(IdMemb, ISBN):
        --» Chaves Primárias:		    (IdMemb, ISBN);
        --» Chaves Candidatas:		    (IdMemb, ISBN);
        --» Chaves Estrangeiras:		IdMemb, ISBN;

    --genero(ISBN, Genero):
        --» Chaves Primárias:		    (ISBN, Genero);
        --» Chaves Candidatas:		    (ISBN, Genero);
        --» Chaves Estrangeiras:		ISBN;

    --autoria(ISBN, CodA):
        --» Chaves Primárias:		    (ISBN, CodA);
        --» Chaves Candidatas:		    (ISBN, CodA);
        --» Chaves Estrangeiras:		ISBN, CodA;

    --autor(CodA, Nome, Pais):
        --» Chaves Primárias:		    CodA;
        --» Chaves Candidatas:		    CodA;
        --» Chaves Estrangeiras:		Não tem;


--2.
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


--3.
    --a)
    insert into membro values ('Darwin Nunez', 'ogoleador', 'Uruguai', 'Artigas', '1999-06-24');
    insert into membro values ('Rafael Silva', 'oleitor', 'Portugal', 'Vila Franca de Xira', '1993-05-17');
    insert into membro values ('Odisseas Vlachodimos', 'oguardiao', 'Alemanha', 'Stuttgart', '1994-04-26');
    insert into membro values ('Nicolas Otamendi', 'oimpostor', 'Argentina', 'Buenos Aires', '1988-02-12');
    insert into membro values ('Jan Vertonghen', 'omascarilha', 'Bélgica', 'Saint Niklaas', '1987-04-24');
    insert into membro values ('Andre Almeida', 'olesionado', 'Portugal', 'Loures', '1990-09-10');
    insert into membro values ('Alenjandro Grimaldo', 'omosqueteiro', 'Espanha', 'Valencia', '1995-09-20');
    insert into membro values ('Julian Weigl', 'oaziado', 'Alemanha', 'Bad Aibling', '1995-09-08');
    insert into membro values ('Luis Fernandes', 'ozarolho', 'Portugal', 'Bragança', '1989-10-06');
    insert into membro values ('Gabriel Pires', 'opassador', 'Brasil', 'Rio de Janeiro', '1993-09-18');
    insert into membro values ('Luca Waldschimdt', 'olouro', 'Alemanha', 'Siegen', '1996-05-19');
    insert into membro values ('Everton Soares', 'ocebolinha', 'Brasil', 'Ceará', '1996-03-22');
    insert into membro values ('Jorge Jesus', 'odetetive', 'Portugal', 'Amadora', '1954-06-24');
    insert into membro values ('Haris Severovic', 'oesferovite', 'Suica', 'Sursee', '1992-02-22');
    insert into membro values ('Diogo Goncalves', 'oquasedefesa', 'Portugal', 'Almodovar', '1997-02-06');
    insert into membro values ('Goncalo Ramos', 'ojovem', 'Portugal', 'Olhao', '2001-05-20');
    insert into membro values ('Gilberto Junior', 'ogilaberto', 'Brasil', 'Rio de Janeiro', '1993-03-07');
    insert into membro values ('Francisco Ferreira', 'omaudefesa', 'Portugal', 'Oliveira de Azemeis', '1997-03-26');
    insert into membro values ('Mile Svilar', 'oredes', 'Belgica', 'Antuerpia', '1999-08-27');
    insert into membro values ('Franco Cervi', 'oanao', 'Argentina', 'San Lorenzo', '1994-05-26');

    --b)
    insert into livro values ('978-4-4859-8723-0', 'O Hipnotista');
    insert into livro values ('978-5-1427-1466-4', 'O Assassino no Expresso Oriente');
    insert into livro values ('978-3-5685-7315-5', 'O Misterioso Caso de Styles');
    insert into livro values ('978-9-9864-0901-4', 'A Casa Torta');
    insert into livro values ('978-8-9985-5448-4', 'Um Crime Capital');
    insert into livro values ('978-9-1786-9840-0', 'The Other Woman');
    insert into livro values ('978-0-6206-5270-4', 'The Girl on the Train');
    insert into livro values ('978-1-7046-1342-0', 'Desaparecidas');
    insert into livro values ('978-9-1497-8713-3', 'O Aprendiz');
    insert into livro values ('978-1-8866-9375-3', 'O Cirurgião');
    insert into livro values ('978-1-1607-8153-4', 'Em Aguas Sombrias');

    insert into autor values ('a01', 'Agatha Christie', 'Reino Unido');
    insert into autor values ('a02', 'Francisco Jose Viegas', 'Portugal');
    insert into autor values ('a03', 'Daniel Silva', 'Estados Unidos da America');
    insert into autor values ('a04', 'Paula Hawkins', 'Zimbabue');
    insert into autor values ('a05', 'Tess Gerritsen', 'Estados Unidos da America');
    insert into autor values ('a06', 'Lars Kepler', 'Suecia');

    insert into autoria values ('978-4-4859-8723-0', 'a06');
    insert into autoria values ('978-5-1427-1466-4', 'a01');
    insert into autoria values ('978-5-1427-1466-4', 'a02');
    insert into autoria values ('978-5-1427-1466-4', 'a05');
    insert into autoria values ('978-3-5685-7315-5', 'a01');
    insert into autoria values ('978-9-9864-0901-4', 'a01');
    insert into autoria values ('978-8-9985-5448-4', 'a02');
    insert into autoria values ('978-9-9864-0901-4', 'a03');
    insert into autoria values ('978-9-1786-9840-0', 'a03');
    insert into autoria values ('978-9-1786-9840-0', 'a04');
    insert into autoria values ('978-0-6206-5270-4', 'a04');
    insert into autoria values ('978-1-7046-1342-0', 'a05');
    insert into autoria values ('978-9-1497-8713-3', 'a04');
    insert into autoria values ('978-9-1497-8713-3', 'a05');
    insert into autoria values ('978-1-8866-9375-3', 'a05');
    insert into autoria values ('978-1-8866-9375-3', 'a06');
    insert into autoria values ('978-1-1607-8153-4', 'a02');
    insert into autoria values ('978-1-1607-8153-4', 'a03');
    insert into autoria values ('978-1-1607-8153-4', 'a04');

    insert into genero values ('978-4-4859-8723-0', 'policial');
    insert into genero values ('978-4-4859-8723-0', 'misterio');
    insert into genero values ('978-5-1427-1466-4', 'policial');
    insert into genero values ('978-5-1427-1466-4', 'drama');
    insert into genero values ('978-5-1427-1466-4', 'romance');
    insert into genero values ('978-3-5685-7315-5', 'policial');
    insert into genero values ('978-3-5685-7315-5', 'romance');
    insert into genero values ('978-9-9864-0901-4', 'policial');
    insert into genero values ('978-9-9864-0901-4', 'ficcao cientifica');
    insert into genero values ('978-9-9864-0901-4', 'horror');
    insert into genero values ('978-8-9985-5448-4', 'policial');
    insert into genero values ('978-8-9985-5448-4', 'drama');
    insert into genero values ('978-9-1786-9840-0', 'policial');
    insert into genero values ('978-9-1786-9840-0', 'drama');
    insert into genero values ('978-0-6206-5270-4', 'policial');
    insert into genero values ('978-0-6206-5270-4', 'horror');
    insert into genero values ('978-1-7046-1342-0', 'policial');
    insert into genero values ('978-1-7046-1342-0', 'horror');
    insert into genero values ('978-9-1497-8713-3', 'policial');
    insert into genero values ('978-9-1497-8713-3', 'drama');
    insert into genero values ('978-1-8866-9375-3', 'policial');
    insert into genero values ('978-1-8866-9375-3', 'horror');
    insert into genero values ('978-1-1607-8153-4', 'policial');
    insert into genero values ('978-1-1607-8153-4', 'drama');
    insert into genero values ('978-1-1607-8153-4', 'romance');

    --c)
    insert into amigo values ('ojovem', 'ogoleador');
    insert into amigo values ('ojovem', 'oleitor');
    insert into amigo values ('ojovem', 'oguardiao');
    insert into amigo values ('ojovem', 'oimpostor');
    insert into amigo values ('ojovem', 'omascarilha');
    insert into amigo values ('ojovem', 'olesionado');
    insert into amigo values ('ojovem', 'omosqueteiro');
    insert into amigo values ('ojovem', 'oaziado');
    insert into amigo values ('ojovem', 'ozarolho');
    insert into amigo values ('ojovem', 'opassador');
    insert into amigo values ('ojovem', 'olouro');
    insert into amigo values ('ojovem', 'ocebolinha');
    insert into amigo values ('ojovem', 'odetetive');
    insert into amigo values ('ojovem', 'oesferovite');
    insert into amigo values ('ojovem', 'oquasedefesa');
    insert into amigo values ('ojovem', 'ogilaberto');
    insert into amigo values ('ojovem', 'omaudefesa');
    insert into amigo values ('ojovem', 'oredes');
    insert into amigo values ('ojovem', 'oanao');
    insert into amigo values ('olouro', 'omascarilha');
    insert into amigo values ('olouro', 'oleitor');
    insert into amigo values ('olouro', 'oanao');
    insert into amigo values ('olouro', 'ocebolinha');
    insert into amigo values ('opassador', 'oredes');
    insert into amigo values ('ogoleador', 'odetetive');
    insert into amigo values ('ogoleador', 'ogilaberto');
    insert into amigo values ('ogoleador', 'oguardiao');
    insert into amigo values ('oimpostor', 'odetetive');
    insert into amigo values ('oimpostor', 'oesferovite');
    insert into amigo values ('oleitor', 'opassador');
    insert into amigo values ('oleitor', 'odetetive');
    insert into amigo values ('olesionado', 'oimpostor');

    --d)
    insert into genero values ('978-4-4859-8723-0', 'policial');
    insert into genero values ('978-4-4859-8723-0', 'misterio');
    insert into genero values ('978-5-1427-1466-4', 'policial');
    insert into genero values ('978-5-1427-1466-4', 'drama');
    insert into genero values ('978-5-1427-1466-4', 'romance');
    insert into genero values ('978-3-5685-7315-5', 'policial');
    insert into genero values ('978-3-5685-7315-5', 'romance');
    insert into genero values ('978-9-9864-0901-4', 'policial');
    insert into genero values ('978-9-9864-0901-4', 'ficcao cientifica');
    insert into genero values ('978-9-9864-0901-4', 'horror');
    insert into genero values ('978-8-9985-5448-4', 'policial');
    insert into genero values ('978-8-9985-5448-4', 'drama');
    insert into genero values ('978-9-1786-9840-0', 'policial');
    insert into genero values ('978-9-1786-9840-0', 'drama');
    insert into genero values ('978-0-6206-5270-4', 'policial');
    insert into genero values ('978-0-6206-5270-4', 'horror');
    insert into genero values ('978-1-7046-1342-0', 'policial');
    insert into genero values ('978-1-7046-1342-0', 'horror');
    insert into genero values ('978-9-1497-8713-3', 'policial');
    insert into genero values ('978-9-1497-8713-3', 'drama');
    insert into genero values ('978-1-8866-9375-3', 'policial');
    insert into genero values ('978-1-8866-9375-3', 'horror');
    insert into genero values ('978-1-1607-8153-4', 'policial');
    insert into genero values ('978-1-1607-8153-4', 'drama');
    insert into genero values ('978-1-1607-8153-4', 'romance');


--4.
    --a)
    select distinct autor.Nome
        from genero natural inner join autoria natural inner join autor
        where Genero like ‘drama’;

    --b)
    select distinct membro.Nome
        from (autoria natural inner join autor) inner join (gosta natural
inner join membro)
            using(ISBN)
    where autor.Nome like ‘Agatha Christie’;

    --c)
    select distinct membro.Nome
        from (membro natural inner join gosta) inner join (autoria natural
    inner join autor)
            using(ISBN)
        where membro.Pais = autor.Pais;

    --d)
    select distinct membro.Nome
        from membro natural inner join genero
    except
        select distinct membro.Nome
            from (membro natural inner join gosta) inner join (autor natural
    inner join autoria)
                using(ISBN)
        where autor.Nome like ‘Agatha Christie’;

    --e)
    select distinct membro.Nome
        from membro
    except
    (select distinct membro.Nome
        from membro inner join amigo on IdMemb = IdMemb2
        where IdMemb1 like ‘oleitor’
    union
    select distinct membro.Nome
        from membro inner join amigo on IdMemb = IdMemb1
        where IdMemb2 like ‘oleitor’)
    except
    select distinct membro.Nome
        from membro
        where IdMemb like ‘oleitor’;

    --f)
    with amigos as (
        select distinct membro.IdMemb, membro.DataNasc
            from membro inner join amigo on IdMemb = IdMemb2
            where IdMemb1 like ‘oleitor’
        union
        select distinct membro.IdMemb, membro.DataNasc
            from membro inner join amigo on IdMemb = IdMemb1
            where IdMemb2 like ‘oleitor’
    )
    select membro.Nome
        from (membro natural inner join amigos), (select IdMemb, DataNasc
    from membro where IdMemb like ‘oleitor’) as leitor
        where amigos.DataNasc > leitor.DataNasc;

    --g)
    select distinct membro.Nome
        from (membro natural inner join gosta) inner join (autoria natural
    inner join autor)
            using(ISBN)
        where autor.Nome like ‘Agatha Christie’
    intersect
    select distinct membro.Nome
        from (membro natural inner join gosta) inner join (autoria natural
    inner join autor)
            using (ISBN)
        where autor.Nome like ‘Francisco Jose Viegas’;

    --h)
    select distinct membro.Nome
        from (membro natural inner join gosta) inner join (autoria natural
    inner join autor)
            using(ISBN)
        where autor.Nome like ‘Agatha Christie’
    union
    select distinct membro.Nome
        from (membro natural inner join gosta) inner join (autoria natural
    inner join autor)
            using(ISBN)
        where autor.Nome like ‘Francisco Jose Viegas’;

    --i)
    select count(*)
        from(
            (select distinct membro.Nome
                from membro inner join amigo on IdMemb = IdMemb2
                where IdMemb1 like 'oleitor'
            union
            select distinct membro.Nome
                from membro inner join amigo on IdMemb = IdMemb1
                where IdMemb2 like 'oleitor'))
        as nAmigos;

    --j)
    with informa as (
        select distinct amigos.Nome, sum(nAmigos)
            from (
                select distinct membro.Nome, count(IdMemb) as
                    from membro inner join amigo on IdMemb =
                group by membro.Nome
                union
                select distinct membro.Nome, count(IdMemb) as
                    from membro inner join amigo on IdMemb =
                group by membro.Nome)
                as amigos
            group by amigos.Nome)
    select informa.Nome
        from informa, (
            select max(informa.Sum) as máximo
                from informa) as tabela
                where informa.sum = tabela.maximo;

    --k)
    with leitor as (
        with expr as (
            select distinct membro.IdMemb, count(ISBN) as nLivros
                from gosta natural inner join membro
                group by membro.IdMemb)
        select distinct expr.IdMemb
            from expr natural inner join (
                select max(expr.nLivros) as máximo
                    from expr)
                as tabela
            where tabela.maximo = expr.nLivros)
    select membro.Nome
        from (
            select distinct amigos.Id
                from (
                    select distinct IdMemb1 as Id
                        from amigo, leitor
                        where IdMemb2 like leitor.IdMemb
                    union
                    select distinct IdMemb2 as Id
                        from amigo, leitor
                        where IdMemb1 like leitor.idMemb) as amigos)
            as amigos inner join membro on amigos.Id = membro.IdMemb;

    --l)
    select livro.Titulo, count(Genero)
        from livro natural inner join genero
    group by livro.Titulo;

    --m)
    select gostos.Titulo, gostos,nGostos, generos.nGeneros
        from (
            (select distinct Titulo, ISBN, count(IdMemb) as nGostos
                from livro natural inner join gosta
            group by ISBN) as gostos
            inner join
            (select ISBN, count(Genero) as nGeneros
                from livro natural inner join genero
            group by ISBN) as generos using(ISBN)
        );

    --n)
    select livros.Nome, livros.nLivros, generos.nGeneros, gostos.nGostos
        from (
            (select autor.Nome, count(ISBN) as nLivros
                from autor natural inner join autoria
            group by autor.Nome) as livros
            natural inner join
            (select autor.Nome, count(genero) as nGeneros
                from autor natural inner join autoria natural inner join genero
            group by autor.Nome) as generos
            natural inner join
            (select autor.Nome, count(IdMemb) as nGostos
                from autor natural inner join autoria natural inner join gosta
            group by autor.Nome) as gostos);

    --o)
    select amigos.IdMemb, livros.Nome, livros.cont as nLivros, amigos.cont as nAmigos
        from (
            (select membro.IdMemb, membro.Nome, count(gosta.ISBN) as cont
                from membro natural inner join gosta
            group by membro.IdMemb) as livros
            inner join
            (select membro.IdMemb, membro.Nome, count(IdMemb1) as cont
                from membro inner join amigo on IdMemb2 = IdMemb or IdMemb1 = IdMemb
            group by membro.IdMemb) as amigos
            using(IdMemb)
        );

    --p)
    with dados as (
        select distinct amigos.Nome, sum(nAmigos)
            from (
                select distinct membro.Nome, count(IdMemb) as nAmigos
                    from membro inner join amigo on Idmemb = IdMemb1
                group by membro.Nome
                union
                select distinct membro.Nome, count(Idmemb) as nAmigos
                    from membro inner join amigo on IdMemb = IdMemb2
                group by membro.Nome) as amigos group by amigos.Nome)
    select dados.Nome
        from (
            select count(IdMemb) - 1 as cont
                from membro) as tabela
            natural inner join dados
        where dados.sum = tabela.cont;

    --q)
    select distinct Titulo
        from livro natural inner join gosta natural inner join (
            select membro.IdMemb
                from membro inner join amigo on IdMemb=IdMemb1 or Idmemb = IdMemb2
        where IdMemb2 like 'oleitor' or IdMemb1 like 'oleitor') as Titulo;
