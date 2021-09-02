package com.stc.clinicservice.api;

import com.stc.clinicservice.dtos.AppointmentDTO;
import com.stc.clinicservice.services.ClinicService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("clinic")
@Validated
public class ClinicAPI {
    @Autowired
    ClinicService clinicService;

    @GetMapping("/getTodayAppointments")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Add new appointment")
    @ApiResponses({
            @ApiResponse(code = 200, message = "", response = AppointmentDTO.class),
            @ApiResponse(code = 400, message = "Missing body field\nInvalid body field"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public ResponseEntity<List<AppointmentDTO>> getTodayAppointments() throws ParseException {
        return new ResponseEntity(clinicService.getTodayAppointments(), HttpStatus.OK);
    }

    @PostMapping("/addAppointment")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Add new appointment")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Appointment Added Successfully", response = String.class),
            @ApiResponse(code = 400, message = "Missing body field\nInvalid body field"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public ResponseEntity<List<JSONObject>> addAppointment(@RequestBody AppointmentDTO appointmentDTO){
        return new ResponseEntity(clinicService.addAppointment(appointmentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/cancelAppointment")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Cancel an appointment")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Appointment Cancelled", response = String.class),
            @ApiResponse(code = 400, message = "Missing body field\nInvalid body field"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public ResponseEntity<List<String>> cancelAppointment(@RequestParam @ApiParam(name = "appointmentId", value = "1") Long appointmentId,
                                                          @RequestParam @ApiParam(name = "reason", value = "doctor isn't available") String reason) throws Exception {
        return new ResponseEntity(clinicService.cancelAppointment(appointmentId, reason), HttpStatus.OK);
    }

    @GetMapping("/getAppointments")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get All Appointments by Patient name")
    @ApiResponses({
            @ApiResponse(code = 200, message = "", response = AppointmentDTO.class),
            @ApiResponse(code = 400, message = "Missing body field\nInvalid body field"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@RequestParam(required = false) @ApiParam(name = "patientName", value = "patient name") String patientName,
            @RequestParam(required = false) @ApiParam(name = "date", value = "searching date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") String date){
        return new ResponseEntity(clinicService.getTodayAppointmentsByPatientName(patientName, date), HttpStatus.OK);
    }




}
