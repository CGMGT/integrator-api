package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.AdmGrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<AdmGrupoEntity, Long>, JpaSpecificationExecutor<AdmGrupoEntity> {

    AdmGrupoEntity findByNombre(String nombre);

    AdmGrupoEntity findByNombreAndActivo(String nombre, String activo);

    @Query(value = "SELECT DISTINCT ag.*\n" +
            "FROM adm_usuario_rel_grupo au\n" +
            "    inner join adm_usuario a\n" +
            "        on a.id = au.usuario\n" +
            "    inner join adm_grupo ag on ag.id = au.grupo\n" +
            "where au.usuario = :idUser and ag.nombre in ('CAPEX', 'OPEX')", nativeQuery = true)
    List<AdmGrupoEntity> findAllByIdUser(Long idUser);

}
