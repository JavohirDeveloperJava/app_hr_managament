package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.app_hr_managament.entity.Role;
import uz.pdp.app_hr_managament.entity.enums.RoleName;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
