package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.MensajesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MensajesEntity, Long>, JpaSpecificationExecutor<MensajesEntity> {
    @Query(value = "SELECT * FROM mensajes " +
            "WHERE id_usuario = :idUser " +
            "AND (attribute2 = :type OR attribute2 IS NULL) " +
            "ORDER BY fecha_creacion " +
            "DESC", nativeQuery = true)
    List<MensajesEntity> getUserMessages(Long idUser, String type);

}
