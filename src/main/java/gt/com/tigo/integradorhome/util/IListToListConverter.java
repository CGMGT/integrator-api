package gt.com.tigo.integradorhome.util;

import java.util.List;

public interface IListToListConverter<T> {

    List<List<String>> toList(List<T> list);

}
