package com.stc.clinicservice.repositories;

import com.stc.clinicservice.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> getAllByAppointmentDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Appointment> getAllByPatientName(String patientName);

    List<Appointment> getAllByPatientNameAndAndAppointmentDateBetween(String patientName,LocalDateTime  startOfDay, LocalDateTime endOfDay);
}
