package org.frc3620.timeclock.workhistory;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import org.frc3620.timeclock.Person;

/**
 *
 * @author wegscd
 */
public class PersonWithHistory {

    Person person;
    SortedMap<Date, HistoryPeriod> historyPeriods = new TreeMap<>();

    public PersonWithHistory(Person _person) {
        super();
        this.person = _person;
    }

    public Person getPerson() {
        return person;
    }

    public HistoryPeriod getOrCreateHistoryPeriod(Date d, Period p) {
        HistoryPeriod rv = historyPeriods.get(d);
        if (null == rv) {
            rv = new HistoryPeriod(d, p);
            historyPeriods.put(d, rv);
        }
        return rv;
    }

    public SortedMap<Date, HistoryPeriod> getHistoryPeriods() {
        return historyPeriods;
    }

}
