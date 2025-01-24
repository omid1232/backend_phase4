package com.example.demo.repository;

import com.example.demo.model.Designer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DesignerRepository extends MongoRepository<Designer, String> {
   // Designer findByName(String name);
    Optional<Designer> findByUsername(String username);

}
