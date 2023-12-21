package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.AdmParametroInternalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationParameterInternalRepository extends JpaRepository<AdmParametroInternalEntity, Long>, JpaSpecificationExecutor<AdmParametroInternalEntity> {
}
