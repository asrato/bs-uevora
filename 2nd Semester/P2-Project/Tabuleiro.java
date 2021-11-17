/**
 * Classe pública (Tabuleiro)
 * Classe utilizada para tratar o tabuleiro e executar todas as operações possíveis relacionadas ao mesmo.
 * Utiliza outras classes, como por exemplo a classe Peca.
 * Utiliza a interface Fila.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */
public class Tabuleiro {

    /**
     * Variável de instância que guarda a configuração do tabuleiro em formato string.
     */
    private String string;

    /**
     * Variável de instância que guarda o tamanho do tabuleiro.
     */
    private int tamanho;

    /**
     * Array de arrays de peças (Peca) -> tabuleiro.
     */
    private Peca[][] tabuleiro;


    /**
     * Construtor da calsse Tabuleiro. Recebe a configuração do tabuleiro, remove os espaços nessa configuração e calcula o tamanho do tabuleiro utilizando algumas fórmulas matemáticas (raíz quadrada).
     * Depois disso cria o tabuleiro inicializando o array de arrays declarado globalmente na classe com o tamanho obtido anteriormente.
     * De seguida, são percorridas todas as posições do tabuleiro e a estas posições são associadas peças consoante o caractere em questão. Se o caractere lido for '-' coloca a peça Nada nessa posição do tabuleiro. Se for 'D' coloca a peça Rainha nessa posição do tabuleiro.
     *
     * @param string configuração do tabuleiro
     */
    public Tabuleiro(String string) {
        int indice = 0;
        char caractere = 0;
        this.string = string.replaceAll(" ", "");

        tamanho = (int) Math.sqrt(this.string.length());
        tabuleiro = new Peca[tamanho][tamanho];

        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                caractere = this.string.charAt(indice);
                indice++;
                if (caractere == 'D') {
                    tabuleiro[linha][coluna] = new Rainha(this, linha, coluna);
                } else {
                    tabuleiro[linha][coluna] = new Nada(this, linha, coluna);
                }
            }
        }
    }


    /**
     * Método que devolve o tamanho do tabuleiro.
     *
     * @return tamanho do tabuleiro calculado no construtor
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Método que transforma o tabuleiro numa string.
     * Se a raíz quadrada do tamanho da configuração for igual ao tamanho do tabuleiro a função devolve a configuração. Se não devolve uma string vazia.
     *
     * @return String
     */
    @Override
    public String toString() {
        if(Math.sqrt(string.length())==tamanho) {
            return this.string;
        }
        return "";
    }

    /**
     * Método que devolve o tipo de peça que está presente na posição recebida pelo argumento.
     * Utiliza um try catch para verificar se a localização é válida ou não.
     *
     * @param linha linha da peça
     * @param coluna coluna da peça
     * @return tipo de peça (null se não existir)
     */
    public Peca peca(int linha, int coluna) {
        Peca controlo = null;
        try{
            controlo = tabuleiro[linha][coluna];
        } catch (IndexOutOfBoundsException e){
            controlo = new Nada(this, linha, coluna);
        }
        return controlo;
    }

    /**
     * Método que verifica se uma peça é ameaçada por outras.
     * Utiliza a interface para verificar quantas peças estão na mesma fila, coluna e diagonais que a peça introduzida.
     *
     * @param linha linha da peça
     * @param coluna coluna de peça
     * @return (l.pecas() + c.pecas() + dd.pecas() + da.pecas()) > 4 -> caso seja ameaçada retorna true
     */
    public boolean ameacada(int linha, int coluna) {
        Linha l = new Linha(this, linha);
        Coluna c = new Coluna(this, coluna);
        DiagonalDescendente dd = new DiagonalDescendente(this, linha, coluna);
        DiagonalAscendente da = new DiagonalAscendente(this, linha, coluna);
        return (l.pecas() + c.pecas() + dd.pecas() + da.pecas()) > 4;
    }

    /**
     * Método relacionada à linha da peça.
     *
     * @param linha índice da linha
     * @return novo objeto da classe Linha
     */
    public Linha linha(int linha){
        return new Linha(this,linha);
    }

    /**
     * Método relacionada à coluna da peça.
     *
     * @param coluna índice da coluna
     * @return novo objeto da classe Coluna
     */
    public Coluna coluna(int coluna){
        return new Coluna(this,coluna);
    }

    /**
     * Método relacionada à diagonal ascendente da peça.
     *
     * @param linha índice da linha
     * @param coluna índice da coluna
     * @return novo objeto da classe DiagonalAscendente
     */
    public DiagonalAscendente diagonalAscendente (int linha, int coluna){
        return new DiagonalAscendente(this, linha, coluna);
    }

    /**
     * Método relacionada à diagonal descendente da peça.
     *
     * @param linha índice da linha
     * @param coluna índice da coluna
     * @return novo objeto da classe DiagonalDescendente
     */
    public DiagonalDescendente diagonalDescendente (int linha, int coluna){
        return new DiagonalDescendente(this, linha, coluna);
    }
}
