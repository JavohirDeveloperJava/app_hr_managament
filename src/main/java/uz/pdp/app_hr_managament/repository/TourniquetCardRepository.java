package uz.pdp.app_hr_managament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.app_hr_managament.entity.TourniquetCard;


import java.util.Optional;
import java.util.UUID;

public interface TourniquetCardRepository extends JpaRepository<TourniquetCard, UUID> {

    Optional<TourniquetCard> findByIdAndStatusTrue(UUID id);
}
