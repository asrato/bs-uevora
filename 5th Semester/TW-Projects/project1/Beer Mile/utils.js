/**
 * Constantes globais
 */

const ITEMS_PER_PAGE = 4;
const URL_EVENTSEARCH = "http://alunos.di.uevora.pt/tweb/t1/eventsearch";
const URL_INSCRICAO = "http://alunos.di.uevora.pt/tweb/t1/inscricao";
const URL_INSCRITOS = "http://alunos.di.uevora.pt/tweb/t1/inscritos";
const URL_CLASSIF = "http://alunos.di.uevora.pt/tweb/t1/classif";
const URL_TOP3 = "http://alunos.di.uevora.pt/tweb/t1/top3";
const URL_ADDEVENT = "http://alunos.di.uevora.pt/tweb/t1/addevent";
const URL_SAVETIME = "http://alunos.di.uevora.pt/tweb/t1/savetime";

/**
 * =========================================================================
 */

/**
 * Variáveis globais
 */

let xmlHttp = new XMLHttpRequest();
let eventosFuturos = {
    pagina: 1,
    paginas: 1,
    eventos: []
};
let eventosADecorrer = {
    pagina: 1,
    paginas: 1,
    eventos: []
};
let eventosRealizados = {
    pagina: 1,
    paginas: 1,
    eventos: []
};
let eventos = {
    pagina: 1,
    paginas: 1,
    eventos: []
};
let eventosFiltrados = {
    pagina: 1,
    paginas: 1,
    eventos: []
};
let classificacaoFiltrada = {
    participantes: [],
    participantesFiltrados: []
};


/**
 * =========================================================================
 */

/**
 * Funções genéricas
 */

const enviarPedido = (method, url, dadosSerializados) => {
    xmlHttp.open(method, url, false);
    xmlHttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xmlHttp.send(dadosSerializados);

    return JSON.parse(xmlHttp.response);
}

const obterObjeto = (dados) => {
    return Object.assign(...Array.from(dados, ([x, y]) => ({ [x]: y })));
}

const serializarObjeto = (objeto) => {
    return new URLSearchParams(Object.keys(objeto).map(key => [key, objeto[key]]));
}

const calcularNumeroDePáginas = (arrayDeObjetos) => {
    return arrayDeObjetos.length % ITEMS_PER_PAGE === 0 ? arrayDeObjetos.length / ITEMS_PER_PAGE : Math.floor(arrayDeObjetos.length / ITEMS_PER_PAGE) + 1;
}

const adicionarOpcoesDePagina = (numeroDePaginas) => {
    let opcao, selectElement = document.getElementById('pagina');

    for (let i = 0; i < numeroDePaginas; i++) {
        opcao = document.createElement('option');
        opcao.value = i + 1;
        opcao.innerText = i + 1;
        selectElement.appendChild(opcao);
    }
}

const obterItemsPorPagina = (objeto) => {
    let novoArray = [], i = ITEMS_PER_PAGE;

    for (i; i > 0; i--) {
        novoArray.push(objeto.eventos[ITEMS_PER_PAGE * objeto.pagina - i]);
    }

    return novoArray;
}

const filtrarPorEventos = (filtro, array) => {
    let elemento, novoArray = [], dataEvento;
    const dataHoje = parseInt((new Date().toISOString().slice(0, 10)).replaceAll('-', ''));

    switch (filtro) {
        case ('futuros'):
            for (elemento of array) {
                dataEvento = parseInt(elemento.data.replaceAll('-', ''));
                if (dataHoje < dataEvento) {
                    novoArray.push(elemento);
                }
            }
            break;
        case ('realizados'):
            for (elemento of array) {
                dataEvento = parseInt(elemento.data.replaceAll('-', ''));
                if (dataHoje > dataEvento) {
                    novoArray.push(elemento);
                }
            }
            break;
        case ('a-decorrer'):
            for (elemento of array) {
                dataEvento = parseInt(elemento.data.replaceAll('-', ''));
                if (dataHoje === dataEvento) {
                    novoArray.push(elemento);
                }
            }
            break;
        case ('todos'):
            novoArray = array;
            break;
        default:
            break;
    }

    return novoArray;
}

