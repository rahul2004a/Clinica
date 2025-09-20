package com.pm.appointmentservice.service;

import com.pm.appointmentservice.dto.AppointmentResponseDto;
import com.pm.appointmentservice.entity.CachedPatient;
import com.pm.appointmentservice.repository.AppointmentRepository;
import com.pm.appointmentservice.repository.CachedPatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CachedPatientRepository cachedPatientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, CachedPatientRepository cachedPatientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.cachedPatientRepository = cachedPatientRepository;
    }

    public List<AppointmentResponseDto> getAppointmentsByDateRange(LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.findByStartTimeBetween(from, to)
                .stream()
                .map(appointment ->{
                    String name = cachedPatientRepository.findById(appointment.getPatientId())
                            .map(CachedPatient::getFullName)
                            .orElse("Unknown Patient");

                    AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();

                    appointmentResponseDto.setId(appointment.getId());
                    appointmentResponseDto.setPatientId(appointment.getPatientId());
                    appointmentResponseDto.setStartTime(appointment.getStartTime());
                    appointmentResponseDto.setEndTime(appointment.getEndTime());
                    appointmentResponseDto.setReason(appointment.getReason());
                    appointmentResponseDto.setPatientName(name);

                    return appointmentResponseDto;
                }).toList();
    }

}
