public class InvalidFieldException extends RuntimeException {

    public InvalidFieldException() {}

    public InvalidFieldException(String errorMessage) {
        super(errorMessage);
    }
}

