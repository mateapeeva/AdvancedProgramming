package lab3;

class DigitalProduct extends Product {
    private int sizeMb;
    public DigitalProduct(int id, String name, String category, double basePrice, int sizeMb) {
        super(id, name, category, basePrice);
        this.sizeMb = sizeMb;
    }

    public int getSizeMb() {
       return this.sizeMb;
    }

    @Override
    public double getFinalPrice() {
        return this.getBasePrice()*0.9;
    }

    @Override
    public char getTypeCode() {
        return 'D';
    }

    public String toString() {
        return super.toString() + String.format(" | %d MB", sizeMb);
    }
}
