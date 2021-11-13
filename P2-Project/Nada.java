/**
 * Subclasse da classe Peca
 * Classe utilizada para atribuir característica a uma peça.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */
public class Nada extends Peca {

    /**
     * Construtor da classe Nada.
     * Utilizada o construtor base da classe super (Peca).
     *
     * @param tab tabuleiro à qual a peça pertence
     * @param linha linha onde a peça está localizada
     * @param coluna coluna onde a peça está localizada
     */
    public Nada(Tabuleiro tab, int linha, int coluna) {
        super(tab, linha, coluna);
    }

    /**
     * Método responsável por verificar se o movimento da peça é possível.
     *
     * @param linha linha destino
     * @param coluna coluna destino
     * @return false, pois uma peca Nada funciona como uma casa do tabuleiro vazia
     */
    @Override
    public boolean podeIrPara(int linha, int coluna) {
        return false;
    }
}
