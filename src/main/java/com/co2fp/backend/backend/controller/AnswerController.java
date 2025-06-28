package com.co2fp.backend.backend.controller;

import com.co2fp.backend.backend.DTO.answer.AnswerByDateDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerRegisterDTO;
import com.co2fp.backend.backend.DTO.answer.AnswerResponseDTO;
import com.co2fp.backend.backend.service.AnswerServiceImplement;
import com.co2fp.backend.backend.service.TransportServiceImplement;
import com.co2fp.backend.backend.service.UserServiceImplement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final UserServiceImplement userService;
    private final TransportServiceImplement transportService;
    private final AnswerServiceImplement answerService;

    public AnswerController(UserServiceImplement userService, TransportServiceImplement transportService, AnswerServiceImplement answerService) {
        this.userService = userService;
        this.transportService = transportService;
        this.answerService = answerService;
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping()
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswers(){
        List<AnswerResponseDTO> answerResponseDTOList= answerService.getAllAnswers();
        return new ResponseEntity<>(answerResponseDTOList, HttpStatus.OK);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswersOfUser(@PathVariable Long id){
        List<AnswerResponseDTO> answerResponseDTOList= answerService.getAllAnswersOfUser(id);
        return new ResponseEntity<>(answerResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/AnswerByDate")
    public ResponseEntity<AnswerByDateDTO> getAllAnswersOfUserByDate(@PathVariable Long id) {
        Optional<AnswerByDateDTO> answerByDateDTO = answerService.getAnswerByDate(id);
        if (answerByDateDTO.isPresent()) {
            return ResponseEntity.ok(answerByDateDTO.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/extra")
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswersExtra(){
        List<AnswerResponseDTO> answerResponseDTOList = answerService.getAllAnswersExtra();
        return new ResponseEntity<>(answerResponseDTOList,HttpStatus.OK);
    }

    @PostMapping("/set")
    public ResponseEntity<Map<String, String>> createAnswer(@RequestBody AnswerRegisterDTO answerRegisterDTO) {
        Map<String, String> response = answerService.setAnswer(answerRegisterDTO);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete/{id}")
    public void deleteanswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
    }

}
