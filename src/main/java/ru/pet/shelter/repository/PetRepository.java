package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Pet;

@Repository
public interface PetRepository extends ReactiveMongoRepository<Pet, String>, PetReadRepository, CatRepository, DogRepository,
        ChipRepository, DescriptionRepository, PhotoRepository, PassportRepository, PassportVaccinationRepository {

}
