package org.frc3620.timeclock.app;

import org.frc3620.timeclock.db.DAO;
import org.frc3620.timeclock.gui.TimeclockFrame;
import org.frc3620.timeclock.gui.TimeclockStatusModel;
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

        final TimeclockFrame timeClockFrame = new TimeclockFrame(timeclockStatusModel);
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
    
    private TimeclockStatusModel timeclockStatusModel;
    @Autowired
    public void setTimeclockStatusModel (TimeclockStatusModel timeclockStatusModel) {
        this.timeclockStatusModel = timeclockStatusModel;
    }

}
