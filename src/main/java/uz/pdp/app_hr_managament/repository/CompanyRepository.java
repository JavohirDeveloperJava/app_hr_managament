package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.app_hr_managament.entity.Company;


import java.util.UUID;

@RepositoryRestResource(path = "company")
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Override
    boolean existsById(UUID uuid);

    @Query( value = "select count(*)>0 from employee emp " +
            "join company com on emp.company_id = com.id" +
            "join employee_role rol on rol.employee_id = emp.id" +
            "where rol.role_id=2 and com.id=:companyId", nativeQuery = true)
    Boolean hasDirector(UUID companyId);
}
