package uz.pdp.app_hr_managament.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String message;

    private Boolean status;

    private Object object;

    public ApiResponse(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }
}
