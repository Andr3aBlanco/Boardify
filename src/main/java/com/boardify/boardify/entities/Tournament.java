package com.boardify.boardify.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Primary Key
    private Long tournamentID;
    //Input fields
    private String tournamentName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private double  prize;
    private double entryFees;

    //Foreign keys
    private Long organizerID;


    //Calculated values
    private int status;
    private String compLevel;
    private String lastEdited;
    //IDK , please assign it to the corresponding part
    private String signupStart;
    private String signupEnd;
    private String eventStart;
    private String eventEnd;
    private int currEnrolled;
    private int maxEnrolled;
    public Long getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(Long tournamentID) {
        this.tournamentID = tournamentID;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public Long getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(Long organizerID) {
        this.organizerID = organizerID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSignupStart() {
        return signupStart;
    }

    public void setSignupStart(String singupStart) {
        this.signupStart = singupStart;
    }

    public String getSignupEnd() {
        return signupEnd;
    }

    public void setSignupEnd(String signupEnd) {
        this.signupEnd = signupEnd;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public int getCurrEnrolled() {
        return currEnrolled;
    }

    public void setCurrEnrolled(int currEnrolled) {
        this.currEnrolled = currEnrolled;
    }

    public int getMaxEnrolled() {
        return maxEnrolled;
    }

    public void setMaxEnrolled(int maxEnrolled) {
        this.maxEnrolled = maxEnrolled;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getCompLevel() {
        return compLevel;
    }

    public void setCompLevel(String compLevel) {
        this.compLevel = compLevel;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public double getEntryFees() {
        return entryFees;
    }

    public void setEntryFees(double entryFees) {
        this.entryFees = entryFees;
    }
}
