package com.example.seckillservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.seckillservice.entity.AddressBook;
import com.example.seckillservice.mapper.AddressBookMapper;
import com.example.seckillservice.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> findByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);
        return addressBookMapper.selectList(wrapper);
    }

    @Override
    public AddressBook findById(Long id) {
        return addressBookMapper.selectById(id);
    }

    @Override
    public AddressBook findDefaultByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.eq(AddressBook::getIsDefault, 1);
        return addressBookMapper.selectOne(wrapper);
    }
}