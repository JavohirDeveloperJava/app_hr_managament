package uz.pdp.app_hr_managament.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.app_hr_managament.entity.Employee;
import uz.pdp.app_hr_managament.entity.EmployeeSalary;
import uz.pdp.app_hr_managament.payload.ApiResponse;
import uz.pdp.app_hr_managament.repository.EmployeeRepository;
import uz.pdp.app_hr_managament.repository.EmployeeSalaryRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeSalaryService {
    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public ApiResponse payment(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null
                &&authentication.isAuthenticated()
                &&authentication.getPrincipal().equals("anonymousUser"));
        Employee employee = (Employee) authentication.getPrincipal();

        List<Employee> employeeList = employeeRepository.findAllByCompanyIdAndEnabledTrue(employee.getCompany().getId());
        for (Employee employees : employeeList) {
            EmployeeSalary salary = new EmployeeSalary();
            salary.setSalary(employees.getSalary());
            salary.setEmployee(employees);
            salary.setDateTime(LocalDateTime.now());
            employeeSalaryRepository.save(salary);
        }
        return new ApiResponse("Payment success", true);
    }

    public List<EmployeeSalary> infoSalary(UUID id, LocalDate start, LocalDate end){
        LocalDateTime startTime = start.atTime(00,00,00);
        LocalDateTime endTime = end.atTime(23,59,59);
        List<EmployeeSalary> employeeInfo = employeeSalaryRepository.getEmployeeInfo(startTime, endTime, id);
        return employeeInfo;
    }
}
