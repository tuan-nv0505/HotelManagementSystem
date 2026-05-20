package com.hotel.services;

import com.hotel.dto.ServiceDTO;
import com.hotel.entity.Service;

import java.util.List;
import java.util.Map;

public interface ServiceService {
    List<ServiceDTO> listService(Map<String, String> params);
    long countService(Map<String, String> params);
    void addOrUpdateService(ServiceDTO serviceDTO);
    void deleteService(int id);
    void deleteService(List<Integer> ids);
}
