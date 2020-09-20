package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Pet;

@Repository
public interface PetRepository extends PetGenericRepository<Pet>, ChipRepository, DescriptionRepository,
        PassportRepository, PassportVaccinationRepository {
}
