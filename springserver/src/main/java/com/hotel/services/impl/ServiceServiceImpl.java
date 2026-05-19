package com.hotel.services.impl;

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

    @Override
    public List<Service> listService(Map<String, String> params) {
        return this.serviceRepository.listService(params);
    }

    @Override
    public long countService(Map<String, String> params) {
        return this.serviceRepository.countService(params);
    }

    @Override
    public void addOrUpdateService(Service service) {
        this.serviceRepository.addOrUpdateService(service);
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
