import java.lang.reflect.Modifier;
import java.util.Scanner;

/**
 * Classe pública (Validador)
 * Classe utilizada para avaliar a validade ou não de diferentes configurações de tabuleiros.
 * Utiliza a biblioteca java.util e java.lang.
 *
 * @author André Rato e Jorge Salsinha
 * @version 1.0
 */
public class Validador {

    /**
     * Função principal (main) responsável pelo funcionamento geral do programa Validador.
     * Ese programa funciona de dois modos:
     * > Validador Individual: onde o programa lê uma configuração de uma linha só e escreve apenas se é válida ou inválida;
     * > Filtro Validador: onde o programa lê várias configurações e reescreve as que são válidas.
     * Começa por criar um objeto de leitura para poder receber a input do utilizador.
     * Depois declara uma variável de controlo e coloca-a como falsa. Declara por fim a string configuração.
     * Caso o array de strings que é passado como argumento da função main foi diferente dos predefinidos, o programa apresenta a mensagem "Argumento Errado".
     *
     * Sub-método - Validador Individual
     * Lê a input do utilizador, criando um Tabuleiro com essa input.
     * Percorrendo o tabuleiro e recorrendo aos métodos peca(linha, coluna) e ameacada(linha, coluna) verifica se existem Rainhas ameaçadas.
     * Caso existam o programa dá print de "INVALIDA". Caso não exsitam Rainhas ameaçadas, o programa dá print de "VALIDA".
     *
     * Sub-método - Filtro Validador
     * Lê consecutivamente a input do utilizador.
     * Este programa recorre ao mesmo estilo de análise da configuração que o sub-método anterior.
     * Se a configuração for válida o programa dá print da configuração. Se for inválida não dá print.
     *
     * @param args se args.length == 0 executa o validador individual; se args[0].equals("filtro") executa o filtro validador.
     */
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        boolean controlo = false;
        String configuracao;
        if (args.length == 0) {
            configuracao = ler.nextLine();
            Tabuleiro tabuleiro = new Tabuleiro(configuracao);

            for (int i = 0; i < tabuleiro.getTamanho(); i++) {
                for (int j = 0; j < tabuleiro.getTamanho(); j++) {
                    if (tabuleiro.peca(i, j) instanceof Rainha) {
                        if (tabuleiro.ameacada(i, j)) {
                            controlo = true;
                            break;
                        }
                    }
                }
            }

            if (controlo) {
                System.out.print("INVALIDA");
            } else {
                System.out.print("VALIDA");
            }

        } else if (args[0].equals("filtro")) {
            while (ler.hasNextLine()) {

                configuracao = ler.nextLine();
                Tabuleiro tab = new Tabuleiro(configuracao);
                for (int i = 0; i < tab.getTamanho(); i++) {
                    for (int j = 0; j < tab.getTamanho(); j++) {
                        if (tab.peca(i, j) instanceof Rainha) {
                            if (tab.ameacada(i, j)) {
                                controlo = true;
                                break;
                            }
                        }
                    }
                }

                if (!controlo) {
                    System.out.println(tab);
                }
                controlo = false;
            }


        } else {
            System.out.println("Argumento Errado!");
        }

    }
}