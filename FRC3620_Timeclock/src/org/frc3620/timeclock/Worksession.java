/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.frc3620.timeclock;

import java.util.Date;

/**
 *
 * @author wegscd
 */
public class Worksession {
    private Integer worksessionId, personId;
    private Date startDate, originalStartDate, endDate, originalEndDate;
    private boolean removed;

    public Integer getWorksessionId() {
        return worksessionId;
    }

    public void setWorksessionId(Integer worksessionId) {
        this.worksessionId = worksessionId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getOriginalStartDate() {
        return originalStartDate;
    }

    public void setOriginalStartDate(Date originalStartDate) {
        this.originalStartDate = originalStartDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getOriginalEndDate() {
        return originalEndDate;
    }

    public void setOriginalEndDate(Date originalEndDate) {
        this.originalEndDate = originalEndDate;
    }
    
    public boolean isToday() {
        return Utils.getStartOfDay(new Date()).before(startDate);
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "Worksession{" + "worksessionId=" + worksessionId + ", personId=" + personId + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
}
