package org.frc3620.timeclock;

/**
 *
 * @author wegscd
 */
public class Person {
    private Integer personId;
    private String lastName, firstName;
    private Boolean mentor;

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getMentor() {
        return mentor;
    }

    public void setMentor(Boolean mentor) {
        this.mentor = mentor;
    }
    
    public String getName() {
        return lastName + ", " + firstName;
    }

    @Override
    public String toString() {
        return "Person{" + "personId=" + personId + ", lastname=" + lastName + ", firstname=" + firstName + ", mentor=" + mentor + '}';
    }
}