const obterIndexDoEvento = (array, idEvento) => {
    let i = 0, index;

    for (i; i < array.length; i++) {
        if (array[i].id === idEvento) {
            index = i;
        }
    }
    return index;
}

const mudarDePagina = (evento) => {
    let i = 1, card, eventosNaPagina, tag = evento.target.name;

    if (tag === 'a-decorrer') {
        eventosADecorrer.pagina = parseInt(evento.target.value);
        eventosNaPagina = obterItemsPorPagina(eventosADecorrer);


        for (i; i <= ITEMS_PER_PAGE; i++) {
            card = document.getElementById('card' + (i % ITEMS_PER_PAGE === 0 ? 4 : i % ITEMS_PER_PAGE));
            construirItem(card, eventosADecorrer.eventos[eventosADecorrer.pagina * eventosADecorrer.paginas + i - 1]);
        }
    } else if (tag === 'realizados') {
        eventosRealizados.pagina = parseInt(evento.target.value);
        eventosNaPagina = obterItemsPorPagina(eventosRealizados);


        for (i; i <= ITEMS_PER_PAGE; i++) {
            card = document.getElementById('card' + (i % ITEMS_PER_PAGE === 0 ? 4 : i % ITEMS_PER_PAGE));
            construirItem(card, eventosRealizados.eventos[eventosRealizados.pagina * eventosRealizados.paginas + i - 1]);
        }
    } else if (tag === 'futuros') {
        eventosFuturos.pagina = parseInt(evento.target.value);
        eventosNaPagina = obterItemsPorPagina(eventosFuturos);


        for (i; i <= ITEMS_PER_PAGE; i++) {
            card = document.getElementById('card' + (i % ITEMS_PER_PAGE === 0 ? 4 : i % ITEMS_PER_PAGE));
            construirItem(card, eventosFuturos.eventos[eventosFuturos.pagina * eventosFuturos.paginas + i - 1]);
        }
    } else if (tag === 'todos') {
        eventos.pagina = parseInt(evento.target.value);
        eventosNaPagina = obterItemsPorPagina(eventos);


        for (i; i <= ITEMS_PER_PAGE; i++) {
            card = document.getElementById('card' + (i % ITEMS_PER_PAGE === 0 ? 4 : i % ITEMS_PER_PAGE));
            construirItem(card, eventos.eventos[eventos.pagina * eventos.paginas + i - 1]);
        }
    }
}

const construirItem = (item, evento) => {
    if (evento !== undefined) {
        item.style.display = "flex";
        item.innerHTML = "<div id=\"" + evento.id + "\"class=\"card-div\"><h2>Evento</h2><p>" + evento.nome + "</p><h2>Data</h2><p>" + evento.data + "</p><h2>Descrição</h2><p>" + evento.descricao + "</p></div>";
    } else {
        item.style.display = 'none'
    }
}

const construirCards = (quantidade, objeto) => {
    let i = 1, card;

    for (i; i <= quantidade; i++) {
        card = document.getElementById('card' + i);
        construirItem(card, objeto.eventos[i - 1]);
    }
}

