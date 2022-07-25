package uz.pdp.app_hr_managament.payload;

import lombok.Data;
import uz.pdp.app_hr_managament.entity.Task;
import uz.pdp.app_hr_managament.entity.TourniquetHistory;


import java.util.List;

@Data
public class DashboardDto {
    private List<Task> taskList;

    private List<TourniquetHistory> histories;
}