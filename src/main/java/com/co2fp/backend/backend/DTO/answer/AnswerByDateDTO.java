package com.co2fp.backend.backend.DTO.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerByDateDTO {
    private Float DailyCo2;
    private Float MonthlyCo2;
    private Float YearlyCo2;
}
