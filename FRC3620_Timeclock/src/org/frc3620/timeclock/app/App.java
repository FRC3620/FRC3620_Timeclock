package org.frc3620.timeclock.app;

import java.util.Date;
import java.util.List;
import org.aspectj.weaver.loadtime.Aj;
import org.frc3620.timeclock.CurrentStatus;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Where;
import org.frc3620.timeclock.db.DAO;
import org.frc3620.timeclock.gui.TimeClockFrame;
import org.frc3620.timeclock.gui.TimeClockStatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author wegscd
 */
@Component
public class App {

    Logger logger = LoggerFactory.getLogger(getClass());

    void go() {
        logger.info("{} dao = {} ", this, dao);

        List<Person> persons = dao.fetchPersons();
        for (Person person : persons) {
            logger.info("test1 = {} ", person);
        }

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.error("Trouble setting look and feel", ex);
        }
        //</editor-fold>

        //</editor-fold>
        TimeClockStatusModel t = new TimeClockStatusModel();
        t.addTimeClockStatus(new CurrentStatus(1, "Wegscheid, Douglas", Where.IN, new Date()));
        t.addTimeClockStatus(new CurrentStatus(2, "Wegscheid, Tearesa", null, null));
        t.addTimeClockStatus(new CurrentStatus(3, "Miller, Mike", Where.OUT, new Date()));

        final TimeClockFrame timeClockFrame = new TimeClockFrame(t);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                timeClockFrame.setVisible(true);
            }
        });

        while (true) {
            boolean windowClosed = timeClockFrame.isWindowClosing();
            // logger.info ("timeClockFrame closed = {}", windowClosed);
            if (windowClosed) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        logger.info("timeClockFrame closed");
        timeClockFrame.setVisible(false);
        logger.info("app.go() exiting");

    }

    private DAO dao;

    @Autowired
    public void setDao(DAO dao) {
        logger.info("{} set DAO to {}", this, dao);
        this.dao = dao;
    }

}
