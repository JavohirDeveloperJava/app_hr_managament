package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.app_hr_managament.entity.TourniquetHistory;


import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface TourniquetCardHistoryRepository extends JpaRepository<TourniquetHistory, UUID> {
    @Query(nativeQuery = true, value = "SELECT entered_at, exited_at from tourniquet_history tour " +
            "join touniquet_card card on tour.tourniquet_card_id = card.id" +
            "join employee emp on card.employee =:id" +
            "where entered_at between :startDate and :endDate " +
            "or exited_at between :startDate and :endDate")
    List<TourniquetHistory> getTourniquetHistoryByEmployee(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate, @Param("id") UUID id);
}