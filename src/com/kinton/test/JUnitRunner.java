package com.kinton.test;

import com.yum.boh.core.util.StringUtil;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by mgkj1 on 2016/10/19.
 */
public class JUnitRunner {
    /**
     * @param args
     */
    public static void main(String[] args) {
        if(args!=null && args.length>0) {
            String testClassname = args[0];
            if(StringUtil.isNotEmpty(testClassname)) {
                try {
                    Class<?> clazz = Class.forName(testClassname);
                    Result result = JUnitCore.runClasses(clazz);
                    for (Failure failure : result.getFailures()) {
                        System.out.println(failure.toString());
                    }
                    if (result.wasSuccessful()) {
                        System.out.println("【Azure POC】all junit successfully executed");
                    }
                }
                catch (Exception ex) {
                    System.out.println("【Azure POC】error: " + ex.getMessage());
                }
            }
            else {
                System.out.println("【Azure POC】illegal classname");
            }
        }
        else {
            System.out.println("【Azure POC】illegal param");
        }
    }
}
