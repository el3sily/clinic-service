package com.stc.clinicservice.services;

import com.stc.clinicservice.configurations.aop.Loggable;
import com.stc.clinicservice.dtos.AppointmentDTO;
import com.stc.clinicservice.entities.Appointment;
import com.stc.clinicservice.exceptions.CustomizedException;
import com.stc.clinicservice.repositories.AppointmentRepository;
import com.stc.clinicservice.util.HelperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ClinicService implements Loggable {
    private static final Logger LOGGER = LoggerFactory.getLogger("default-logger");

    @Autowired
    AppointmentRepository appointmentRepository;

    public List<AppointmentDTO> getTodayAppointments(){
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDate = startOfDay.toLocalDate().atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.getAllByAppointmentDateBetween(startOfDay, endOfDate);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
                appointments.forEach(
                appointment -> appointmentDTOS.add(HelperUtil.convertEntityToDTO(appointment))
        );
        return appointmentDTOS;
    }


    public String addAppointment(AppointmentDTO appointmentDTO){
        Appointment appointment = Appointment.builder()
                .appointmentDate(appointmentDTO.getAppointmentDate())
                .patientName(appointmentDTO.getPatientName())
                .patientEmail(appointmentDTO.getPatientEmail())
                .patientNumber(appointmentDTO.getPatientNumber())
                .build();
        appointmentRepository.save(appointment);

        return "Appointment Added Successfully";
    }

    public String cancelAppointment(Long appointmentId, String reason) throws Exception {
        Appointment appointment = appointmentRepository.getById(appointmentId);
        if(Objects.nonNull(appointment)){
            appointment.setStatus("cancelled");
            appointment.setReason(reason);
            appointmentRepository.save(appointment);
            LOGGER.info("Appointment cancelled because {}", reason);
            return "Appointment Cancelled";
        }
        else
            throw new CustomizedException("appointment not found");
    }

    public List<AppointmentDTO> getTodayAppointmentsByPatientName(String patientName, String date){
        List<Appointment> appointments = new ArrayList<>();

        if(Objects.nonNull(patientName) && Objects.nonNull(date)) {
            LocalDate localDate = LocalDate.parse(date);
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDate = startOfDay.toLocalDate().atTime(LocalTime.MAX);
            appointments = appointmentRepository.getAllByPatientNameAndAndAppointmentDateBetween(patientName, startOfDay, endOfDate);
        }
        else if(Objects.nonNull(date)) {
            LocalDate localDate = LocalDate.parse(date);
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDate = startOfDay.toLocalDate().atTime(LocalTime.MAX);
            appointments = appointmentRepository.getAllByAppointmentDateBetween(startOfDay, endOfDate);
        }

        else if(Objects.nonNull(patientName))
            appointments = appointmentRepository.getAllByPatientName(patientName);

        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        appointments.forEach(
                appointment -> appointmentDTOS.add(HelperUtil.convertEntityToDTO(appointment))
        );
        return appointmentDTOS;
    }



}
