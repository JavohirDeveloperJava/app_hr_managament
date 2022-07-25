package uz.pdp.app_hr_managament.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/*
https://docs.google.com/document/d/1p39pWX1zDLWoRQcmY5InjwYM3p0s687OdPPmmyEW0c8/edit?usp=sharing
 */

@Data
public class LoginDto {

    @NotNull
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    private String password;
}