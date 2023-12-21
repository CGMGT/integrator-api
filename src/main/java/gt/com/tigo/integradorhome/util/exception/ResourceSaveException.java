package gt.com.tigo.integradorhome.util.exception;

public class ResourceSaveException extends TigoException {

    private static final String MESSAGE = "No se ha podido guardar el recurso.";

    public ResourceSaveException() {
        super(MESSAGE);
    }

    public ResourceSaveException(String message) {
        super(message);
    }

}
