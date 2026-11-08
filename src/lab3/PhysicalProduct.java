package lab3;

class PhysicalProduct extends Product {

    private double weight;

    public PhysicalProduct(int id, String name, String category, double basePrice, double weight) {
        super(id, name, category, basePrice);
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getShippingCost() {
        double cost = 0;
        double w = this.getWeight();
        if (w <= 1){
            cost = 60;
        }else{
            cost = 60 + 30*(w-1);
        }

        return cost;
    }

    @Override
    public double getFinalPrice() {
        return getShippingCost()+getBasePrice();
    }

    @Override
    public char getTypeCode() {
        return 'P';
    }

    public String toString() {
        return super.toString() + String.format(" | %.2f kg", weight);
    }
}
