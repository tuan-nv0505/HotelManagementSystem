package com.hotel.services.impl;

import com.hotel.converter.ServiceConverter;
import com.hotel.dto.ServiceDTO;
import com.hotel.entity.Service;
import com.hotel.exceptions.ServiceInUseException;
import com.hotel.repositories.ServiceRepository;
import com.hotel.services.BookingServiceService;
import com.hotel.services.ServiceService;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private BookingServiceService bookingServiceService;

    @Autowired
    private ServiceConverter serviceConverter;

    @Override
    public List<ServiceDTO> list(Map<String, String> params) {
        return this.serviceConverter.toServiceDTO(this.serviceRepository.list(params));
    }

    @Override
    public long count(Map<String, String> params) {
        return this.serviceRepository.count(params);
    }

    @Override
    public void addOrUpdate(ServiceDTO serviceDTO) {
        this.serviceRepository.addOrUpdate(this.serviceConverter.toServiceEntity(serviceDTO));
    }

    @Override
    public void delete(int id) {
        if (this.bookingServiceService.existsByService(id)) {
            throw new ServiceInUseException("This service has already been used in booking data!");
        }
        this.serviceRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {

        if (this.bookingServiceService.existsByService(ids)) {
            throw new ServiceInUseException("This service has already been used in booking data!");
        }
        this.serviceRepository.delete(ids);
    }

    @Override
    public ServiceDTO get(int id) {
        return this.serviceConverter.toServiceDTO(this.serviceRepository.get(id));
    }

    @Override
    public ServiceDTO save(ServiceDTO entity) {
        return null;
    }
}
