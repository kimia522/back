package com.co2fp.backend.backend.DTO.transport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportResponseDTO {
    private Long transport_id;
    private String transport_name;
    private Float fuel_factor;
    private Float emission_factor;

}