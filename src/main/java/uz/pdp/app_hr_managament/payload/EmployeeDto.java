package uz.pdp.app_hr_managament.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class EmployeeDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Double salary;

    @Email
    @NotNull
    private String email;

    @NotNull
    private UUID companyId;


}