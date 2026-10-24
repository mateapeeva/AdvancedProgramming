package lab1;

class ParkingLot {
    private Ticket[] tickets;
    private int ticketCount;
    private int capacity;

    public ParkingLot(int capacity) throws IllegalArgumentException{

        if (capacity <= 0){
            throw new IllegalArgumentException();
        }

        tickets = new Ticket[capacity];
        this.capacity = capacity;
        ticketCount = 0;
    }

    public void addTicket(Ticket ticket) throws LotFullException, IllegalArgumentException{
        if (ticket == null){
            throw new IllegalArgumentException();
        } else if (ticketCount == capacity) {
            throw new LotFullException();
        }

        tickets[ticketCount++] = ticket;
    }

    public int getTicketCount(){
        return ticketCount;
    }

    public double totalRevenue(){
        double totalRev = 0.0;
        for (int i = 0; i < ticketCount; i++) {
            totalRev += tickets[i].price();
        }

        return totalRev;
    }

}
