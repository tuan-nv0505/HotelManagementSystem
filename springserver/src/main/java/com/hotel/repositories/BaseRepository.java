package com.hotel.repositories;

import java.util.List;
import java.util.Map;

public interface BaseRepository<T> {
    List<T> list(Map<String, String> params);
    long count(Map<String, String> params);
    void addOrUpdate(T dto);
    void delete(int id);
    void delete(List<Integer> ids);
}
