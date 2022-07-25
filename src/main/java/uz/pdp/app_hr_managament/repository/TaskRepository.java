package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.app_hr_managament.entity.Task;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {


    List<Task> findAllByEmployeeId(UUID employee_id);

    @Query(nativeQuery = true, value = "Select * from task " +
            "join employee emp on task.employee_id =:id" +
            "where status = 3 and " +
            "completed_at between :start and :end")
    List<Task> getAllByEmployeeId(@Param("start")Timestamp start, @Param("end") Timestamp end, @Param("id") UUID id);
}
