package lab2;

import java.util.Collections;
import java.util.List;

class ValidationResult {

    private final List<String> errors;

    ValidationResult(List<String> errors){
        this.errors = Collections.unmodifiableList(List.copyOf(errors));
    }

    public static ValidationResult ok(){
        return new ValidationResult(List.of());
    }
    public static ValidationResult error(String message) throws IllegalArgumentException{
        if (message == null || message.isEmpty()){
            throw new IllegalArgumentException();
        }

        return new ValidationResult(List.of(message));
    }

    public static ValidationResult of(List<String> errors) throws IllegalArgumentException{
        if (errors == null){
            throw new IllegalArgumentException();
        }

        return new ValidationResult(List.copyOf(errors));
    }

    public boolean isValid(){
        return errors.isEmpty();
    }

    public List<String> getErrors(){
        return errors;
    }
}
