package uz.pdp.app_hr_managament.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TourniquetHistory {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private TourniquetCard tourniquetCard;

    private Timestamp enteredAt;

    private Timestamp exitedAt;
}