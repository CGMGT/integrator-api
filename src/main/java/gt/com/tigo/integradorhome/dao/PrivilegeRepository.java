package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.AdmPermisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<AdmPermisoEntity, Long>, JpaSpecificationExecutor<AdmPermisoEntity> {
}
