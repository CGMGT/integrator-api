package gt.com.tigo.integradorhome.util.exception;

public class ResourceDeleteException extends TigoException {

    private static final String MESSAGE = "No se ha podido eliminar el recurso.";

    public ResourceDeleteException() {
        super(MESSAGE);
    }

    public ResourceDeleteException(String message) {
        super(message);
    }

}
