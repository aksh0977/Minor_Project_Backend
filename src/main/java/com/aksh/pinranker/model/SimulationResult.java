package com.aksh.pinranker.model;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {

    private boolean cracked;
    private int attemptsUsed;
    private String crackedPin;
    private final List<String> triedPins = new ArrayList<>();
    private boolean lockedOut;

    public boolean isCracked() {
        return cracked;
    }

    public void setCracked(boolean cracked) {
        this.cracked = cracked;
    }

    public int getAttemptsUsed() {
        return attemptsUsed;
    }

    public void setAttemptsUsed(int attemptsUsed) {
        this.attemptsUsed = attemptsUsed;
    }

    public String getCrackedPin() {
        return crackedPin;
    }

    public void setCrackedPin(String crackedPin) {
        this.crackedPin = crackedPin;
    }

    public List<String> getTriedPins() {
        return triedPins;
    }

    public void addTriedPin(String pin) {
        this.triedPins.add(pin);
    }

    public boolean isLockedOut() {
        return lockedOut;
    }

    public void setLockedOut(boolean lockedOut) {
        this.lockedOut = lockedOut;
    }
}
