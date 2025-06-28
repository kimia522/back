package com.co2fp.backend.backend.controller;

import com.co2fp.backend.backend.DTO.transport.TransportRegisterDTO;
import com.co2fp.backend.backend.DTO.transport.TransportResponseDTO;
import com.co2fp.backend.backend.service.AnswerServiceImplement;
import com.co2fp.backend.backend.service.TransportServiceImplement;
import com.co2fp.backend.backend.service.UserServiceImplement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transportmethods")
public class TransportController {
    private final UserServiceImplement userService;
    private final TransportServiceImplement transportService;
    private final AnswerServiceImplement answerService;
    //    private final ResulService
    public TransportController(UserServiceImplement userService, TransportServiceImplement transportService, AnswerServiceImplement answerService) {
        this.userService = userService;
        this.transportService = transportService;
        this.answerService = answerService;
    }
    @GetMapping()
//    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAllTransportmethod() {
        List<TransportResponseDTO> transportResponseDTOList=transportService.getAllTransportmethod();
        if(transportResponseDTOList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No transport Method found"));
        }else {
            return new ResponseEntity<>(transportResponseDTOList,HttpStatus.OK);
        }
    }
//    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createTransport(@RequestBody TransportRegisterDTO transportRegisterDTO) {
        Map<String, String> response = transportService.setMethod(transportRegisterDTO);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteTransport(@PathVariable Long id) {
        Map<String, String> response = transportService.deleteTransport(id);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/cascade/{id}")
    public ResponseEntity<Map<String, String>> deleteTransportCascade(@PathVariable Long id) {
        Map<String, String> response = transportService.deleteTransportCascade(id);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/keep-answers/{id}")
    public ResponseEntity<Map<String, String>> deleteTransportKeepAnswers(@PathVariable Long id) {
        Map<String, String> response = transportService.deleteTransportAndKeepAnswers(id);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

}
