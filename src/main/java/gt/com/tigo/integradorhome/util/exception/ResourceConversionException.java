package gt.com.tigo.integradorhome.util.exception;

public class ResourceConversionException extends TigoException {

    private static final String MESSAGE = "Error al convertir el recurso.";

    public ResourceConversionException() {
        super(MESSAGE);
    }

    public ResourceConversionException(String message) {
        super(message);
    }

}
