package com.hotel.services.impl;

import com.hotel.converter.ServiceConverter;
import com.hotel.dto.ServiceDTO;
import com.hotel.entity.Service;
import com.hotel.repositories.ServiceRepository;
import com.hotel.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceConverter serviceConverter;

    @Override
    public List<ServiceDTO> listService(Map<String, String> params) {
        return this.serviceConverter.toServiceDTO(this.serviceRepository.listService(params));
    }

    @Override
    public long countService(Map<String, String> params) {
        return this.serviceRepository.countService(params);
    }

    @Override
    public void addOrUpdateService(ServiceDTO serviceDTO) {
        this.serviceRepository.addOrUpdateService(this.serviceConverter.toServiceEntity(serviceDTO));
    }

    @Override
    public void deleteService(int id) {
        this.serviceRepository.deleteService(id);
    }

    @Override
    public void deleteService(List<Integer> ids) {
        this.serviceRepository.deleteService(ids);
    }
}
