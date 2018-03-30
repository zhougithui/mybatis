package com.footprint.mybatis.generator.mapper;

import com.footprint.mybatis.generator.entity.BillEntity;
import com.footprint.mybatis.generator.optimisticlock.annotation.OptimisticLock;


public interface BillEntityMapper {

    BillEntity selectByPrimaryKey(Long id);

    @OptimisticLock
    int updateByPrimaryKeySelective(BillEntity record);

    int insertSelective(BillEntity record);
}