package org.frc3620.timeclock.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author wegscd
 */
public class AppWrapper {
    static Logger logger = LoggerFactory.getLogger(AppWrapper.class);

    public static void main(String[] args) {
        Throwable boom = null;
        try {
            ApplicationContext springApplicationContext = new org.springframework.context.support.ClassPathXmlApplicationContext("spring.xml");
            App app = springApplicationContext.getBean(App.class);
            app.go();
            logger.info("app.go() returned");
        } catch (RuntimeException ex) {
            boom = ex;
        }
        if (boom != null) {
            boom.printStackTrace();
        }
    }
}
