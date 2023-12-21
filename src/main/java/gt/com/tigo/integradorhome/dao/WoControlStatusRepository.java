package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.WoControlStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WoControlStatusRepository extends JpaRepository<WoControlStatusEntity, Long>, JpaSpecificationExecutor<WoControlStatusEntity>  {
}
