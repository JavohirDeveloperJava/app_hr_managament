package uz.pdp.app_hr_managament.payload;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TaskDto {

    private String name;

    private String description;

    private Integer deadline;

    private UUID employeeId;
}