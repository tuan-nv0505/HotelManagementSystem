package com.hotel.services;

import com.hotel.dto.BaseDTO;
import com.hotel.dto.RoomTypeDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    List<T> list(Map<String, String> params);
    long count(Map<String, String> params);
    void addOrUpdate(T entity);
    void delete(int id);
    void delete(List<Integer> ids);
    T get(int id);
    T save(T entity);
}
