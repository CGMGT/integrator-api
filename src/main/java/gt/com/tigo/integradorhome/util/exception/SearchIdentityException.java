package gt.com.tigo.integradorhome.util.exception;

public class SearchIdentityException extends Exception {

    private static final String MESSAGE = "Error al recuperar la información del usuario.";

    public SearchIdentityException() {
        super(MESSAGE);
    }

    public SearchIdentityException(String message) {
        super(message);
    }

}
