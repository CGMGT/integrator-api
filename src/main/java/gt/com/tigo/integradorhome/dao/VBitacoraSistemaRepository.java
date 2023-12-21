package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.VBitacoraSistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VBitacoraSistemaRepository extends JpaRepository<VBitacoraSistemaEntity, Long>, JpaSpecificationExecutor<VBitacoraSistemaEntity> {

    @Query( value = "SELECT DISTINCT source_type FROM v_bitacora_sistema ORDER BY source_type ASC LIMIT 10", nativeQuery = true)
    List<Object> getSourceType(String sourceType);

}
