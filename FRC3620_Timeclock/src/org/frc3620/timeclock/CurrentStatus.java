package org.frc3620.timeclock;

import java.util.Date;

/**
 *
 * @author wegscd
 */
public class CurrentStatus implements Comparable<CurrentStatus> {
    private Integer key;
    private String who;
    private Where where;
    private Date when;

    public CurrentStatus(Integer key, String who, Where where, Date when) {
        this.key = key;
        this.who = who;
        this.where = where;
        this.when = when;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    @Override
    public int compareTo(CurrentStatus o) {
        return getWho().compareToIgnoreCase(o.getWho());
    }
    
}
