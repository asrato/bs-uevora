import CostumExceptions.EmptyException;
import CostumExceptions.FullException;

public class BusStop {
    private ArrayQueue<BusData> fila;
    private int elapsedTime = 0;
    private int totalPeopleInQueue = 0;
    private int counter = 0;

    public BusStop() {
        this.fila = new ArrayQueue<>();
    }

    public BusStop(int size) {
        this.fila = new ArrayQueue<>(size);
    }

    public void chega_grupo(int hours, int minutes, int people) throws FullException {
        BusData busData = new BusData(hours, minutes, people);
        this.fila.enqueue(busData);
        this.totalPeopleInQueue += people;
        System.out.println("Hora " + hours + ":" + minutes + " " + people + " pessoas chegam, ficam " + this.totalPeopleInQueue + " na fila");

    }

    public void chegada_bus(int hours, int minutes, int emptySeats) throws EmptyException {
        System.out.println("Chega Bus -> " + hours + ":" + minutes + " há " + emptySeats + " vagas no bus");

        while (emptySeats != 0 && !this.fila.empty()) {
            int temp = this.fila.front().getNumber();
            this.elapsedTime += (calcTime(hours, minutes, this.fila.front().getHours(), this.fila.front().getMinutes()));

            if (emptySeats == this.fila.front().getNumber()) {
                emptySeats = 0;
                this.fila.front().setNumber(0);
            } else if (emptySeats > this.fila.front().getNumber()) {
                emptySeats -= this.fila.front().getNumber();
                this.fila.front().setNumber(0);
            } else {
                this.fila.front().setNumber(this.fila.front().getNumber() - emptySeats);
                emptySeats = 0;
            }

            System.out.println("Hora " + this.fila.front().getHours() + ":" + this.fila.front().getMinutes() + " chegaram " + temp + " ficam " + this.fila.front().getNumber());

            if (this.fila.front().getNumber() == 0) {

                this.fila.dequeue();
            }

            this.counter++;

        }
        System.out.println("Tempo médio de espera: " + average(this.elapsedTime, this.counter) + "m");
        this.counter = 0;
        this.elapsedTime = 0;
    }

    private long calcTime(int busHours, int busMinutes, int groupHours, int groupMinutes) {
        long busTime = busHours * 60 + busMinutes;
        long groupTime = groupHours * 60 + groupMinutes;
        return busTime - groupTime;
    }

    private int average(int time, int n) {
        return time / n;
    }

    public static void main(String[] args) throws EmptyException, FullException {
        BusStop b23 = new BusStop();
        b23.chega_grupo(14, 14, 3);
        b23.chega_grupo(14, 18, 2);
        System.out.println(b23.fila);
        b23.chegada_bus(14, 29, 4);
        b23.chegada_bus(14, 40, 10);
    }
}