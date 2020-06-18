package simple_rest_api.validators;

import java.util.Map;

public interface Validator<T> {
    Map<String, String> validate(T item);
    boolean hasErrors();
}
