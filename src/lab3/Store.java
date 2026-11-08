package lab3;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Store {
    private final List<Product> products = new ArrayList<>();
    private int invalidLines = 0;

    public int readProducts(InputStream is) throws IOException {
        int read = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;

        while ((line = reader.readLine()) != null){
            if(line.trim().isEmpty()){
                continue;
            }

            try{
                products.add(Product.createProduct(line));
                read++;
            }catch (InvalidProductException e){
                invalidLines++;
            }
        }

        return read;
    }

    public int getInvalidLines() {
        return invalidLines;
    }

    public void printSorted(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        products.stream()
                .sorted()
                .forEach(writer::println);

        writer.flush();
    }

    public void printByCategory(OutputStream os, String category) throws CategoryNotFoundException {
        PrintWriter writer = new PrintWriter(os);

        List<Product> productsCategory = products.stream()
                .filter(product -> product.getCategory().equals(category))
                .sorted(new CompareCategory())
                .collect(Collectors.toList());
        if (productsCategory.isEmpty()) {
            throw new CategoryNotFoundException(category);
        }

        productsCategory.forEach(writer::println);

        writer.flush();

    }

    public void printStatistics(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        if (products.isEmpty()) {
            writer.println("No products");
            writer.flush();
            return;
        }

        int physical = (int) products.stream()
                .filter(product -> product.getTypeCode() == 'P')
                .count();
        int digital = (int) products.stream()
                .filter(product -> product.getTypeCode() == 'D')
                .count();
        double totPrice = products.stream().mapToDouble(Product::getFinalPrice).sum();
        Product cheapest = products.stream().min(Product::compareTo).orElseThrow();
        Product mostExpensive = products.stream().max(Product::compareTo).orElseThrow();

        writer.printf("Total: %d (physical: %d, digital: %d)%n", products.size(), physical,digital);
        writer.println("Cheapest: " + cheapest);
        writer.println("Most expensive: " + mostExpensive);
        writer.printf("Average price: %.2f%n", totPrice/products.size());

        writer.flush();




    }
}
