package com.footprint.mybatis.generator.mapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.footprint.mybatis.generator.entity.BillEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author hui.zhou 9:27 2018/1/24
 */
public class BillEntityMapperTest {
    ClassPathXmlApplicationContext ac;
    BillEntityMapper mapper;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("spring-mybatis.xml");
        ac.registerShutdownHook();
        mapper = ac.getBean(BillEntityMapper.class);
    }

    @After
    public void destroy(){
        ac.close();
    }

    @Test
    public void testInsert(){
        BillEntity entity = new BillEntity();
        entity.setBillDate(LocalDate.now());
        entity.setBillRequestNo(System.currentTimeMillis() + "");
        entity.setBillStatus("SUCCESS");
        entity.setCreateTime(LocalDateTime.now());

        mapper.insertSelective(entity);
    }

    @Test
    public void testQuery(){
        System.out.println(mapper.selectByPrimaryKey(51l).getBillDate());
    }

    @Test
    public void testUpdate(){
        BillEntity entity = mapper.selectByPrimaryKey(51l);
        entity.setBillStatus("FAILD");
        int updateCount = mapper.updateByPrimaryKeySelective(entity);
        Assert.assertEquals(1, updateCount);
    }
}
