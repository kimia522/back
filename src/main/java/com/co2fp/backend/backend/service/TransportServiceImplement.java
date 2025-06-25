package com.co2fp.backend.backend.service;

import com.co2fp.backend.backend.DTO.transport.TransportRegisterDTO;
import com.co2fp.backend.backend.DTO.transport.TransportResponseDTO;
import com.co2fp.backend.backend.entity.TransportEntity;
import com.co2fp.backend.backend.repository.TransportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransportServiceImplement implements TransportService{

    private final TransportRepository transportRepository;

    public TransportServiceImplement(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    @Override
    public List<TransportResponseDTO> getAllTransportmethod() {
        log.info("Fetching All TransferMethods");
        List<TransportEntity> transportEntities = transportRepository.findAll();
        List<TransportResponseDTO> transportResponseList =transportEntities.stream()
                .map(this::convertToTransportResponseDTO)
                .collect(Collectors.toList());
        log.info("Fetched ALl TransportMethod");
        return transportResponseList;
    }

    @Override
    public TransportResponseDTO getTransportmethodById(Long Transport_Id) {
        log.info("Fetching TransferMethod By Id: {}",Transport_Id);
        TransportEntity transportEntity=transportRepository.findById(Transport_Id)
                .orElseThrow(()->new RuntimeException("Transport with given doesnt exists"));
        TransportResponseDTO transportResponse= convertToTransportResponseDTO(transportEntity);
        log.info("Fetched TransferMethod by id: {}",Transport_Id);
        return transportResponse;
    }

    @Override
    public Map<String, String> deleteTransport(Long Transport_Id) {
        log.info("Request to delete transport with ID: " + Transport_Id);

        if (!transportRepository.existsById(Transport_Id)) {
            log.warn("Transport method not found.");
            return Map.of("error", "Transport method not found.");
        }

        TransportEntity transportEntity = transportRepository.findById(Transport_Id).orElseThrow();

        if (!transportEntity.getAnswer().isEmpty()) {
            log.warn("Cannot delete transport with ID " + Transport_Id + " because it has linked answers.");
            return Map.of("error", "Cannot delete: Transport method is linked to existing answers.");
        }

        transportRepository.deleteById(Transport_Id);
        log.info("Transport method deleted successfully.");

        return Map.of("message", "Transport method deleted successfully.");
    }

    @Override
    public Map<String, String> deleteTransportCascade(Long Transport_Id) {
        log.info("Request to cascade delete transport with ID: " + Transport_Id);

        if (!transportRepository.existsById(Transport_Id)) {
            log.warn("Transport method not found.");
            return Map.of("error", "Transport method not found.");
        }

        transportRepository.deleteById(Transport_Id);
        log.info("Transport method and linked answers deleted successfully.");

        return Map.of("message", "Transport method and all linked answers deleted successfully.");
    }

    @Override
    public Map<String, String> deleteTransportAndKeepAnswers(Long Transport_Id) {
        log.info("Request to delete transport with ID: " + Transport_Id + " but keep answers.");

        if (!transportRepository.existsById(Transport_Id)) {
            log.warn("Transport method not found.");
            return Map.of("error", "Transport method not found.");
        }

        TransportEntity transport = transportRepository.findById(Transport_Id).orElseThrow();


        transport.getAnswer().forEach(answer -> answer.setTransport(null));

        transportRepository.deleteById(Transport_Id);

        log.info("Transport method deleted, but linked answers were kept.");
        return Map.of("message", "Transport method deleted, but linked answers were kept.");
    }

    @Override
    public Map<String, String> setMethod(TransportRegisterDTO transportRegisterDTO) {
        log.info("Saving a new transportmethod...");

        try {
            TransportEntity transportEntity = TransportEntity.builder()
                    .transport_name(transportRegisterDTO.getTransport_name())
                    .fuel_factor(transportRegisterDTO.getFuel_factor())
                    .emission_factor(transportRegisterDTO.getEmission_factor())
                    .build();

            //
            TransportEntity savedTransport = transportRepository.save(transportEntity);

            log.info("Transport saved successfully with ID: " + savedTransport.getTransport_id());

            return Map.of(
                    "message", "TransportMethod saved successfully",
                    "answer_id", String.valueOf(savedTransport.getTransport_id()) //
            );

        } catch (Exception e) {
            log.error("Error saving transport: " + e.getMessage());
            return Map.of("error", "Failed to save transport: " + e.getMessage());
        }
    }

    private TransportResponseDTO convertToTransportResponseDTO(TransportEntity transportEntity) {
        return TransportResponseDTO.builder()
                .transport_id(transportEntity.getTransport_id())
                .transport_name(transportEntity.getTransport_name())
                .emission_factor(transportEntity.getEmission_factor())
                .fuel_factor(transportEntity.getFuel_factor())
                .build();
    }
}
