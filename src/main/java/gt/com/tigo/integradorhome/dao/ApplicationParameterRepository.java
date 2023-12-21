package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.AdmParametroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationParameterRepository extends JpaRepository<AdmParametroEntity, Long>, JpaSpecificationExecutor<AdmParametroEntity> {

    AdmParametroEntity findByCodigo(String codigo);

    AdmParametroEntity findByNombre(String nombre);

}
