package com.hotel.converter;

import com.hotel.dto.ServiceDTO;
import com.hotel.entity.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceConverter {
    @Autowired
    private ModelMapper modelMapper;

    public ServiceDTO toServiceDTO(Service serviceEntity) {
        return this.modelMapper.map(serviceEntity, ServiceDTO.class);
    }

    public List<ServiceDTO> toServiceDTO(List<Service> listServiceEntity) {
        return listServiceEntity.stream().map(
                element-> modelMapper.map(element, ServiceDTO.class)
        ).collect(Collectors.toList());
    }

    public Service toServiceEntity(ServiceDTO serviceDTO) {
        return this.modelMapper.map(serviceDTO, Service.class);
    }
}
