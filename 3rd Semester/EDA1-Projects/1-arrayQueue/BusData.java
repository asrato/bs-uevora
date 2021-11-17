public class BusData {
    private int hours;
    private int minutes;
    private int number;

    public BusData(int hours, int minutes, int number) {
        this.hours = hours;
        this.minutes = minutes;
        this.number = number;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        if (this.number == 1) {
            return this.hours + ":" + this.minutes + " Grupo " + this.number + " pessoa";
        } else {
            return this.hours + ":" + this.minutes + " Grupo " + this.number + " pessoas";
        }
    }
}
