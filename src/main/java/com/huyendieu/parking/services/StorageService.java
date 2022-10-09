package com.huyendieu.parking.services;

public interface StorageService {

    String store(String base64);

    void delete(String fileUrl);
}
