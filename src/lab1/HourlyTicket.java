package lab1;

class HourlyTicket extends Ticket {
    public HourlyTicket(String plate, int minutes){
        super(plate,minutes);
    }

    public double price(){
        double number_hours = Math.ceil(getMinutes()/60.0);
        double price = number_hours*50;

        return price;
    }
}
