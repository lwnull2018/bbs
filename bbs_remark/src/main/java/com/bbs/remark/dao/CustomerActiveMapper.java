package com.bbs.remark.dao;

import com.bbs.remark.pojo.CustomerActive;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Mapper
@Component
public interface CustomerActiveMapper {

    int insert(CustomerActive record);

    int updateByPrimaryKey(CustomerActive record);

    CustomerActive findOne(CustomerActive record);

    CustomerActive findActiveOne(CustomerActive record);

    CustomerActive findActiveWork(CustomerActive record);

    int count(CustomerActive doingActive);

    long statAllTimesPerDay(CustomerActive record);
}
