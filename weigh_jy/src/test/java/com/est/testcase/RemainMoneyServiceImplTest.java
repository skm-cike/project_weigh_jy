package com.est.testcase;

import com.est.weigh.report.service.IRemainMoneyService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 陆华 on 16-3-29 上午10:38
 */

public class RemainMoneyServiceImplTest {
    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-*.xml");
        System.out.println(context.getBeansOfType(IRemainMoneyService.class));
    }
}
