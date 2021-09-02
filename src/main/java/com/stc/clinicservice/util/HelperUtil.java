package com.stc.clinicservice.util;

import com.stc.clinicservice.dtos.AppointmentDTO;
import com.stc.clinicservice.entities.Appointment;

public class HelperUtil {
    public static AppointmentDTO convertEntityToDTO(Appointment appointment){
        return AppointmentDTO.builder()
                .appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .patientName(appointment.getPatientName())
                .patientEmail(appointment.getPatientEmail())
                .patientNumber(appointment.getPatientNumber())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .build();
    }
}
