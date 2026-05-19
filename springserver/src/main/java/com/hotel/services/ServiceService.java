package com.hotel.services;

import com.hotel.entity.Service;

import java.util.List;
import java.util.Map;

public interface ServiceService {
    List<Service> listService(Map<String, String> params);
    long countService(Map<String, String> params);
    void addOrUpdateService(Service service);
    void deleteService(int id);
    void deleteService(List<Integer> ids);
}
