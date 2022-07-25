package uz.pdp.app_hr_managament.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.app_hr_managament.entity.Employee;


import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditAwarelpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null
                &&authentication.isAuthenticated()
                &&!authentication.getPrincipal().equals("anonymousUser")
        ){
            Employee employee = (Employee) authentication.getPrincipal();
            return Optional.of(employee.getId());
        }

        return Optional.empty();
    }
}