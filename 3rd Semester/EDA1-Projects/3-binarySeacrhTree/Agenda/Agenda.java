package Agenda;

import ABPTree.ABP;

import java.util.Iterator;

public class Agenda {
    private ABP<Contacto> agenda = new ABP<Contacto>();

    public void adicionar(String id, int numero) {
        Contacto contacto = this.procurarId(id);
        if(contacto == null)
            this.agenda.insert(new Contacto(id, numero));
        else {
            if(!this.contemNumero(contacto, numero))
                contacto.numeros.insert(numero);
        }            
    }

    private Contacto procurarId(String id) {
        for (Contacto c : this.agenda)
            if (c.id.equals(id))
                return c;
        return null;
    }

    private Contacto procurarNumero(int numero) {
        for (Contacto c : this.agenda)
            for (Integer n : c.numeros)
                if (n == numero)
                    return c;
        return null;
    }

    private boolean contemNumero(Contacto contacto, int numero) {
        for(Integer n: contacto.numeros)
            if(n == numero)
                return true;
        return false;
    }

    public void editar(String id, String novoId) {
        Contacto c = procurarId(id);
        if (c != null)
            c.id = novoId;
    }

    public void editar(int numero, String novoId) {
        Contacto c = procurarNumero(numero);
        if (c != null)
            c.id = novoId;
    }

    public void editar(String id, int novoNumero) {
        Contacto c = procurarId(id);
        if (c != null)
            c.adicionarNumero(novoNumero);
    }

    public void editar(int numero, int novoNumero) {
        Contacto c = procurarNumero(numero);
        if (c != null) {
            c.removerNumero(numero);
            c.adicionarNumero(novoNumero);
        }
    }

    public void remover(String id) {
        Contacto c = procurarId(id);
        this.agenda.remove(c);
    }

    public void remover(int numero) {
        Contacto c = procurarNumero(numero);
        this.agenda.remove(c);
    }

    public void listar() {
        this.ordenar();
        for(Contacto c : this.agenda)
            System.out.println(c);
    }

    private void ordenar() {
        ABP<Contacto> ordenado = new ABP<>();
        Contacto contacto = this.agenda.findMin();
        while(contacto != null) {
            this.agenda.remove(contacto);
            ordenado.insert(contacto);
            contacto = this.agenda.findMin();
        }
        this.agenda = ordenado;
    }

    public void chamador(int numero) {
        Contacto c = procurarNumero(numero);
        if(c == null)
            System.out.println("DESCONHECIDO");
        else
            System.out.println(c.id);
    }

    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        agenda.adicionar("Sam The Kid", 123);
        agenda.adicionar("Nininho Vaz Maia", 321);
        agenda.adicionar("Maria Leal", 456);
        agenda.adicionar("Trump", 654);
        agenda.listar();
        System.out.println("\n----------\n - Edição e Remoção:\n");
        agenda.editar("Sam The Kid", "Ana");
        agenda.editar(123, 987);
        agenda.remover("Trump");
        agenda.editar("Maria Leal", 420);
        agenda.listar();
        System.out.println("\n----------\n - Chamador:\n");
        agenda.chamador(321);
        agenda.chamador(0000);
    }
}