const carregarEventos = (tag) => {
    eventos.eventos = enviarPedido("POST", URL_EVENTSEARCH, "nome=").eventos;
    eventos.paginas = calcularNumeroDePáginas(eventos.eventos);
    switch (tag) {
        case 'a-decorrer':
            eventosADecorrer.eventos = filtrarPorEventos('a-decorrer', eventos.eventos);

            if (eventosADecorrer.eventos.length > 0) {
                construirCards(4, eventosADecorrer);

                eventosADecorrer.paginas = calcularNumeroDePáginas(eventosADecorrer.eventos);
                adicionarOpcoesDePagina(eventosADecorrer.paginas);
                document.getElementById("numeroDaPagina").innerText = eventosADecorrer.paginas;
            } else {
                document.getElementById('conteudo-cards').style.display = 'none';
                document.getElementById('conteudo-paginacao').style.display = 'none';
                p = document.createElement('p');
                p.innerText = 'Não existem eventos a decorrer';
                document.getElementById('conteudo').appendChild(p);
            }
            break;
        case 'realizados':
            eventosRealizados.eventos = filtrarPorEventos('realizados', eventos.eventos);

            if (eventosRealizados.eventos.length > 0) {
                construirCards(4, eventosRealizados);

                eventosRealizados.paginas = calcularNumeroDePáginas(eventosRealizados.eventos)
                adicionarOpcoesDePagina(eventosRealizados.paginas);
                document.getElementById("numeroDaPagina").innerText = eventosRealizados.paginas;
            } else {
                document.getElementById('conteudo-cards').style.display = 'none';
                document.getElementById('conteudo-paginacao').style.display = 'none';
                p = document.createElement('p');
                p.innerText = 'Não foram realizados quaisquer eventos';
                document.getElementById('conteudo').appendChild(p);
            }
            break;
        case 'futuros':
            eventosFuturos.eventos = filtrarPorEventos('futuros', eventos.eventos);

            if (eventosFuturos.eventos.length > 0) {
                construirCards(4, eventosFuturos);

                eventosFuturos.paginas = calcularNumeroDePáginas(eventosFuturos.eventos)
                adicionarOpcoesDePagina(eventosFuturos.paginas);
                document.getElementById("numeroDaPagina").innerText = eventosFuturos.paginas;
            } else {
                document.getElementById('conteudo-cards').style.display = 'none';
                document.getElementById('conteudo-paginacao').style.display = 'none';
                p = document.createElement('p');
                p.innerText = 'Não existem eventos futuros';
                document.getElementById('conteudo').appendChild(p);
            }
            break;
        default:
            break;
    }
}

const filtrarEventosPorParametro = (filtro, valor) => {
    let data, eventoData;
    eventosFiltrados.eventos = [];
    switch (filtro) {
        case 'nome':
            if (valor === '') {
                eventosFiltrados = { ...eventos };
            } else {
                for (evento of eventos.eventos) {
                    if (evento.nome.toLowerCase().search(valor.toLowerCase()) > -1) {
                        eventosFiltrados.eventos.push(evento);
                    }
                }
            }
            break;
        case 'data':
            data = parseInt(valor.replaceAll('-', ''));
            for (evento of eventos.eventos) {
                eventoData = parseInt(evento.data.replaceAll('-', ''));
                if (eventoData === data) {
                    eventosFiltrados.eventos.push(evento);
                }
            }
            break;
        default:
            eventosFiltrados = { ...eventos };
            break;
    }
}

const apagarOpcoesDeSelecao = (idSelect) => {
    let select = document.getElementById(idSelect), opcoes, opcao, i;
    indexUltimaOpcao = select.options.length - 1;
    for (i = indexUltimaOpcao; i >= 0; i--) {
        select.remove(i);
    }
}

const obterEventoPorId = (id) => {
    let evento;
    for (evento of eventos.eventos) {
        if (evento.id === id) {
            return evento;
        }
    }
}

const descodificarEscalao = (escalao) => {
    switch (escalao) {
        case 'jun':
            return "Júnior";
        case 'sen':
            return "Sénior";
        case 'vet35':
            return "Veterano 35";
        case 'vet40':
            return "Veterano 40";
        case 'vet50':
            return "Veterano 50";
        case 'vet60':
            return "Veterano 60";
        default:
            return "";
    }
}

const descodificarGenero = (genero) => {
    switch (genero) {
        case 'm':
            return "Masculino";
        case 'f':
            return "Feminino";
        default:
            return "";
    }
}

