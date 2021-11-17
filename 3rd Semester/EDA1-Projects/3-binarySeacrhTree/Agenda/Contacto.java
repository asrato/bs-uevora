package Agenda;

import ABPTree.ABP;

public class Contacto implements Comparable<Contacto> {
    public String id;
    public ABP<Integer> numeros = new ABP<>();

    public Contacto(String id, int numero) {
        this.id = id;
        this.numeros.insert(numero);
    }

    public void adicionarNumero(int numero) {
        this.numeros.insert(numero);
    }

    public void removerNumero(int numero) {
        this.numeros.remove(numero);
    }

    public int compareTo(Contacto contacto){
        return this.id.compareTo(contacto.id);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(this.id).append(" » ");
        for(Integer n : this.numeros)
            if(n != null)
                string.append(n.toString()).append(" ");
        return string.toString();
    }
}
