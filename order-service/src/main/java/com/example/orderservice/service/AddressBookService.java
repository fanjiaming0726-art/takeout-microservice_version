package com.example.orderservice.service;


import com.example.orderservice.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    void addAddress(AddressBook addressBook);
    void updateAddress(AddressBook addressBook);
    void deleteAddress(Long id);
    List<AddressBook> findByUserId(Long userId);
    AddressBook findById(Long id);
    void clearDefaultByUserId(Long userId);
    AddressBook findDefaultByUserId(Long userId);
}