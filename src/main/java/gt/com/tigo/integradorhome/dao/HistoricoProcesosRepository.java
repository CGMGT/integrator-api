package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.HistoricoProcesosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoProcesosRepository extends JpaRepository<HistoricoProcesosEntity, Long>, JpaSpecificationExecutor<HistoricoProcesosEntity> {
}
