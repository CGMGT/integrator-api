package gt.com.tigo.integradorhome.util.exception;

public class InvalidFilterException extends TigoException {

    private static final String MESSAGE = "Error al construir filtro de búsqueda.";

    public InvalidFilterException() {
        super(MESSAGE);
    }

    public InvalidFilterException(String message) {
        super(message);
    }

}
