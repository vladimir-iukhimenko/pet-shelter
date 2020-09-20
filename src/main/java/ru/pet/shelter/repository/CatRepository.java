package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Cat;

@Repository
public interface CatRepository extends PetGenericRepository<Cat>, ChipRepository {

}