const clicarNumEvento = (event) => {
    let id, evento, modal, participante, participantes, conteudoModal, pessoa, texto;
    id = parseInt(event.target.childNodes[0].id);
    evento = obterEventoPorId(id);
    participantes = enviarPedido("POST", URL_INSCRITOS, "evento=" + id);

    conteudoModal = document.getElementById('modal-conteudo');

    conteudoModal.innerHTML = '<span class="fechar" onclick="fecharModal()">&times;</span><h2 id="nome-evento"></h2>';

    modal = document.getElementById('modal');
    modal.style.display = 'block';

    document.getElementById('nome-evento').innerText = evento.nome;



    for (participante of participantes.inscritos) {
        texto = `[${participante.dorsal}] ${participante.nome} (${descodificarEscalao(participante.escalao)} ${descodificarGenero(participante.genero)})`;
        pessoa = document.createElement('p');
        pessoa.innerText = texto
        conteudoModal.appendChild(pessoa);
    }
}

const apagarFilhosDeElemento = (idElemento, limit) => {
    let i;
    const pai = document.getElementById(idElemento);
    const numeroDeFilhos = pai.childNodes.length;

    for (i = numeroDeFilhos - 1; i > limit; i--) {
        pai.removeChild(pai.lastChild);
    }
}

const filtrarParticipantes = (filtro) => {
    let participante, i = 1, toAdd, array = [];

    classificacaoFiltrada.participantesFiltrados = [];

    if (filtro === 'geral') {
        classificacaoFiltrada.participantesFiltrados = [...classificacaoFiltrada.participantes]; // copia segura de arrays
    } else {
        for (participante of classificacaoFiltrada.participantes) {
            if (participante.genero === filtro) {
                toAdd = { ...participante }; // copia segura de objetos
                toAdd.pos = i;
                i++;
                array.push(toAdd);
            }
        }
        classificacaoFiltrada.participantes = [...array];
        classificacaoFiltrada.participantesFiltrados = [...array];
    }
}

/**
 * ===================================================
 */

/**
 * Funções da página inscricao.html
 */

const carregarEventosComoOpcoes = (selectId, tag) => {
    let selectElement, opcao, evento;

    eventos.eventos = enviarPedido("POST", URL_EVENTSEARCH, "nome=").eventos;

    eventosFuturos.eventos = filtrarPorEventos(tag, eventos.eventos);

    selectElement = document.getElementById(selectId);

    if (eventosFuturos.eventos.length > 0) {
        for (evento of eventosFuturos.eventos) {
            opcao = document.createElement('option');
            opcao.value = evento.id;
            opcao.innerText = evento.nome;
            selectElement.appendChild(opcao);
        }
    }
    if (document.getElementById('evento-descricao') != null) {
        document.getElementById('evento-descricao').innerText = 'Descrição: ' + eventosFuturos.eventos[0].descricao;
        document.getElementById('evento-data').innerText = 'Data: ' + eventosFuturos.eventos[0].data;
    }
}

const mudarDetalhesDeEvento = () => {
    let eventoSelecionado = document.getElementById('opcao-eventos').value;

    document.getElementById('evento-descricao').innerText = 'Descrição: ' + eventosFuturos.eventos[obterIndexDoEvento(eventosFuturos.eventos, parseInt(eventoSelecionado))].descricao;
    document.getElementById('evento-data').innerText = 'Data: ' + eventosFuturos.eventos[obterIndexDoEvento(eventosFuturos.eventos, parseInt(eventoSelecionado))].data;
}

const enviarInscricao = (evento) => {
    evento.preventDefault();

    let dadosDoFormulario = new FormData(evento.target), dadosSerializados, resposta, objeto = obterObjeto(dadosDoFormulario), campo;

    for (campo of dadosDoFormulario) {
        if (campo[1] === '' || campo[1] === null) {
            alert(`Não se esqueça de preencher o campo "${campo[0]}"`);
            return;
        }
    }

    dadosSerializados = serializarObjeto(objeto);

    resposta = enviarPedido("POST", URL_INSCRICAO, dadosSerializados);

    if (resposta.resultado === 'ok') {
        alert(`Participante número ${resposta.inscricao} inscrito com sucesso!`);
        window.location.assign('../eventos/proximos.html')
    } else {
        alert(resposta.resultado);
    }
}

