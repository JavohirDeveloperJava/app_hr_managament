package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.app_hr_managament.entity.EmployeeSalary;


import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, UUID> {

    @Query(nativeQuery = true,value = "Select * from employee_salary sal" +
            "join employee emp on sal.employee_id =:id" +
            "where sal.date_time between :start and :ended" +
            "or" +
            "Select * from employee_salary sal" +
            "where sal.date_time  between :start and :ended")
    List<EmployeeSalary> getEmployeeInfo(@Param("start")LocalDateTime start, @Param("end") LocalDateTime ended, @Param("id") UUID id);
}
