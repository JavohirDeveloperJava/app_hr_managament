package uz.pdp.app_hr_managament.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app_hr_managament.entity.Employee;
import uz.pdp.app_hr_managament.payload.ApiResponse;
import uz.pdp.app_hr_managament.payload.EmployeeDto;
import uz.pdp.app_hr_managament.payload.LoginDto;
import uz.pdp.app_hr_managament.service.EmployeeService;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/auth")
public class EmployeeController {
    @Autowired
    EmployeeService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addDirector")
    public HttpEntity<?> addDirector(@Valid @RequestBody EmployeeDto employeeDto){
        ApiResponse apiResponse = authService.addDirector(employeeDto);
        return ResponseEntity.status(apiResponse.getStatus()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping("/addManager")
    public HttpEntity<?> addManager(@Valid @RequestBody EmployeeDto employeeDto){
        ApiResponse apiResponse = authService.addManager(employeeDto);
        return ResponseEntity.status(apiResponse.getStatus()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER')")
    @PostMapping("/addWorker")
    public HttpEntity<?> addWorker(@Valid @RequestBody EmployeeDto employeeDto){
        ApiResponse apiResponse = authService.addWorker(employeeDto);
        return ResponseEntity.status(apiResponse.getStatus()?201:409).body(apiResponse);
    }

    @GetMapping("/inActive{id}")
    public HttpEntity<?> inActive(@PathVariable UUID id){
        ApiResponse apiResponse = authService.inActiveEmployee(id);
        return ResponseEntity.status(apiResponse.getStatus()?203:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER')")
    @GetMapping("/list")
    public HttpEntity<List<Employee>> getEmployee(){
        List<Employee> allEmployee = authService.getAllEmployee();

        return ResponseEntity.ok(allEmployee);
    }

    @PostMapping("/verify")
    public HttpEntity<?> verifyEmail(@RequestParam Integer emailCode, @RequestParam String sendingEmail, HttpServletRequest request){
        ApiResponse apiResponse = authService.verifyEmail(sendingEmail, emailCode, request);
        return ResponseEntity.status(apiResponse.getStatus()?201:409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse login = authService.login(loginDto);
        return ResponseEntity.status(login.getStatus()?202:409).body(login);
    }
}
