package ABPTree;

import java.util.Iterator;

public class ABP<Tipo extends Comparable<? super Tipo>> implements Iterable<Tipo> {
    public NoABP<Tipo> raiz;

    public ABP(){
        this.raiz = null;
    }

    public ABP(Tipo x) {
        this.raiz = new NoABP<>(x);
    }

    public ABP(NoABP<Tipo> no) {
        this.raiz = no;
    }

    public ABP(Tipo x, NoABP<Tipo> esq, NoABP<Tipo> dir) {
        this.raiz = new NoABP<>(x, esq, dir);
    }

    public boolean empty() {
        return this.raiz == null;
    }

    public void print() {
        print(this.raiz, 0);
    }

    private String ntabs(int t) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < t; i ++)
            string.append("\t");
        return string.toString();
    }

    private void print(NoABP<Tipo> no, int t) {
        if(no != null) {
            System.out.println(ntabs(t) + no);
            print(no.esquerdo, t + 1);
            print(no.direito, t + 1);
        }
    }

    public Tipo findMin() {
        if(empty())
            return null;
        return findMin(this.raiz);
    }

    private Tipo findMin(NoABP<Tipo> n) {
        if(n.esquerdo == null)
            return n.elemento;
        else return findMin(n.esquerdo);
    }

    public boolean contains(Tipo x, NoABP<Tipo> n) {
        if(n == null)
            return false;
        else {
            if(n.elemento.compareTo(x) < 0)
                return contains(x, n.direito);
            else if(n.elemento.compareTo(x) > 0)
                return contains(x, n.esquerdo);
            else
                return true;
        }
    }

    public void insert(Tipo x) {
        this.raiz = insert(x, this.raiz);
    }

    private NoABP<Tipo> insert(Tipo x, NoABP<Tipo> n) {
        if(n == null)
            n = new NoABP<>(x, null, null);
        else if ((n.elemento).compareTo(x) > 0)
            n.esquerdo = insert(x, n.esquerdo);
        else if((n.elemento).compareTo(x) < 0)
            n.direito = insert(x, n.direito);
        return n;
    }

    public void remove(Tipo x) {
        this.raiz = remove(x, this.raiz);
    }

    private NoABP<Tipo> remove(Tipo x, NoABP<Tipo> n) {
        if(n == null)
            return null;
        if(n.elemento.compareTo(x) < 0)
            n.direito = remove(x, n.direito);
        else if (n.elemento.compareTo(x) > 0)
            n.esquerdo = remove(x, n.esquerdo);
        else if(n.esquerdo != null && n.direito != null) {
            Tipo minimo = findMin(n.direito);
            n.elemento = minimo;
            n.direito = remove(minimo, n.direito);
        } else if (n.esquerdo == null)
            n = n.direito;
        else
            n = n.esquerdo;
        return n;
    }

    public Iterator<Tipo> iterator() {
        return new IteradorABP<>(this.raiz);
    }
}
