package lab2;

@FunctionalInterface
interface Validator<T> {
    ValidationResult validate(T value);

    default Validator<T> and(Validator<? super T> other) throws IllegalArgumentException{
        if(other == null){
            throw new IllegalArgumentException();
        }
        return (T value) -> {
            ValidationResult firstValidation = this.validate(value);
            if (!firstValidation.isValid()) return firstValidation;
            return other.validate(value);
        };
    }

    static <T> Validator<T> notNull(){
        return  (T value) -> {
            if (value == null) return ValidationResult.error("must not be null");
            return ValidationResult.ok();
        };
    }

    static Validator<String> minLength(int min) throws IllegalArgumentException{
        if (min < 0) throw new IllegalArgumentException();

        return (String value) -> {
            if(value == null) return ValidationResult.error("must not be null");
            if(value.length() < min) return ValidationResult.error("must have at least " + min + " characters");
            return ValidationResult.ok();
        };
    }

    static Validator<Integer> range(int min, int max) throws IllegalArgumentException{
        if (min > max) throw new IllegalArgumentException();

        return (Integer value) -> {
            if(value == null) return ValidationResult.error("must not be null");
            if(value < min || value > max) return ValidationResult.error("must be between " + min + " and " + max);
            return ValidationResult.ok();
        };
    }
}
