public class BigInt implements AstroInt {

    private boolean signal;
    private DoubleLinkedList<Integer> list = new DoubleLinkedList<>();

    public BigInt(String number) {
        if (number.charAt(0) == '-') { // sinal
            this.signal = true;
            number = number.substring(1);
        }
        while (number.charAt(0) == '0' && number.length() > 1) { // retirar zeros excessivos
            number = number.substring(1);
        }
        if (number.matches("^[0-9]*$")) {   // verificar se é string numérica e colocar na lista começando pelo algarismo das unidades
            for (int i = number.length() - 1; i >= 0; i--) {
                int digit = Character.getNumericValue(number.charAt(i));
                this.list.add(digit);
            }
        } else {
            throw new NumberFormatException();
        }
    }

    private int comparar(AstroInt x) {
        String a = this.toString();
        if (a.charAt(0) == '-') // remoção do sinal
            a = a.substring(1);
        String b = x.toString();
        if (b.charAt(0) == '-')
            b = b.substring(1);
        String c = a;
        String d = b;
        while (c.length() < d.length()) // colocar as strings com o mesmo comprimento
            c = "0" + c;
        while (c.length() > d.length())
            d = "0" + d;
        return c.compareTo(d);
    }

    public DoubleLinkedList<Integer> getList() {
        return this.list;
    }

    public boolean getSignal() {
        return this.signal;
    }

    public void setSignal(boolean signal) {
        this.signal = signal;
    }

    public AstroInt add(AstroInt x) {
        BigInt v1 = new BigInt(this.toString());
        BigInt v2 = new BigInt(x.toString());
        BigInt v3;
        BigInt maior;
        BigInt menor;
        boolean sinal;
        StringBuilder numero = new StringBuilder();
        Integer valorExtra = 0;
        if (v1.comparar(v2) > 0) {  //verificar qual o BigInt que tem o maior valor absoluto
            maior = new BigInt(v1.toString());
            menor = new BigInt(v2.toString());
        } else {
            maior = new BigInt(v2.toString());
            menor = new BigInt(v1.toString());
        }
        if (v1.getSignal() == v2.getSignal()) { // seos sinais forem iguais, somam-se os valores e dá-se o mesmo sinal
            sinal = v1.getSignal();
            for (int i = 1; i <= menor.getList().size(); i++) {
                Integer a = v1.getList().get(i);
                if (a == null)
                    a = 0;
                Integer b = v2.getList().get(i);
                if (b == null)
                    b = 0;
                Integer soma = (a + b + valorExtra) % 10;
                valorExtra = (a + b + valorExtra) / 10;
                numero.append(soma);
            }
            for (int i = menor.getList().size() + 1; i <= maior.getList().size(); i++) {
                Integer a = maior.getList().get(i);
                Integer soma = (a + valorExtra) % 10;
                valorExtra = (a + valorExtra) / 10;
                numero.append(soma);
            }
            if (valorExtra != 0) {
                numero.append(valorExtra);
            }
        } else {    // se os sinais forem diferentes, subtraem-se e dá-se o sinal do maior, em valor absoluto
            sinal = maior.getSignal();
            for (int i = 1; i <= menor.getList().size(); i++) {
                Integer a = maior.getList().get(i);
                Integer b = menor.getList().get(i) + valorExtra;
                if (a < b) {
                    a = a + 10;
                    valorExtra = 1;
                } else {
                    valorExtra = 0;
                }
                Integer soma = a - b;
                numero.append(soma);
            }
            for (int i = menor.getList().size() + 1; i <= maior.getList().size(); i++) {
                Integer a = maior.getList().get(i);
                Integer b = valorExtra;
                if (a == null)
                    a = 0;
                if (a < b) {
                    a = a + 10;
                    valorExtra = 1;
                } else {
                    valorExtra = 0;
                }
                Integer soma = a - b;
                numero.append(soma);
            }
            if (valorExtra != 0) {
                numero.append(valorExtra);
            }
        }
        numero = numero.reverse();
        v3 = new BigInt(numero.toString());
        v3.setSignal(sinal);
        return v3;
    }

