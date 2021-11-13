package ABPTree;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteradorABP<Tipo> implements Iterator<Tipo> {
    public NoABP<Tipo> noAtual;
    public ArrayStack<NoABP<Tipo>> escolhas;

    public IteradorABP(NoABP<Tipo> no) {
        this.noAtual = no;
        this.escolhas = new ArrayStack<>();
    }

    public boolean hasNext() {
        return this.noAtual != null;
    }

    public Tipo next() {
        if(!hasNext())
            throw new NoSuchElementException();
        Tipo resultado = this.noAtual.elemento;
        if(this.noAtual.direito != null)
            this.escolhas.push(this.noAtual.direito);
        if(this.noAtual.esquerdo != null)
            this.noAtual = this.noAtual.esquerdo;
        else {
            if(!this.escolhas.empty())
                this.noAtual = this.escolhas.pop();
            else
                this.noAtual = null;
        }
        return resultado;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
