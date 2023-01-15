package com.bbs.remark.service;

import com.bbs.remark.dao.CustomerActiveMapper;
import com.bbs.remark.pojo.CustomerActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Service
public class CustomerActiveService {

    @Autowired
    private CustomerActiveMapper customerActiveMapper;

    private CustomerActiveMapper getMapper() {
        return customerActiveMapper;
    }

    public int insert(CustomerActive record) {
        return getMapper().insert(record);
    }

    public CustomerActive findOne(CustomerActive record) {
        return getMapper().findOne(record);
    }

    public CustomerActive findActiveOne(CustomerActive record) {
        return getMapper().findActiveOne(record);
    }

    public CustomerActive findActiveWork(CustomerActive record) {
        return getMapper().findActiveWork(record);
    }

    public int updateByPrimaryKey(CustomerActive doingActive) {
        return getMapper().updateByPrimaryKey(doingActive);
    }

    public int count(CustomerActive doingActive) {
        return getMapper().count(doingActive);
    }

    /**
     * 回座后统计指定该人员一天总用时
     * @param record
     * @return 返回总用时的秒数
     */
    public long statAllTimesPerDay(CustomerActive record) {
        return getMapper().statAllTimesPerDay(record);
    }

}
