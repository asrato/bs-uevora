package ABPTree;

public class NoABP<Tipo> {
    public Tipo elemento;
    public NoABP<Tipo> esquerdo;
    public NoABP<Tipo> direito;

    public NoABP(Tipo x) {
        this.elemento = x;
        this.esquerdo = null;
        this.direito = null;
    }

    public NoABP(Tipo x, NoABP<Tipo> esq, NoABP<Tipo> dir) {
        this.elemento = x;
        this.esquerdo = esq;
        this.direito = dir;
    }

    public String toString() {
        return this.elemento.toString();
    }
}