/**
 * ===================================================
 */

/**
 * Funções da página procurar.html
 */

const carregarPaginaDeProcura = () => {
    document.getElementById('pesquisar-data').value = new Date().toISOString().slice(0, 10);
    carregarEventos('todos');
    eventosFiltrados = { ...eventos };

    if (eventosFiltrados.eventos.length > 0) {
        construirCards(4, eventosFiltrados);

        apagarOpcoesDeSelecao('pagina');

        eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);
        adicionarOpcoesDePagina(eventosFiltrados.paginas);
        document.getElementById("numeroDaPagina").innerText = eventosFiltrados.paginas;
    } else {
        document.getElementById('conteudo-cards').style.display = 'none';
        document.getElementById('conteudo-paginacao').style.display = 'none';
        p = document.createElement('p');
        p.innerText = 'Não existem eventos';
        document.getElementById('conteudo').appendChild(p);
    }
}

const limparCamposDeProcura = () => {
    document.getElementById('form-nome').reset();
    document.getElementById('pesquisar-data').value = new Date().toISOString().slice(0, 10);
    document.getElementById('selecionar-metodo-pesquisa').reset();
    eventosFiltrados = { ...eventos };

    if (eventosFiltrados.eventos.length > 0) {
        div = document.getElementById('vazio-texto');
        if (div !== undefined) {
            div.style.display = 'none';
        }
        document.getElementById('items-evento').style.display = 'flex';
        document.getElementById('conteudo-paginacao').style.display = 'flex';
        construirCards(4, eventosFiltrados);

        apagarOpcoesDeSelecao('pagina');

        eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);
        adicionarOpcoesDePagina(eventosFiltrados.paginas);
        document.getElementById("numeroDaPagina").innerText = eventosFiltrados.paginas;
    } else {
        document.getElementById('items-evento').style.display = 'none';
        document.getElementById('conteudo-paginacao').style.display = 'none';
        div = document.getElementById('vazio-texto');
        p = document.getElementById('texto-vazio-p');
        div.style.display = 'flex';
        p.innerText = `Não existem eventos para o dia ${document.getElementById('pesquisar-data').value}`;
        div.appendChild(p);
    }
}

const procurarEvento = (evento) => {
    evento.preventDefault();

    let formTipoProcura, tipoProcura, valor, p, div;

    formTipoProcura = document.getElementById('selecionar-metodo-pesquisa');
    tipoProcura = formTipoProcura.querySelector('input[name=filtro]:checked').value;

    valor = tipoProcura === 'nome' ? document.getElementById('pesquisar-nome').value : document.getElementById('pesquisar-data').value;

    filtrarEventosPorParametro(tipoProcura, valor);
    eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);

    if (tipoProcura === 'data') {
        filtrarEventosPorParametro('data', document.getElementById('pesquisar-data').value);
        eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);

        if (eventosFiltrados.eventos.length > 0) {

            div = document.getElementById('vazio-texto');
            if (div !== undefined) {
                div.style.display = 'none';
            }
            document.getElementById('items-evento').style.display = 'flex';
            document.getElementById('conteudo-paginacao').style.display = 'flex';
            construirCards(4, eventosFiltrados);

            apagarOpcoesDeSelecao('pagina');

            eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);
            adicionarOpcoesDePagina(eventosFiltrados.paginas);
            document.getElementById("numeroDaPagina").innerText = eventosFiltrados.paginas;
        } else {
            document.getElementById('items-evento').style.display = 'none';
            document.getElementById('conteudo-paginacao').style.display = 'none';
            div = document.getElementById('vazio-texto');
            p = document.getElementById('texto-vazio-p');
            div.style.display = 'flex';
            p.innerText = `Não existem eventos para o dia ${document.getElementById('pesquisar-data').value}`;
            div.appendChild(p);
        }
    } else {
        filtrarEventosPorParametro('nome', document.getElementById('pesquisar-nome').value);
        eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);

        if (eventosFiltrados.eventos.length > 0) {
            div = document.getElementById('vazio-texto');
            if (div !== undefined) {
                div.style.display = 'none';
            }
            document.getElementById('items-evento').style.display = 'flex';
            document.getElementById('conteudo-paginacao').style.display = 'flex';
            construirCards(4, eventosFiltrados);

            apagarOpcoesDeSelecao('pagina');

            eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);
            adicionarOpcoesDePagina(eventosFiltrados.paginas);
            document.getElementById("numeroDaPagina").innerText = eventosFiltrados.paginas;
        } else {
            document.getElementById('items-evento').style.display = 'none';
            document.getElementById('conteudo-paginacao').style.display = 'none';
            div = document.getElementById('vazio-texto');
            p = document.getElementById('texto-vazio-p');
            div.style.display = 'flex';
            p.innerText = `Não existem eventos que contenham "${document.getElementById('pesquisar-nome').value}" no nome`;
            div.appendChild(p);
        }
    }
}

