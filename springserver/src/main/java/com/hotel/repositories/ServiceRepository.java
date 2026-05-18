package com.hotel.repositories;

import com.hotel.entity.Service;

import java.util.List;

public interface ServiceRepository {
    List<Service> listService();
    void addOrUpdateService(Service service);
}
