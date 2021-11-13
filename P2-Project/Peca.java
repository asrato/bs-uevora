/**
 * Classe abstrata Peca
 * Classe utilizada para caracterizar cada uma das posições do tabuleiro.
 * Utiliza outras classes, tais como o Tabuleiro.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */
public abstract class Peca {

    /**
     * Variáveis de instência:
     * linha - guarda a linha onde a peça está
     * coluna - guarda a coluna onde a peça está
     */
    private int linha, coluna;


    /**
     * Construtor da classe Peca, que guarda nas variáveis this.linha e this.coluna os valores dos argumentos.
     *
     * @param tab tabuleiro onde a peça é inserida
     * @param linha linha da peça
     * @param coluna coluna da peça
     */
    public Peca(Tabuleiro tab, int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }


    /**
     * Método que devolve a linha da peça.
     *
     * @return linha da peça
     */
    public int linha() {
        return this.linha;
    }

    /**
     * Método que devolve a coluna da peça.
     *
     * @return coluna da peça
     */
    public int coluna() {
        return this.coluna;
    }

    /**
     * Método abstrato que verifica os movimentos das peças.
     *
     * @param linha linha destino
     * @param coluna coluna destino
     * @return boolean (true se for possível, false se não for)
     */
    public abstract boolean podeIrPara(int linha, int coluna);

    /**
     * Método que verifica se a peça ataca ou não uma outra peça.
     *
     * @param vitima peça que será, ou não, atacada
     * @return boolean -> podeIrPara(vitima.linha, vitima.coluna)
     */
    public final boolean ataca(Peca vitima) {
        return podeIrPara(vitima.linha, vitima.coluna);
    }

    /**
     * Método que verifica se, no tabuleiro, existe peça ou não
     *
     * @return true se a Peca for instanciada pela subclasse Nada, false se não for
     */
    public boolean vazia() {
        return this instanceof Nada;
    }
}
