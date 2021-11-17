/**
 * Classe implementada na Interface Fila.
 * Classe utilizada para a verificação dos vários aspetos de uma coluna (fila vertical).
 * Utiliza outras classes, tais como a classe pública Tabuleiro ou a classe pública Peca.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */


public class Coluna implements Fila {

    /**
     * Variável de instância que guarda o Tabuleiro que é recebido como argumento no construtor da classe, de modo a poder ser utilizado nos diversos métodos.
     */
    private Tabuleiro tabuleiro;

    /**
     * Variável de instância responsável por guardar o número da coluna, ou seja, o índice da coluna que está a ser analisada.
     */
    private int numero;


    /**
     * Construtor da classe Coluna, que guarda nas variáveis de instância da classe os valores recebidos como argumentos no construtor.
     *
     * @param t Tabuleiro a que a coluna pertence
     * @param n Número da coluna (índice)
     */
    public Coluna(Tabuleiro t, int n) {
        this.tabuleiro = t;
        this.numero = n;
    }


    /**
     * Método responsável pelo retorno do tamanho da coluna, utilizando o método getTamanho() da classe Tabuleiro.
     *
     * @return tamanho do tabuleiro pois o tamanho da coluna é igual tamanho de um dos lados do tabuleiro
     */
    @Override
    public int comprimento(){
        return this.tabuleiro.getTamanho();
    }

    /**
     * Método responsável pelo cálculo e pela obtenção do número de peças existentes na coluna.
     * O contador é inicializado a 0 pois, inicialmente não existe qualquer peca na coluna.
     * De seguida é iniciado um ciclo for que correrá todas as peças de um tabuleiro utilizando as coordenadas i, this.numero, em que i é a variação da posição das peças na coluna e this.numero é o índice da coluna.
     * Cada vez que numa posição do tabuleiro seja encontrada uma peça diferente da subclasse Nada, o contador é incrementado.
     *
     * @return o contador (número de peças diferentes de Nada)
     */
    @Override
    public int pecas(){
        int contador = 0;
        for (int i = 0; i < this.tabuleiro.getTamanho(); i++){
            if(!(this.tabuleiro.peca(i, this.numero) instanceof Nada)){
                contador++;
            }
        }
        return contador;
    }

    /**
     * Método responsável por retornar qual a peça que está na posição pos da coluna.
     *
     * @param pos variável que indica a posição da peça na coluna
     * @return tipo de peça presente na posição pos da coluna
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Peca peca(int pos) throws IndexOutOfBoundsException{
        return this.tabuleiro.peca(pos, this.numero);
    }
}
