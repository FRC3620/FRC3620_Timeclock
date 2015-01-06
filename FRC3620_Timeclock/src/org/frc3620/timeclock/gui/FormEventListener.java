package org.frc3620.timeclock.gui;

/**
 *
 * @author wegscd
 */
public interface FormEventListener {
   public void personSelected (Integer i);
   public void checkin (Integer i);
   public void checkout (Integer i);
   public void addWorksession (Integer personIndex);
   public void editWorksession (Integer personIndex, Integer worksessionIndex);
   public void removeWorksession (Integer personIndex, Integer worksessionIndex);
   public void setMentorMode(Integer personIndex);
   public void clearMentorMode();
   public boolean checkMentorModePassword();
   public void backup();
}
