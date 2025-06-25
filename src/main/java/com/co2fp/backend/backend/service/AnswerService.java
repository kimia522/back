package com.co2fp.backend.backend.service;
import com.co2fp.backend.backend.DTO.answer.AnswerRegisterDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerResponseDTO;

import java.util.List;
import java.util.Map;

public interface AnswerService {
    List<AnswerResponseDTO> getAllAnswers();
    List<AnswerResponseDTO> getAllAnswersExtra();
    public Map<String, String> setAnswer(AnswerRegisterDTO answerDTO);
    void deleteAnswer(Long id);
}