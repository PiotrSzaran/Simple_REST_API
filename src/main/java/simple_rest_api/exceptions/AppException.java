package simple_rest_api.exceptions;

public class AppException extends RuntimeException{
    public AppException(String message) {
        super(message);
    }
}
