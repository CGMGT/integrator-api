package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.WoMapeosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WoMapeosRepository extends JpaRepository<WoMapeosEntity, Long>, JpaSpecificationExecutor<WoMapeosEntity> {
}
