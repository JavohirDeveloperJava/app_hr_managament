package uz.pdp.app_hr_managament.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.pdp.app_hr_managament.entity.Employee;
import uz.pdp.app_hr_managament.entity.Task;
import uz.pdp.app_hr_managament.payload.ApiResponse;
import uz.pdp.app_hr_managament.payload.TaskDto;
import uz.pdp.app_hr_managament.repository.EmployeeRepository;
import uz.pdp.app_hr_managament.repository.TaskRepository;


import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JavaMailSender javaMailSender;

    static Integer NEW = 1, WORKING = 2, COMLETED = 3, UNCOMPLETED = 4;

    public List<Task> getAllTask(){
        List<Task> taskList = taskRepository.findAll();
        return taskList;
    }

    public ApiResponse taskDirector(TaskDto taskDto){
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(new Date(System.currentTimeMillis()+((1000*60*60*24)*taskDto.getDeadline())));
        task.setStatus(NEW);
        Optional<Employee> optionalEmployee = employeeRepository.findById(taskDto.getEmployeeId());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Bunday Emloyee mavjud emas", false);
        Employee employee = optionalEmployee.get();
        task.setEmployee(employee);
        taskRepository.save(task);
        sendEmail(employee.getEmail(), task.getId());
        return new ApiResponse("Save and send message to Manager", true);
    }

    public ApiResponse taskManager(TaskDto taskDto){
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(new Date(System.currentTimeMillis()+((1000*60*60*24)*taskDto.getDeadline())));
        task.setStatus(NEW);
        Optional<Employee> optionalEmployee = employeeRepository.findById(taskDto.getEmployeeId());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Bunday Emloyee mavjud emas", false);
        Employee employee = optionalEmployee.get();
        task.setEmployee(employee);
        taskRepository.save(task);
        sendEmail(employee.getEmail(), task.getId());
        return new ApiResponse("Save and send message to Working", true);

    }
    // Qabul qilish vazifani
    public ApiResponse acceptTask(UUID id){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task not found", false);
        Task task = optionalTask.get();
        task.setStatus(WORKING);
        taskRepository.save(task);
        return new ApiResponse("Task accepted success", true);
    }

    //Completed task
    public ApiResponse completed(UUID id){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task not found", false);
        Task task = optionalTask.get();
        if (new Date(System.currentTimeMillis()).before(task.getDeadline())){
            UUID created = task.getCreatedBy();
            Optional<Employee> employeeLeader = employeeRepository.findByIdAndEnabledTrue(created);
            if (!employeeLeader.isPresent())
                return new ApiResponse("Vazifa yaratgan employee mavjud emas", false);
            Employee employee = employeeLeader.get();
            String emailTo = employee.getEmail();

            Employee taskEmployee = task.getEmployee();
            String emailFrom = taskEmployee.getEmail();

            task.setCompletedAt(new Date());

            task.setStatus(COMLETED);

            taskRepository.save(task);

            sendForCompleted(task, emailTo, emailFrom);
            return new ApiResponse("Successfly completed task", true);
        }

        task.setStatus(UNCOMPLETED);
        task.setCompletedAt(new Date());
        taskRepository.save(task);
        return new ApiResponse("Uncompleted task", false);
    }
    // Task for Employee
    public List<Task> getTaskByEmployeeId(UUID id){

        List<Task> taskList = taskRepository.findAllByEmployeeId(id);

        return taskList;
    }
    //Sending task email warning

    public List<Task> info(LocalDate start, LocalDate end, UUID id){
        LocalDateTime dateTimeStart = start.atTime(00,00,00);
        LocalDateTime dateTimeEnd = end.atTime(23,59,59);
        Timestamp startTime = Timestamp.valueOf(dateTimeStart);
        Timestamp endTime = Timestamp.valueOf(dateTimeEnd);
        List<Task> infoTask = taskRepository.getAllByEmployeeId(startTime, endTime, id);
        return infoTask;
    }
    public void sendEmail(String sendingEmail, UUID id){

        String link = "http://localhost:8080/api/task/accept/"+id;
        String body = "<form action='" + link + "' method=\"get\">\n" +
                "<label>Create new task </label>" +
                "<button>Accept</button>\n" +
                "</form>";

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Test@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
        }catch (Exception ignore){

        }
    }

    //Sending email comleted
    public void sendForCompleted(Task task, String emailTo, String emailFrom){
        String body = "<p>Task: " + task.getName() + "</p>" +
                "<p>Description: " + task.getDescription() + "</p>" +
                "<p>Completed: " + task.getCompletedAt() + "</p>";
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject("Task completed");
            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (Exception ignored) {
        }

    }
}