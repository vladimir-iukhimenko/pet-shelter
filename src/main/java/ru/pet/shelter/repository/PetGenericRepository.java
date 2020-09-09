package ru.pet.shelter.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.pet.shelter.model.Pet;

@NoRepositoryBean
public interface PetGenericRepository<T extends Pet> extends ReactiveMongoRepository<T, String> {
}
