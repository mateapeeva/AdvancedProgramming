package lab3;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String line){
        super("Category " + line + " not found");
    }
}
