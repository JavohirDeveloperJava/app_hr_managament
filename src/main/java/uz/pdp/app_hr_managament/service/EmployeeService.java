package uz.pdp.app_hr_managament.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.app_hr_managament.entity.Company;
import uz.pdp.app_hr_managament.entity.Employee;
import uz.pdp.app_hr_managament.entity.enums.RoleName;
import uz.pdp.app_hr_managament.payload.ApiResponse;
import uz.pdp.app_hr_managament.payload.EmployeeDto;
import uz.pdp.app_hr_managament.payload.LoginDto;
import uz.pdp.app_hr_managament.repository.CompanyRepository;
import uz.pdp.app_hr_managament.repository.EmployeeRepository;
import uz.pdp.app_hr_managament.repository.RoleRepository;
import uz.pdp.app_hr_managament.security.JwtProvider;


import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService implements UserDetailsService {


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Lazy
    @Autowired
    JavaMailSender javaMailSender;

    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CompanyRepository companyRepository;

    public ApiResponse addDirector(EmployeeDto dto){
        Boolean exists = employeeRepository.existsByEmailAndEnabledTrue(dto.getEmail());
        if (exists)
            return new ApiResponse("Employee has already exists", false);

        if (!companyRepository.existsById(dto.getCompanyId()))
            return new ApiResponse("Company not found",false);

//        Boolean hasDirector = companyRepository.hasDirector(dto.getCompanyId());
//        if(hasDirector)
//            return new ApiResponse("Director bor bu companiyada ", false);

        Company company = companyRepository.getOne(dto.getCompanyId());

        Employee employee = createEmployee(dto);
        employee.setRole(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
        employee.setCompany(company);
        employeeRepository.save(employee);

        sendEmail(employee.getEmail(), employee.getEmailCode());

        return new ApiResponse("Director saqlandi", true);
    }

    public ApiResponse addManager(EmployeeDto dto){
        Boolean exists = employeeRepository.existsByEmailAndEnabledTrue(dto.getEmail());
        if (exists)
            return new ApiResponse("Employee has already exists", false);
        Employee employee = createEmployee(dto);
        employee.setRole(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)));
        employeeRepository.save(employee);

        sendEmail(employee.getEmail(), employee.getEmailCode());

        return new ApiResponse("Manager saqlandi", true);
    }

    public ApiResponse addWorker(EmployeeDto dto){
        Boolean exists = employeeRepository.existsByEmailAndEnabledTrue(dto.getEmail());
        if (exists)
            return new ApiResponse("Employee has already exists", false);
        Employee employee = createEmployee(dto);
        employee.setRole(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_WORKER)));
        employeeRepository.save(employee);

        sendEmail(employee.getEmail(), employee.getEmailCode());

        return new ApiResponse("Worker saqlandi", true);
    }

    public Employee createEmployee(EmployeeDto dto){
        Employee employee = new Employee();
        employee.setEmail(dto.getEmail());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setSalary(dto.getSalary());
        employee.setEmailCode((int)(Math.random()*10000+1000));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null
                &&authentication.isAuthenticated()
                &&!authentication.getPrincipal().equals("anonymousUser")){
            Employee authEmployee = (Employee) authentication.getPrincipal();
            employee.setCompany(authEmployee.getCompany());
        }
        return employee;
    }

    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    public ApiResponse inActiveEmployee(UUID id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(!optionalEmployee.isPresent())
            return new ApiResponse("Bunday employee yoq", false);
        Employee employee = optionalEmployee.get();
        employee.setEnabled(false);
        employeeRepository.save(employee);
        return new ApiResponse("In Active employee", true);
    }

    public void sendEmail(String sendingEmail, Integer emailCode){
        String link = "http://localhost:8080/api/auth/verify?emailCode=" + emailCode + "&email=" + sendingEmail;
        String body = "<form action=" + link + " method=\"post\">\n" +
                "<label>Create password for your cabinet</label>" +
                "<br/><input type=\"text\" name=\"password\" placeholder=\"password\">\n" +
                "<br/>  <button>Submit</button>\n" +
                "</form>";

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Teat@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
        }catch (Exception ignore){

        }
    }
    public ApiResponse verifyEmail(String sendingEmail, Integer emailCode, HttpServletRequest request){
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailAndEmailCode(sendingEmail, emailCode);
        if (optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            employee.setEnabled(true);
            employee.setPassword(passwordEncoder.encode(request.getParameter("password")));
            employee.setEmailCode(null);
            employeeRepository.save(employee);
            return new ApiResponse("Employee saqlandi", true);
        }
        return new ApiResponse("Account tasdiqlanmadi", false);
    }

    public ApiResponse login(LoginDto loginDto){
        try{
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()
                    ));
            Employee employee = (Employee) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getEmail(), employee.getRole());

            return new ApiResponse("Token", true, token);

        }catch (BadCredentialsException badCredentialsException){
            return new ApiResponse("Parol yoki Login xato!", false);
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return employeeRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email+" topilmadi"));
    }
}