package gt.com.tigo.integradorhome.util;

import java.util.List;

public interface IListToArrayConverter<T> {

    List<Object[]> toArray(List<T> list);

}
