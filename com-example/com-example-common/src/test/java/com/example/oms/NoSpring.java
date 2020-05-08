package com.example.oms;

import com.example.common.exception.BusinessException;
import org.junit.jupiter.api.Test;

public class NoSpring {

    @Test
    public void test123() {
        test1223();

    }

    @Test
    public void test1223() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new BusinessException("除数不能大于0");
        }finally {
            System.out.println("记录保存");
        }
    }
}
