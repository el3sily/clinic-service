package com.stc.clinicservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "patient_email")
    private String patientEmail;

    @Column(name = "patient_number")
    private String patientNumber;

    @Column(name = "status")
    @Builder.Default
    private String status = "ongoing";

    @Column(name = "reason")
    private String reason;

}
