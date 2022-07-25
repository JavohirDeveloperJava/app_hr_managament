package uz.pdp.app_hr_managament.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app_hr_managament.entity.Employee;
import uz.pdp.app_hr_managament.entity.EmployeeSalary;
import uz.pdp.app_hr_managament.entity.Task;
import uz.pdp.app_hr_managament.entity.TourniquetHistory;
import uz.pdp.app_hr_managament.payload.DashboardDto;
import uz.pdp.app_hr_managament.service.EmployeeSalaryService;
import uz.pdp.app_hr_managament.service.EmployeeService;
import uz.pdp.app_hr_managament.service.TaskService;
import uz.pdp.app_hr_managament.service.TourniquetHistoryService;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {
    @Autowired
    EmployeeSalaryService employeeSalaryService;

    @Autowired
    TaskService taskService;

    @Autowired
    TourniquetHistoryService tourniquetHistoryService;

    @Autowired
    EmployeeService employeeService;

    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER')")
    @GetMapping("/listEmployee")
    public HttpEntity<?> getListEmployee(){
        List<Employee> allEmployee = employeeService.getAllEmployee();
        return ResponseEntity.ok(allEmployee);
    }
    @PreAuthorize("hasAnyRole('DIRECTOR')")
    @GetMapping(value = "/infoTaskTourniquet/{id}")
    public HttpEntity<?> getInfoTaskTouniquet(@PathVariable UUID id, @RequestParam LocalDate start, @RequestParam LocalDate end){
        DashboardDto dto = new DashboardDto();
        List<Task> taskInfo = taskService.info(start, end, id);
        List<TourniquetHistory> historiesInfo = tourniquetHistoryService.info(start, end, id);
        dto.setHistories(historiesInfo);
        dto.setTaskList(taskInfo);
        return ResponseEntity.ok(dto);
    }
    @PreAuthorize("hasAnyRole('DIRECTOR')")
    @GetMapping("/salaryInfo")
    public HttpEntity<?> getInfoSalary(@PathVariable UUID id, @RequestParam LocalDate start, @RequestParam LocalDate end){
        List<EmployeeSalary> employeeSalaries = employeeSalaryService.infoSalary(id, start, end);
        return ResponseEntity.ok(employeeSalaries);
    }
    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER')")
    @GetMapping("/taskInfo")
    public HttpEntity<?> getListTask(){
        List<Task> allTask = taskService.getAllTask();
        return ResponseEntity.ok(allTask);
    }
}