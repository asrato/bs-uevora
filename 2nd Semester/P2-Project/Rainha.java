/**
 * Subclasse da classe Peca
 * Classe utilizada para atribuir característica a uma peça.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */
public class Rainha extends Peca {
    int tamanho;

    /**
     * Construtor da classe Rainha.
     * Utilizada o construtor base da classe super (Peca).
     *
     * @param tab tabuleiro à qual a peça pertence
     * @param linha linha onde a peça está localizada
     * @param coluna coluna onde a peça está localizada
     */
    public Rainha(Tabuleiro tab, int linha, int coluna) {
        super(tab, linha, coluna);
        this.tamanho = tab.getTamanho();
    }


    /**
     * Método responsável por verificar se o movimento da peça é possível.
     * Primeiro verifica se a linha ou a coluna da peça são iguais, ou à linha destino, ou à coluna destino, ou a ambas. Se sim o método retorna true.
     * Depois verifica as diagonais, percorrendo as 4 sub-diagonais. Se a linha e coluna da peça da diagonal coincidirem com a linha e a coluna da peça em si, retorna true.
     *
     * @param linha linha destino
     * @param coluna coluna destino
     * @return boolean -> true se for possível ir, false se não for possível
     */
    @Override
    public boolean podeIrPara(int linha, int coluna) {
        if (linha == this.linha() || coluna == this.coluna()) {
            return true;
        }
        /**
         * Diagonal Ascendente - Direita
         */
        for(int i = this.linha(), j = this.coluna(); i>=0 && j<this.tamanho; i--, j++){
            if(i == linha && j == coluna){
                return true;
            }
        }

        /**
         * Diagonal Ascendente - Esquerda
         */
        for(int i = this.linha(), j = this.coluna(); i<this.tamanho && j>=0; i++, j--){
            if(i == linha && j == coluna){
                return true;
            }
        }

        /**
         * DiagonalDescendente - Direita
         */
        for(int i = this.linha(), j = this.coluna(); i<this.tamanho && j<this.tamanho; i++, j++){
            if(i == linha && j == coluna){
                return true;
            }
        }

        /**
         * Diagonal Descendente - Esquerda
         */
        for(int i = this.linha(), j = this.coluna(); i>=0 && j>=0; i--, j--){
            if(i == linha && j == coluna){
                return true;
            }
        }

        return false;
    }
}