    @Override
    public AstroInt sub(AstroInt x) {
        BigInt v1 = new BigInt(this.toString());
        BigInt v2 = new BigInt(x.toString());
        BigInt v3;
        BigInt maior;
        BigInt menor;
        boolean sinal;
        StringBuilder numero = new StringBuilder();
        Integer valorExtra = 0;
        if (v1.comparar(v2) > 0) {
            maior = new BigInt(v1.toString());
            menor = new BigInt(v2.toString());
        } else {
            maior = new BigInt(v2.toString());
            menor = new BigInt(v1.toString());
        }
        if (v1.getSignal() == v2.getSignal()) { // se os sinais forem iguais, subtraem-se os valores e dá-se o sinal do maior em valor absoluto
            if (v1.comparar(v2) > 0) {
                sinal = v1.getSignal();
            } else {
                sinal = !v1.getSignal();
            }
            for (int i = 1; i <= menor.getList().size(); i++) {
                Integer a = maior.getList().get(i);
                Integer b = menor.getList().get(i) + valorExtra;
                if (a < b) {
                    a = a + 10;
                    valorExtra = 1;
                } else
                    valorExtra = 0;
                Integer diferenca = a - b;
                numero.append(diferenca);
            }
            for (int i = menor.getList().size() + 1; i <= maior.getList().size(); i++) {
                Integer a;
                try {
                    a = v1.getList().get(i);
                } catch (Exception e) {
                    a = 0;
                }
                Integer b;
                try {
                    b = v2.getList().get(i);
                } catch (Exception e) {
                    b = 0;
                }
                if (b == null)
                    b = 0;
                if (a == null)
                    a = 0;
                b += valorExtra;
                if (a < b) {
                    a = a + 10;
                    valorExtra = 1;
                } else
                    valorExtra = 0;
                Integer diferenca = a - b;
                numero.append(diferenca);
            }
            if (valorExtra != 0) {
                numero.append(valorExtra);
            }
            numero = numero.reverse();
        } else {    // se os sinais forem diferentes, somam-se os valores e dáse o valor do maior em valor absoluto
            sinal = maior.getSignal();
            v1.setSignal(false);
            v2.setSignal(false);
            BigInt soma = new BigInt(v1.add(v2).toString());
            numero.append(soma);
        }
        v3 = new BigInt(numero.toString());
        v3.setSignal(sinal);
        return v3;
    }

    @Override
    public AstroInt mult(AstroInt x) {
        if (x.toString().equals("0") || this.toString().equals("0")) {  // se qualquer um dos doisvalores for 0 o return é 0
            return new BigInt("0");
        }
        BigInt v1 = new BigInt(this.toString());
        BigInt v2 = new BigInt(x.toString());
        int size = v1.getList().size() + v2.getList().size();
        BigInt[] array = new BigInt[size];  // array que guarda os valores que serão somados
        boolean sinal = !(v1.getSignal() == v2.getSignal());
        Integer valorExtra = 0;
        StringBuilder numero = new StringBuilder();
        int ar = 0;
        for (int j = 1; j <= v1.getList().size(); j++) {
            numero = new StringBuilder();
            int z = 0;
            while (z < j - 1) { // multiplicar por 10
                numero.append("0");
                z++;
            }
            for (int i = 1; i <= v2.getList().size(); i++) {
                Integer a = v2.getList().get(i);
                Integer b = v1.getList().get(j);
                Integer produto = (a * b + valorExtra) % 10;
                valorExtra = (a * b + valorExtra) / 10;
                numero.append(produto);
            }
            if(valorExtra != 0) {
                numero.append(valorExtra);
            }
            numero = numero.reverse();
            BigInt toArray = new BigInt(numero.toString());
            array[ar] = toArray;
            ar++;
            z = 0;
        }
        BigInt valor = new BigInt("0");
        for (int i = 0; i < array.length; i++) {    // soma dos valores presentes no array
            if (array[i] != null) {
                BigInt y = array[i];
                String res = valor.add(y).toString();
                valor = new BigInt(res);
            }
        }
        valor.setSignal(sinal);
        return valor;
    }

    @Override
    public AstroInt div(AstroInt x) {
        BigInt dividendo = new BigInt(this.toString());
        BigInt divisor = new BigInt(x.toString());
        BigInt i = new BigInt("1");
        BigInt um = new BigInt("1");
        boolean sinal = false;
        if(divisor.toString().equals("0"))  // divisão por 0 é impossível
            return null;
        if(dividendo.getSignal() != divisor.getSignal())    // se sinais diferentes, o resultado é negativo
            sinal = true;
        BigInt res = new BigInt(divisor.mult(i).toString());
        while(res.comparar(dividendo) <= 0) {   // multiplicar o divisor por um interador até que o valor seja maior que o dividendo
            i = new BigInt(i.add(um).toString());
            res = new BigInt(divisor.mult(i).toString());
        }
        i = new BigInt(i.sub(um).toString());   // decrementação do iterador para que volte ao valor correto do quociente
        BigInt resultado = new BigInt(i.toString());
        resultado.setSignal(sinal);
        return resultado;
    }

    @Override
    public AstroInt mod(AstroInt x) {   // dividendo = quociente * divisor + resto <=> resto = dividendo - (quociente * divisor)
        BigInt v1 = new BigInt(this.toString());
        BigInt v2 = new BigInt(x.toString());
        BigInt divi = new BigInt(v1.div(v2).toString());
        BigInt valor = new BigInt(v2.mult(divi).toString());
        BigInt resultado = new BigInt(v1.sub(valor).toString());
        resultado.setSignal(signal);
        return resultado;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        if (this.list.size() == 1 && this.list.get(1) == 0) {
            return "0";
        }
        if (this.signal) {
            string.append("-");
        }
        for (int i = this.list.size(); i > 0; i--) {
            string.append(this.list.get(i));
        }
        return string.toString();
    }
}