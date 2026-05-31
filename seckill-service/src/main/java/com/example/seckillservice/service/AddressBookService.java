package com.example.seckillservice.service;


import com.example.seckillservice.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    List<AddressBook> findByUserId(Long userId);
    AddressBook findById(Long id);
    AddressBook findDefaultByUserId(Long userId);
}