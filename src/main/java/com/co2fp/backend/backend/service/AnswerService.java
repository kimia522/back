package com.co2fp.backend.backend.service;
import com.co2fp.backend.backend.DTO.answer.AnswerByDateDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerRegisterDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AnswerService {
    List<AnswerResponseDTO> getAllAnswers();
    List<AnswerResponseDTO> getAllAnswersExtra();
    public Map<String, String> setAnswer(AnswerRegisterDTO answerDTO);
    void deleteAnswer(Long id);
    public List<AnswerResponseDTO> getAllAnswersOfUser(Long id);
    public Optional<AnswerByDateDTO> getAnswerByDate(Long id);
}