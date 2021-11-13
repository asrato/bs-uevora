import java.util.*;

/**
 * Classe pública (Gerador)
 * Classe utilizada para gerar configurações de tabuleiros, com certas restrições (número de rainhas, número de tabuleiros e tamanho dos mesmos).
 * Utiliza a bibilioteca java.util para gerar novos números e receber strings como input do utilizador (Random e Scanner, respetivamente).
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */
public class Gerador {

    /**
     * Método que produz n configurações aleatórias de q rainhas em tabuleiros de dimensão m*m.
     * Inicialmente são inicializadas 4 variáveis locais inteiras (tamanho, damas, tabuleiros e numero), com os valores recebidos como argumentos (m, q e n, respetivamente) e com 0.
     * De seguida é criada uma lista de Strings (lista).
     * Depois é criado um objetvo do tipo Random chamado gerador e um array de caracteres chamado string, de tamanho m*m.
     * É verificado, de seguida, se o número de rainhas é maior que o número de linhas. Se for o método retorna null.
     * Se não for, é executado um ciclo for para colocar todos os caracteres da string iguais a '-'.
     * Depois disso, executa um ciclo while que se encontra dentro de outro ciclo while.
     * O ciclo interior gera posições aleatórias na string (entre 0 e m*m) e coloca nessas posições o caractere 'D'.
     * Caso a posição já esteja ocupada por um 'D' volta a repetir o processo sem contabilizar a entrada de uma nova rainha.
     * Após a colocação das q rainhas ('D'), a string (array de caracteres) é convertida para String e é adicionada à lista de strings.
     * Depois de adicionada, a variável tabuleiros é decrementada, e a string tab (string auxiliar) é resetada e o array de caracteres string é preenchido novamente com '-'.
     * A variável damas é resposta novamente com o valor de q.
     * Assim que o valor de tabuleiros for 0 o ciclo while externo termina e o programa retorna a lista de strings com as configurações geradas.
     * Este método não verifica se as configurações se repetem, havendo a possibilidade de isso acontecer.
     *
     * @param m dimensão do tabuleiro
     * @param q número de rainhas
     * @param n número de tabuleiros a serem gerados
     * @return lista de configurações com as características recebidas como agumento
     */
    static List<String> random(int m, int q, int n) {
        int tamanho = m, damas = q, tabuleiros = n, numero = 0;
        List<String> lista = new ArrayList<String>();
        Random gerador = new Random();
        char[] string = new char[m*m];
        if (q > m) {
            return null;
        }
        for(int i = 0; i < m*m; i++){
            string[i] = '-';
        }
        while(tabuleiros>0){
            while(damas>0){
                numero = gerador.nextInt(m*m);
                if(string[numero] != 'D'){
                    string[numero] = 'D';
                    damas--;
                }
            }
            String tab = String.valueOf(string);
            lista.add(tab);
            tabuleiros--;
            tab = "";
            for(int i = 0; i < m*m; i++){
                string[i] = '-';
            }
            damas = q;
        }

        return lista;
    }

    /**
     * Função principal (main) responsável pelo funcionamento geral do programa Gerador.
     * Começa por declarar as variáveis inteiras tamanho, damas, numeroTabuleiros.
     * Depois cria um objeto da classe Scanner com o nome ler, de modo a poder ler as inputs do utilizador.
     * Após isso lê a string introduzida pelo usuário (até ao primeiro espaço encontrado).
     * Neste ponto, o programa executa um bloco if para verificar se essa string corresponde a random. Se não verificar o programa encerra automaticamente.
     * Se verificar a condição, é lindo o próximo inteiro da input od utilizador e este é guardado em tamanho. O mesmo é feito para a variável damas e para a variável numeroTabuleiros.
     * Depois disto, é criada uma lista de strings cujos valores serão aqueles que são retornados pelo método random, utilizando as três variáveis inteiras como argumentos (tamanho, damas, numeroTabuleiros).
     * De seguida, é dado print dos elementos dessas mesma lista utilizando um for que irá terminiar a sua execução quando não existirem mais itens na lista (enquanto String item : gr)
     *
     * @param args
     */
    public static void main(String[] args) {
        int tamanho, damas, numeroTabuleiros;
        Scanner ler = new Scanner(System.in);
        String string = ler.next();
        if(string.equals("random")) {
            tamanho = ler.nextInt();
            damas= ler.nextInt();
            numeroTabuleiros = ler.nextInt();
            List<String> gr = Gerador.random(tamanho, damas, numeroTabuleiros);
            for (String item : gr) {
                System.out.println(item);
            }
        }
    }
}