const mudarMetodo = (evento) => {
    let div, p;

    if (evento.target.value === 'data') {
        filtrarEventosPorParametro('data', document.getElementById('pesquisar-data').value);
        eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);

        if (eventosFiltrados.eventos.length > 0) {

            div = document.getElementById('vazio-texto');
            if (div !== undefined) {
                div.style.display = 'none';
            }
            document.getElementById('items-evento').style.display = 'flex';
            document.getElementById('conteudo-paginacao').style.display = 'flex';
            construirCards(4, eventosFiltrados);

            apagarOpcoesDeSelecao('pagina');

            eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);
            adicionarOpcoesDePagina(eventosFiltrados.paginas);
            document.getElementById("numeroDaPagina").innerText = eventosFiltrados.paginas;
        } else {
            document.getElementById('items-evento').style.display = 'none';
            document.getElementById('conteudo-paginacao').style.display = 'none';
            div = document.getElementById('vazio-texto');
            p = document.getElementById('texto-vazio-p');
            div.style.display = 'flex';
            p.innerText = `Não existem eventos para o dia ${document.getElementById('pesquisar-data').value}`;
            div.appendChild(p);
        }
    } else {
        filtrarEventosPorParametro('nome', document.getElementById('pesquisar-nome').value);
        eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);

        if (eventosFiltrados.eventos.length > 0) {
            div = document.getElementById('vazio-texto');
            if (div !== undefined) {
                div.style.display = 'none';
            }
            document.getElementById('items-evento').style.display = 'flex';
            document.getElementById('conteudo-paginacao').style.display = 'flex';
            construirCards(4, eventosFiltrados);

            apagarOpcoesDeSelecao('pagina');

            eventosFiltrados.paginas = calcularNumeroDePáginas(eventosFiltrados.eventos);
            adicionarOpcoesDePagina(eventosFiltrados.paginas);
            document.getElementById("numeroDaPagina").innerText = eventosFiltrados.paginas;
        } else {
            document.getElementById('items-evento').style.display = 'none';
            document.getElementById('conteudo-paginacao').style.display = 'none';
            div = document.getElementById('vazio-texto');
            p = document.getElementById('texto-vazio-p');
            div.style.display = 'flex';
            p.innerText = `Não existem eventos que contenham "${document.getElementById('pesquisar-nome').value}" no nome`;
            div.appendChild(p);
        }
    }
}

/**
 * ===================================================
 */

/**
 * Funções de ver participantes
 */

const fecharModal = () => {
    document.getElementById('modal').style.display = 'none';
}


/**
 * ===================================================
 */

/**
 * Funções - conteudo - classificacoes
 */

