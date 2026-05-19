package com.hotel.repositories;

import com.hotel.entity.Service;

import java.util.List;
import java.util.Map;

public interface ServiceRepository {
    List<Service> listService(Map<String, String> params);
    long countService(Map<String, String> params);
    void addOrUpdateService(Service service);
    void deleteService(int id);
    void deleteService(List<Integer> ids);
}
