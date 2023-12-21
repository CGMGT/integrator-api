package gt.com.tigo.integradorhome.dao;

import gt.com.tigo.integradorhome.model.CustomReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomReportRepository extends JpaRepository<CustomReportEntity, Long>, JpaSpecificationExecutor<CustomReportEntity> {
    @Query(
            value = "SELECT * \n" +
                    " FROM V_DB_WO_RESUMEN" +
                    " WHERE PERIODO = ?" ,
            nativeQuery = true
    )
    List<Object> getResumenWO(String periodo);

    @Query(
            value = "SELECT * \n" +
                    " FROM V_WO_RES_BY_TASK_TYPE" ,
            nativeQuery = true
    )
    List<Object> getResByTaskType();

    @Query(
            value = "SELECT * \n" +
                    " FROM V_WO_RES_BY_DISTRICT" ,
            nativeQuery = true
    )
    List<Object> getResByDistrict();

    @Query(
            value = "SELECT * \n" +
                    " FROM V_WO_LAST_10_ERRORS" +
                    " WHERE PERIODO = ?",
            nativeQuery = true
    )
    List<Object> getLast10Errors(String periodo);

    @Query(
            value = "SELECT * \n" +
                    " FROM V_DB_PERIODOS" ,
            nativeQuery = true
    )
    List<Object> getPeriods();
}
