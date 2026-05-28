package com.example.fjm0313_takeout_self.service;


import com.example.fjm0313_takeout_self.entity.AddressBook;

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