package com.co2fp.backend.backend.DTO.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDTO {
    private Long answer_id;
    private LocalDate date;
    private LocalTime time;
    private Long  distance;
    private Long  passenger_count;
    private String transport_name;
    private Long transport_id;
    private String username;
    private Long user_id;
}
