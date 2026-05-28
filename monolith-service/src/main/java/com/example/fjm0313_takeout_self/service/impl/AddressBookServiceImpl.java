package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.fjm0313_takeout_self.entity.AddressBook;
import com.example.fjm0313_takeout_self.mapper.AddressBookMapper;
import com.example.fjm0313_takeout_self.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;


    @Override
    public void addAddress(AddressBook addressBook) {
        addressBookMapper.insert(addressBook);
    }

    @Override
    public void updateAddress(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    @Override
    public void deleteAddress(Long id) {
        addressBookMapper.deleteById(id);
    }

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
    public void clearDefaultByUserId(Long userId) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookMapper.update(null, wrapper);
    }

    @Override
    public AddressBook findDefaultByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.eq(AddressBook::getIsDefault, 1);
        return addressBookMapper.selectOne(wrapper);
    }
}