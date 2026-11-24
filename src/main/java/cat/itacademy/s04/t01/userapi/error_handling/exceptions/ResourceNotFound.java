package cat.itacademy.s04.t01.userapi.error_handling.exceptions;

public class ResourceNotFound  extends RuntimeException {
    public ResourceNotFound(String message) {
        super(message);
    }
}
