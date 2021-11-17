/**
 * Classe implementada na Interface Fila.
 * Classe utilizada para a verificação dos vários aspetos de uma diagonal ascendente (fila diagonal).
 * Utiliza outras classes, tais como a classe pública Tabuleiro ou a classe pública Peca.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */


public class DiagonalAscendente implements Fila{

    /**
     * Variável de instância que guarda o Tabuleiro que é recebido como argumento no construtor da classe, de modo a poder ser utilizado nos diversos métodos.
     */
    private Tabuleiro tabuleiro;

    /**
     * linha - variável de instância responsável por guardar a linha da peça pela qual a diagonal ascendente passa.
     * coluna - variável de instância responsável por guardar a coluna da peça pela qual a diagonal ascendente passa.
     */
    private int linha, coluna;


    /**
     * Construtor da classe DiagonalAscendente, que guarda nas variáveis de instância da classe os valores recebidos como argumento.
     *
     * @param tab Tabuleiro a que a diagonal pertence
     * @param linha linha da peça pela qual a diagonal passa
     * @param coluna coluna da peça pela qual a diagonal passa
     */
    public DiagonalAscendente(Tabuleiro tab, int linha, int coluna) {
        this.tabuleiro = tab;
        this.linha = linha;
        this.coluna = coluna;
    }


    /**
     * Método responsável pelo retorno do tamanho da coluna.
     * A variável contador é inicializada com -1, de modo a que, durante o percorrer da diagonal, a peça de coordenadas (linha, coluna) não seja contada duas vezes.
     * Após isso, são executados 2 ciclos for utilizando duas variáveis cada um, de modo a que o incremento/decremento das variáveis i e j seja simultâneo.
     * No primeiro ciclo for é verificada a parte esquerda da diagonal ascendente (parte inferior).
     * No segundo ciclo for é verificada a parte direita da diagonal ascendente (parte superior).
     * Em qualquer um dos ciclos, o contador é incrementado cada vez que as variáveis i e j são alteradas (incrementas ou decrementadas).
     *
     * @return contador, ou seja, o tamanho da diagonal
     */
    @Override
    public int comprimento() {
        int contador = -1;
        for (int i = this.linha, j = this.coluna; i < this.tabuleiro.getTamanho() && j >= 0; i++, j--) {
            contador++;
        }
        for (int i = this.linha, j = this.coluna; i >= 0 && j < this.tabuleiro.getTamanho(); i--, j++) {
            contador++;
        }
        return contador;
    }

    /**
     * Método responsável pelo cálculo e pela obtenção do número de peças existentes na diagonal.
     * O contador é inicializado com -1, de modo a evitar que a peça pela qual a diagonal passa (cujas coordenadas são recebidas pelo construtor) não é contabilizada duas vezes.
     * De seguida são verificadas as duas partes da diagonal, utilizando os mesmo dois ciclos for utilizados no método acima.
     * Em cada um dos ciclos for, sempre que a peça do tabuleiro cujas coordenadas são (i, j) for diferente de Nada, ou seja, que exista lá uma peça, o contador é incrementado.
     *
     * @return contador (número de peças presentes na diagonal)
     */
    @Override
    public int pecas() {
        int contador = -1;
        for (int i = this.linha, j = this.coluna; i < this.tabuleiro.getTamanho() && j >= 0; i++, j--){
            if(!(this.tabuleiro.peca(i, j) instanceof Nada)){
                contador++;
            }
        }
        for (int i = this.linha, j = this.coluna; i >= 0 && j < this.tabuleiro.getTamanho(); i--, j++) {
            if(!(this.tabuleiro.peca(i, j) instanceof Nada)){
                contador++;
            }
        }
        return contador;
    }

    /**
     * Método responsável por retornar a peça que está na posição pos da diagonal.
     * Inicialmente são inicializadas duas variáveis locais com as variáveis de instância, de modo a estas poderem ser alteradas sem serem perdidos os seus valores iniciais.
     * Depois, no bloco if, é verificado se a posição pos é válida ou não na diagonal em questão, verificando se ele é menor que 0 ou se é maior que o comprimento da diagonal.
     * Se alguma dessas condições se verificar dá throw a uma nova excepção (IndexOutOfBoundsException) com a mensagem ("Out of bounds").
     * Após esta verificação é executado um ciclo while, que colocará as variáveis locais limiteLinha e limiteColuna na primeira posição da diagonal (considerando a primeira posição aquela que tem menor linha).
     * Assim que o limiteLinha seja 0 ou que o limiteColuna seja igual a comprimento()-1, o ciclo while não é mais executado.
     *
     * @param pos variável que indica a posição da peça na diagonal
     * @return retorna a peça presente na posição pos da diagonal (incrementando pos ao limiteLinha e decrementando pos ao limiteColuna)
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Peca peca(int pos) throws IndexOutOfBoundsException {
        int limiteLinha = linha, limiteColuna = coluna;
        if(pos < 0 || pos >= comprimento()) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }

        while (limiteLinha > 0 && limiteColuna < comprimento()-1){
            limiteColuna++;
            limiteLinha--;
        }

        return this.tabuleiro.peca(limiteLinha + pos, limiteColuna - pos);
    }
}
