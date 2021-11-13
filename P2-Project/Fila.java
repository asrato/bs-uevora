/**
 * Interface Fila
 * Interface utilizada para caracterizar um constituinte do Tabuleiro.
 * Utiliza outras classes, tais como a classe pública Tabuleiro ou a classe públic Peca.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */

public interface Fila {

    /**
     * Método responsável pelo retorno do comprimento da fila.
     *
     * @return inteiro
     */
    int comprimento();

    /**
     * Método responsável pelo retorno do número de peças na fila (!(peca instanceof Nada)).
     *
     * @return inteiro
     */
    int pecas();

    /**
     * Método responsável pelo retorno da peça que está na posição pos da fila.
     *
     * @param pos posição da peça na fila (varia entre 0 e comprimento())
     * @return Peca
     * @throws IndexOutOfBoundsException
     */
    Peca peca(int pos) throws IndexOutOfBoundsException;
}
