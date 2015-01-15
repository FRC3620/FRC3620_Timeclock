package org.frc3620.timeclock.workhistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.frc3620.timeclock.Worksession;

/**
 *
 * @author wegscd
 */
public class HistoryPeriod {
    Date start;
    Period period;
    double hours = 0;
    Set<Date> days = new TreeSet<>();
    List<Worksession> validWorksessions = new ArrayList<>();
    List<Worksession> openWorksessions = new ArrayList<>();

    public HistoryPeriod(Date _start, Period _period) {
        super();
        this.start = _start;
        this.period = _period;
    }
    
    public void addValidWorksession (Worksession w) {
        validWorksessions.add(w);
    }
    
    public void addOpenWorksession (Worksession w) {
        openWorksessions.add(w);
    }
    
    public void addDay (Date d) {
        days.add(d);
    }
    
    public void addHours (double h) {
        hours += h;
    }

    public Date getStart() {
        return start;
    }

    public Period getPeriod() {
        return period;
    }

    public double getHours() {
        return hours;
    }

    public Set<Date> getDays() {
        return days;
    }

    public List<Worksession> getValidWorksessions() {
        return validWorksessions;
    }

    public List<Worksession> getOpenWorksessions() {
        return openWorksessions;
    }
    
}
