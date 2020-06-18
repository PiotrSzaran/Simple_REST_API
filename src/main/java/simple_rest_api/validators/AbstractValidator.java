package simple_rest_api.validators;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public abstract class AbstractValidator<T> implements Validator<T> {

    protected Map<String, String> errors = new HashMap<>();

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}