const carregarPagina = (tag) => {
    let idSelecionado, selecionado;

    carregarEventosComoOpcoes('eventos', 'realizados');

    selecionado = document.getElementById('eventos');
    idSelecionado = selecionado.value;

    classificacaoFiltrada.participantes = enviarPedido("POST", URL_CLASSIF, "evento=" + idSelecionado).classif;

    filtrarParticipantes(tag);

    adicionarEntradasNaTabela('tabela-classificacao');
}

const adicionarEntradasNaTabela = (idTabela) => {
    let tabela, elemento, tr, td;
    tabela = document.getElementById(idTabela);

    apagarFilhosDeElemento('tabela-classificacao', 1);

    for (elemento of classificacaoFiltrada.participantesFiltrados) {
        tr = document.createElement("tr");

        td = document.createElement("td");
        td.innerText = `${elemento.pos}`;
        tr.appendChild(td);

        td = document.createElement("td");
        td.innerText = `${descodificarEscalao(elemento.escalao)} ${descodificarGenero(elemento.genero)}`
        tr.appendChild(td);

        td = document.createElement("td");
        td.innerText = `${elemento.dorsal} - ${elemento.nome}`
        tr.appendChild(td);

        td = document.createElement("td");
        td.innerText = `${elemento.tempo}`
        tr.appendChild(td);

        tabela.appendChild(tr);
    }
}

const mudarEvento = () => {

}

const mudarFiltros = () => {
    let form, idEvento, filtro, valorFiltro;

    form = document.getElementById('filtros-classificacao');

    idEvento = document.getElementById('eventos').value;
    filtro = document.querySelector('input[name="filtro"]:checked').value;

    valorFiltro = document.getElementById('filtro-' + filtro).value;

    filtrarClassificacao(filtro, valorFiltro);

    adicionarEntradasNaTabela('tabela-classificacao')
}

const prevenirReload = (evento) => {
    evento.preventDefault();
}

const filtrarClassificacao = (filtro, valor) => {
    let participante, array = [], participanteParaAdicionar, posicao;
    if (valor === "") {
        array = [...classificacaoFiltrada.participantes]
    } else {
        posicao = 1;
        for (participante of classificacaoFiltrada.participantes) {
            if (participante[filtro].toLowerCase().includes(valor.toLowerCase())) {
                participanteParaAdicionar = { ...participante };
                participanteParaAdicionar.pos = posicao;
                array.push(participanteParaAdicionar)
                posicao++;
            }
        }
    }

    classificacaoFiltrada.participantesFiltrados = [...array];
}


/**
 * ===================================================
 */

/**
 * Funções - conteudo - Top3
 */

const clicarTop3 = (genero) => {
    let id, top3, escalao, modal, conteudoModal, i, nome, tempo;

    id = document.getElementById('eventos').value;

    escalao = document.getElementById('filtro-escalao').value;

    top3 = enviarPedido("POST", URL_TOP3, "evento=" + id + "&genero=" + genero + "&escalao=" + escalao);

    conteudoModal = document.getElementById('modal-conteudo');
    modal = document.getElementById('modal');
    modal.style.display = 'block';

    for (i = 1; i <= 3; i++) {
        nome = document.getElementById('nome-lugar' + i);
        nome.innerText = top3.top3[i - 1].nome;
    }
}


/**
 * ===================================================
 */

/**
 * Funções - conteudo - Criar Evento
 */

const enviarNovoEvento = (evento) => {
    evento.preventDefault();

    let dadosDoFormulario = new FormData(evento.target), dadosSerializados, resposta, objeto = obterObjeto(dadosDoFormulario), campo;

    for (campo of dadosDoFormulario) {
        if (campo[1] === '' || campo[1] === null) {
            alert(`Não se esqueça de preencher o campo "${campo[0]}"`);
            return;
        }
    }

    dadosSerializados = serializarObjeto(objeto);

    resposta = enviarPedido("POST", URL_ADDEVENT, dadosSerializados);

    if (resposta.resultado === 'ok') {
        alert(`Evento criado com sucesso! ID do evento: ${resposta.id}`);
        evento.target.reset();
        obterData();
    } else {
        alert(resposta.resultado);
    }
}

