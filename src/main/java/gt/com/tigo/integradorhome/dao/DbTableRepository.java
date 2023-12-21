package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbTableRepository extends JpaRepository<DummyEntity, Long> {

    @Query(
            value = "SELECT UPPER(REPLACE(column_name, '_', ' ')) AS col " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = 'public' " +
                    "AND table_name = :tableName",
            nativeQuery = true
    )
    List<String> getColumnLabelsByTableName(String tableName);

}
