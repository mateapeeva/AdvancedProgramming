package lab3;


import java.util.Comparator;

class CompareCategory implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        if(String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()) == 0){

            if(Double.compare(o1.getFinalPrice(),o2.getFinalPrice()) == 0){
                return Integer.compare(o1.getId(),o2.getId());
            }

            return Double.compare(o2.getFinalPrice(),o1.getFinalPrice());
        }

        return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName());
    }
}

abstract class Product implements Comparable<Product> {
    private int id;
    private String name;
    private String category;
    private double basePrice;

    public Product(int id, String name, String category, double basePrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public abstract double getFinalPrice();
    public abstract char getTypeCode();

    public static Product createProduct(String line) throws InvalidProductException {
        try{
            String[] parts = line.split(";",-1);

            if(parts.length != 6){
                throw new InvalidProductException(line);
            }

            for(int i=0; i< parts.length; i++){
                parts[i] = parts[i].trim();
            }

            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            String category = parts[3];
            double basePrice = Double.parseDouble(parts[4]);
            double extra = Double.parseDouble(parts[5]);

            if(basePrice < 0 || extra <= 0){
                throw new InvalidProductException(line);
            }


            if(parts[0].equals("P")){
                return new PhysicalProduct(id,name,category,basePrice,extra);
            }else if(parts[0].equals("D")){
                int sizeMb = Integer.parseInt(parts[5]);
                return new DigitalProduct(id,name,category,basePrice, sizeMb);
            }else{
                throw new InvalidProductException(line);
            }




        }catch (NumberFormatException e){
            throw new InvalidProductException(line);
        }



    }

    @Override
    public int compareTo(Product other) {
        if(Double.compare(this.getFinalPrice(),other.getFinalPrice()) == 0){
            if(this.getName().compareTo(other.getName()) == 0){
                return Integer.compare(this.getId(), other.getId());
            }
            return this.getName().compareTo(other.getName());
        }

        return Double.compare(this.getFinalPrice(),other.getFinalPrice());

    }

    @Override
    public String toString() {
        return String.format("%c %-5d %-15s %-12s %10.2f", getTypeCode(), id, name, category, getFinalPrice());
    }
}