const obterData = () => {
    let dataHoje, dataAmanha, elementoData;

    dataHoje = new Date();
    dataAmanha = new Date();
    dataAmanha.setDate(dataHoje.getDate() + 1);
    elementoData = document.getElementById('inscricao-data');
    elementoData.value = dataAmanha.toISOString().substring(0, 10);
    elementoData.setAttribute('min', dataAmanha.toISOString().substring(0, 10));
}

const limparForm = () => {
    let evento = document.getElementById('evento-criar');
    evento.reset();
    obterData();
}

const carregarCriarEvento = () => {
    let dataHoje, dataAmanha, elementoData;

    dataHoje = new Date();
    dataAmanha = new Date();
    dataAmanha.setDate(dataHoje.getDate() + 1);
    elementoData = document.getElementById('inscricao-data');
    elementoData.value = dataAmanha.toISOString().substring(0, 10);
    elementoData.setAttribute('min', dataAmanha.toISOString().substring(0, 10));
}


/**
 * ===================================================
 */

/**
 * Funções - conteudo - Registar Tempos
 */

const carregarRegistarTempos = () => {
    let selectEventos, selectParticipantes, evento, participante, opcao;

    eventos.eventos = enviarPedido("POST", URL_EVENTSEARCH, "nome=").eventos;

    eventosRealizados.eventos = filtrarPorEventos('realizados', eventos.eventos);

    selectEventos = document.getElementById('opcao-eventos');

    if (eventosRealizados.eventos.length > 0) {
        for (evento of eventosRealizados.eventos) {
            opcao = document.createElement('option');
            opcao.value = evento.id;
            opcao.innerText = evento.nome;
            selectEventos.appendChild(opcao);
        }
    }

    classificacaoFiltrada.participantes = enviarPedido("POST", URL_INSCRITOS, "evento=1").inscritos;

    selectParticipantes = document.getElementById('opcao-participantes');

    if (classificacaoFiltrada.participantes.length > 0) {
        for (participante of classificacaoFiltrada.participantes) {
            opcao = document.createElement('option');
            opcao.value = participante.dorsal;
            opcao.innerText = `${participante.dorsal} - ${participante.nome}`;;
            selectParticipantes.appendChild(opcao);
        }
    }
}

const carregarParticipantes = () => {
    let selectParticipantes = document.getElementById('opcao-participantes');

    apagarOpcoesDeSelecao('opcao-participantes');  

    classificacaoFiltrada.participantes = enviarPedido("POST", URL_INSCRITOS, "evento=" + document.getElementById('opcao-eventos').value).inscritos;

    if (classificacaoFiltrada.participantes.length > 0) {
        for (participante of classificacaoFiltrada.participantes) {
            opcao = document.createElement('option');
            opcao.value = participante.dorsal;
            opcao.innerText = participante.nome + " (" + participante.dorsal + ")";
            selectParticipantes.appendChild(opcao);
        }
    }
}

const registarTempos = (evento) => {
    evento.preventDefault();

    let dadosDoFormulario = new FormData(evento.target), dadosSerializados, resposta, objeto = obterObjeto(dadosDoFormulario), campo;

    for (campo of dadosDoFormulario) {
        if (campo[1] === '' || campo[1] === null) {
            alert(`Não se esqueça de preencher o campo "${campo[0]}"`);
            return;
        }
        if (campo[0] === 'tempo') {
            if (campo[1] <= 0) {
                alert('O tempo tem de ser positivo');
                return;
            }
        }
    }

    dadosSerializados = serializarObjeto(objeto);

    resposta = enviarPedido("POST", URL_SAVETIME, dadosSerializados);

    if (resposta.resultado === 'ok') {
        alert(`Tempo registado com sucesso!`);
        evento.target.reset();
    } else {
        alert(resposta.resultado);
    }
}