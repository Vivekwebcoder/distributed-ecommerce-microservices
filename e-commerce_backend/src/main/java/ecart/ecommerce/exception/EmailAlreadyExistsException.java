package ecart.ecommerce.exception;
import ecart.ecommerce.exception.EmailAlreadyExistsException;
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

}