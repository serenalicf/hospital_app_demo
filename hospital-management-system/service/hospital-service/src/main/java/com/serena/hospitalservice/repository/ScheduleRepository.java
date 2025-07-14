package com.serena.hospitalservice.repository;

import com.serena.model.model.hospital.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, Integer> {
    Schedule getScheduleByHospitalCodeAndHospitalScheduleId(String hospitalCode, String hospitalScheduleId);

    Schedule findByHospitalCodeAndHospitalScheduleId(String hospitalCode, String hospitalScheduleId);
}
