package lab2;

import java.util.ArrayList;
import java.util.List;

class UserValidation {
    public static ValidationResult validate(User user) throws IllegalArgumentException{

        if(user == null) throw new IllegalArgumentException();

        List<String> allErrors = new ArrayList<>();


        Validator<String> nameValidator = Validator.<String>notNull()
                .and(Validator.minLength(3));

        ValidationResult nameResult = nameValidator.validate(user.getName());
        if(!nameResult.isValid()){
            for (String err : nameResult.getErrors()){
                allErrors.add("name: " + err);
            }
        }


        Validator<Integer> ageValidator = Validator.<Integer>notNull()
                .and(Validator.range(18,120));

        ValidationResult ageResult = ageValidator.validate(user.getAge());
        if(!ageResult.isValid()){
            for(String err : ageResult.getErrors()){
                allErrors.add("age: " + err);
            }
        }

        Validator<String> emailValidator = Validator.<String>notNull()
                .and(Validator.minLength(5))
                        .and(value -> {
                            if(!value.contains("@")) return ValidationResult.error("must contain @");
                            return ValidationResult.ok();
                        });

        ValidationResult emailResult = emailValidator.validate(user.getEmail());
        if (!emailResult.isValid()) {
            for (String err : emailResult.getErrors()) {
                allErrors.add("email: " + err);
            }
        }

        if(allErrors.isEmpty()) return ValidationResult.ok();

        return new ValidationResult(allErrors);

    }
}
