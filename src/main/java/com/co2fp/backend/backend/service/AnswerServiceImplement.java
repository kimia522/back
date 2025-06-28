package com.co2fp.backend.backend.service;

import com.co2fp.backend.backend.DTO.answer.AnswerByDateDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerRegisterDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerResponseDTO;
import com.co2fp.backend.backend.entity.AnswerEntity;
import com.co2fp.backend.backend.entity.TransportEntity;
import com.co2fp.backend.backend.entity.UserEntity;
import com.co2fp.backend.backend.repository.AnswerRepository;
import com.co2fp.backend.backend.repository.TransportRepository;
import com.co2fp.backend.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnswerServiceImplement implements AnswerService{
    private final AnswerRepository answerRepository;

    private final TransportRepository transportRepository;

    private final UserRepository userRepository;

    public AnswerServiceImplement(AnswerRepository answerRepository, TransportRepository transportRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.transportRepository = transportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AnswerResponseDTO> getAllAnswers() {
        log.info("Fetching All Answers");
        List<AnswerEntity> answerEntityList = answerRepository.findAll();
        List<AnswerResponseDTO> answerResponseDTOList =answerEntityList.stream()
                .map(this::convertToAnswerResponseDTO)
                .collect(Collectors.toList());
        log.info("Fetched ALl Answers");
        return answerResponseDTOList;
    }

    @Override
    public List<AnswerResponseDTO> getAllAnswersExtra() {
        return List.of();
    }

    @Override
    public Map<String, String> setAnswer(AnswerRegisterDTO answerDTO) {
        log.info("Saving a new answer...");

        try {
            //
            TransportEntity transportEntity = transportRepository.findById(answerDTO.getTransport_id())
                    .orElseThrow(() -> new RuntimeException("Transport not found with ID: " + answerDTO.getTransport_id()));

            //
            UserEntity userEntity = userRepository.findById(answerDTO.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + answerDTO.getUser_id()));

            //
            Float co2_emmission_calc = (transportEntity.getFuel_factor()*transportEntity.getEmission_factor()*answerDTO.getDistance().floatValue())/answerDTO.getPassenger_count();
            //
            AnswerEntity answerEntity = AnswerEntity.builder()
                    .date(answerDTO.getDate())
                    .time(answerDTO.getTime())
                    .distance(answerDTO.getDistance())
                    .passenger_count(answerDTO.getPassenger_count())
                    .co2_emmission(co2_emmission_calc)
                    .transport(transportEntity)
                    .user(userEntity)
                    .build();

            //
            AnswerEntity savedAnswer = answerRepository.save(answerEntity);

            log.info("Answer saved successfully with ID: " + savedAnswer.getAnswer_id());

            return Map.of(
                    "message", "Answer saved successfully",
                    "answer_id", String.valueOf(savedAnswer.getAnswer_id()) //
            );

        } catch (Exception e) {
            log.error("Error saving answer: " + e.getMessage());
            return Map.of("error", "Failed to save answer: " + e.getMessage());
        }
    }

    @Override
    public void deleteAnswer(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new RuntimeException("Answer with ID " + id + " not found.");
        }
        answerRepository.deleteById(id);
    }

    @Override
    public List<AnswerResponseDTO> getAllAnswersOfUser(Long id) {
        log.info("Fetching All Answers of user");
        List<AnswerEntity> answerEntityList = answerRepository.findByUser_UserId(id);
        List<AnswerResponseDTO> answerResponseDTOList =answerEntityList.stream()
                .map(this::convertToAnswerResponseDTO)
                .collect(Collectors.toList());
        log.info("Fetched ALl Answers");
        return answerResponseDTOList;
    }

    @Override
    public Optional<AnswerByDateDTO> getAnswerByDate(Long id) {

            // local date now
            LocalDate today = LocalDate.now();
            // calculate emission of today
            List<AnswerEntity> answerEntityListday = answerRepository.findByUser_UserIdAndDate(id,today);
            Float totalEmissionDay = answerEntityListday.stream()
                    .map(AnswerEntity::getCo2_emmission)
                    .reduce(0f, Float::sum);
            // calculate emmission of this month
            LocalDate firstDay = today.withDayOfMonth(1);
            LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
            List<AnswerEntity> answerEntityListmonth =
                    answerRepository.findByUser_UserIdAndDateBetween(id, firstDay, lastDay);
            Float totalEmissionMonth = answerEntityListmonth.stream()
                    .map(AnswerEntity::getCo2_emmission)
                    .reduce(0f, Float::sum);
            // calculate emmission of this year
            LocalDate firstDayOfYear = today.withDayOfYear(1);
            LocalDate lastDayOfYear = today.withDayOfYear(today.lengthOfYear());

            List<AnswerEntity> answerEntityListyear =
                    answerRepository.findByUser_UserIdAndDateBetween(id, firstDayOfYear, lastDayOfYear);

            Float totalEmissionYear = answerEntityListyear.stream()
                    .map(AnswerEntity::getCo2_emmission)
                    .reduce(0f, Float::sum);

            AnswerByDateDTO answerByDateDTO = AnswerByDateDTO.builder()
                    .DailyCo2(totalEmissionDay)
                    .MonthlyCo2(totalEmissionMonth)
                    .YearlyCo2(totalEmissionYear)
                    .build();

            return Optional.ofNullable(answerByDateDTO);
    }

    private AnswerResponseDTO convertToAnswerResponseDTO(AnswerEntity answerEntity) {
        return AnswerResponseDTO.builder()
                .answer_id(answerEntity.getAnswer_id())
                .date(answerEntity.getDate())
                .time(answerEntity.getTime())
                .distance(answerEntity.getDistance())
                .passenger_count(answerEntity.getPassenger_count())
                .co2_emmission(answerEntity.getCo2_emmission())
                .transport_name(answerEntity.getTransport().getTransport_name())
                .transport_id(answerEntity.getTransport().getTransport_id())
                .username(answerEntity.getUser().getUsername())
                .user_id(answerEntity.getUser().getUserId())
                .build();
    }
}
