package org.frc3620.timeclock.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Worksession;
import org.frc3620.timeclock.db.DAO;
import org.frc3620.timeclock.gui.FormEventListener;
import org.frc3620.timeclock.gui.TimeclockFrame;
import org.frc3620.timeclock.gui.PersonsStatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author wegscd
 */
@Component
public class App implements FormEventListener {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    TimeclockFrame timeclockFrame;

    void go() {
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
        timeclockStatusModel.reload();

        timeclockFrame = new TimeclockFrame(timeclockStatusModel, this);
        final TimeclockFrame timeclockFrame2 = timeclockFrame;
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                timeclockFrame2.setVisible(true);
            }
        });

        String time = null;
        SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
        while (true) {
            boolean windowClosed = timeclockFrame.isWindowClosing();
            // logger.info ("timeClockFrame closed = {}", windowClosed);
            if (windowClosed) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }

            final String newTime = sdt.format(new Date());
            if (!newTime.equals(time)) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        timeclockFrame.setTimeText(newTime);
                    }
                });
                time = newTime;
            }
        }

        logger.info("timeClockFrame closed");
        timeclockFrame.setVisible(false);
        logger.info("app.go() exiting");

    }

    private DAO dao;

    @Autowired
    public void setDao(DAO dao) {
        logger.info("{} set DAO to {}", this, dao);
        this.dao = dao;
    }

    private PersonsStatusModel timeclockStatusModel;

    @Autowired
    public void setTimeclockStatusModel(PersonsStatusModel timeclockStatusModel) {
        this.timeclockStatusModel = timeclockStatusModel;
    }

    @Override
    public void personSelected(Integer i) {
        Person person = timeclockStatusModel.getPersonAt(i);
        logger.info("selected {}: {}", i, person);
        updatePersonInfoOnScreen(person);
    }
    
    void updatePersonInfoOnScreen (Person person) {
        List<Worksession> worksessions = dao.fetchWorksessionsForPerson(person.getPersonId());
        Worksession lastWorksession = (worksessions.size() > 0) ? worksessions.get(0) : null;
        if (lastWorksession != null && lastWorksession.isToday() && lastWorksession.getEndDate() == null) {
            timeclockFrame.setCheckInButtonEnabled(false);
            timeclockFrame.setCheckOutButtonEnabled(true);
        } else {
            timeclockFrame.setCheckInButtonEnabled(true);
            timeclockFrame.setCheckOutButtonEnabled(false);
        }
    }
    @Override
    public void checkin(Integer i) {
        logger.info("checkin {}: {}", i, timeclockStatusModel.getPersonAt(i));
    }

    @Override
    public void checkout(Integer i) {
        logger.info("checkout {}: {}", i, timeclockStatusModel.getPersonAt(i));
    }

}
