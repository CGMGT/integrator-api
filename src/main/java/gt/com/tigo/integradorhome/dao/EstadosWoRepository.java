package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.EstadosWoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosWoRepository extends JpaRepository<EstadosWoEntity, Long>, JpaSpecificationExecutor<EstadosWoEntity> {

    @Query(
            value = "SELECT pkg_integrador.F_PROCESAR_WO(?1) FROM DUAL",
            nativeQuery = true
    )
    int procesar(String usuario);

}
