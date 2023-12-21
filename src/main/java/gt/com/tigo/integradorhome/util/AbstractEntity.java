package gt.com.tigo.integradorhome.util;

import java.sql.Timestamp;

public abstract class AbstractEntity {

    public abstract void setFechaCreacion(Timestamp fechaCreacion);

    public abstract void setUsuarioCreacion(String usuarioCreacion);

    public abstract void setFechaModificacion(Timestamp fechaModificacion);

    public abstract void setUsuarioModificacion(String usuarioModificacion);

}
