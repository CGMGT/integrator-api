package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.CatalogosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogosRepository extends JpaRepository<CatalogosEntity, Long>, JpaSpecificationExecutor<CatalogosEntity> {

    @Query(value = "SELECT DISTINCT grupo FROM catalogos ORDER BY grupo", nativeQuery = true)
    List<Object> getGroups();

    List<CatalogosEntity> findByGrupoOrderByNombre(String grupo);
}
