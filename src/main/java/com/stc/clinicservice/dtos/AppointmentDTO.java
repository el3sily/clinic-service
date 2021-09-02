package com.stc.clinicservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Appointment")
@Data
@Builder
public class AppointmentDTO {
    @ApiModelProperty(name = "appointmentId", value = "appointment id", position = 1, required = true, example = "")
    private Long appointmentId;

    @ApiModelProperty(name = "appointmentDate", value = "appointment date", position = 2, required = true, example = "2021-09-01T10:00:00")
    @NotNull(message = "appointment date can't be null")
    private LocalDateTime appointmentDate;

    @ApiModelProperty(name = "patientName", value = "patient name", position = 3, required = true, example = "Mohamed")
    @NotNull(message = "name can't be null")
    private String patientName;

    @ApiModelProperty(name = "patientEmail", value = "patient email", position = 4, example = "mohamed@email.com")
    private String patientEmail;

    @ApiModelProperty(name = "number", value = "patient number", position = 5, example = "+212345678")
    private String patientNumber;

    @ApiModelProperty(name = "status", value = "appointment status", position = 6, example = "ongoing")
    private String status;

    @ApiModelProperty(name = "reason", value = "reason in case canceled appointment", position = 7, example = "doctor isn't available")
    private String reason;


}
