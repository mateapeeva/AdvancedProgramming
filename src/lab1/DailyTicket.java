package lab1;

class DailyTicket extends Ticket {
    public DailyTicket(String plate, int minutes){
        super(plate,minutes);
    }

    public double price(){
        double number_days = Math.ceil(getMinutes() / 1440.0);
        double price = number_days*300;

        return price;
    }
}
