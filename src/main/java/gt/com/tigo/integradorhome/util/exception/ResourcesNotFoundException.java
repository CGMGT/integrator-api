package gt.com.tigo.integradorhome.util.exception;

public class ResourcesNotFoundException extends Exception {

    private static final String MESSAGE = "No se han podido encontrar los recursos solicitados.";

    public ResourcesNotFoundException() {
        super(MESSAGE);
    }

    public ResourcesNotFoundException(String message) {
        super(message);
    }

}
