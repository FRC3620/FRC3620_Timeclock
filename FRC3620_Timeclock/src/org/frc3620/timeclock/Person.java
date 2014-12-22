package org.frc3620.timeclock;

/**
 *
 * @author wegscd
 */
public class Person {
    private Integer personId;
    private String lastname, firstname;
    private Boolean mentor;

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Boolean getMentor() {
        return mentor;
    }

    public void setMentor(Boolean mentor) {
        this.mentor = mentor;
    }

    @Override
    public String toString() {
        return "Person{" + "personId=" + personId + ", lastname=" + lastname + ", firstname=" + firstname + ", mentor=" + mentor + '}';
    }
}
