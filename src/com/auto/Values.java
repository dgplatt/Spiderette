package com.auto;

import java.util.ArrayList;
import java.util.Collections;

public class Values {
    private int spaceValue;
    private int unknownValue;
    private int disorderedValue;
    private int orderedValue;
    private int suitedValue;

    /* 
    * ---------Value of removing a card from underneath card ---------
    * spaceValue :  Value of a free space
    * unknownValue :  Value of a unknown card
    * disorderedValue :  Value of removing a card from another not in numerical order
    * orderedValue :  Value of having cards in numerical order but not of the same suit
    * suitedValue :  Value of having cards in numerical order and of the same suit
    */
    public Values(int spaceValue, int unknownValue, int disorderedValue, int orderedValue, int suitedValue) {
        this.spaceValue = spaceValue;
        this.unknownValue = unknownValue;
        this.disorderedValue = disorderedValue;
        this.orderedValue = orderedValue;
        this.suitedValue = suitedValue;
    }

    public int getMaxIncrease() {
        int maxIncrease = 0;
        ArrayList<Integer> removeList = new ArrayList<Integer>();
        removeList.add(spaceValue);
        removeList.add(unknownValue);
        removeList.add(disorderedValue);
        removeList.add(orderedValue);
        removeList.add(suitedValue);

        maxIncrease = Collections.max(removeList);

        ArrayList<Integer> addList = new ArrayList<Integer>();
        addList.add(-spaceValue);
        //removeList.add(-unknownValue); Can't add on to unknown
        //removeList.add(-disorderedValue); Can't add on to disordered
        addList.add(-orderedValue);
        addList.add(-suitedValue);

        maxIncrease += Collections.max(addList);

        return maxIncrease;
    }

    public int getMaxDecrease() {
        int maxDecrease = 0;
        ArrayList<Integer> removeList = new ArrayList<Integer>();
        removeList.add(spaceValue);
        removeList.add(unknownValue);
        removeList.add(disorderedValue);
        removeList.add(orderedValue);
        removeList.add(suitedValue);

        maxDecrease = Collections.min(removeList);

        ArrayList<Integer> addList = new ArrayList<Integer>();
        addList.add(-spaceValue);
        //removeList.add(-unknownValue); Can't add on to unknown
        //removeList.add(-disorderedValue); Can't add on to disordered
        addList.add(-orderedValue);
        addList.add(-suitedValue);

        maxDecrease += Collections.min(addList);

        return -maxDecrease;
    }

    public int getSpaceValue() {
        return spaceValue;
    }

    public void setSpaceValue(int spaceValue) {
        this.spaceValue = spaceValue;
    }

    public int getUnknownValue() {
        return unknownValue;
    }

    public void setUnknownValue(int unknownValue) {
        this.unknownValue = unknownValue;
    }

    public int getDisorderedValue() {
        return disorderedValue;
    }

    public void setDisorderedValue(int disorderedValue) {
        this.disorderedValue = disorderedValue;
    }

    public int getOrderedValue() {
        return orderedValue;
    }

    public void setOrderedValue(int orderedValue) {
        this.orderedValue = orderedValue;
    }

    public int getSuitedValue() {
        return suitedValue;
    }

    public void setSuitedValue(int suitedValue) {
        this.suitedValue = suitedValue;
    }
}
