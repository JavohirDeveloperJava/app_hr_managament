package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.app_hr_managament.entity.Employee;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Boolean existsByEmailAndEnabledTrue(String email);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByIdAndEnabledTrue(UUID id);

    Optional<Employee> findByEmailAndEmailCode(String email, Integer emailCode);

    List<Employee> findAllByCompanyIdAndEnabledTrue(UUID company_id);
}