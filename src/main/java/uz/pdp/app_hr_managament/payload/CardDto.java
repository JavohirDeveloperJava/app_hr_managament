package uz.pdp.app_hr_managament.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CardDto {
    @NotNull
    private UUID compId;

    @NotNull
    private UUID employeeId;
}
