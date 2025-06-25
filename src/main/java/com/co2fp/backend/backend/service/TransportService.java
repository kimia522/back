package com.co2fp.backend.backend.service;

import com.co2fp.backend.backend.DTO.transport.TransportRegisterDTO;
import com.co2fp.backend.backend.DTO.transport.TransportResponseDTO;

import java.util.List;
import java.util.Map;

public interface TransportService {
    List<TransportResponseDTO> getAllTransportmethod();
    TransportResponseDTO getTransportmethodById(Long  Transport_Id);
    Map<String, String> deleteTransport(Long Transport_Id);
    Map<String, String> deleteTransportCascade(Long Transport_Id);
    Map<String, String> deleteTransportAndKeepAnswers(Long Transport_Id);
    public Map<String, String> setMethod(TransportRegisterDTO transportRegisterDTO);
}
