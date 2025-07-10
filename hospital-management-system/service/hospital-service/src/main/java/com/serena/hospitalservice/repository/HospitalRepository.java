package com.serena.hospitalservice.repository;

import com.serena.model.model.hospital.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    Hospital getHospitalByHospitalCode(String hospitalCode);
}
