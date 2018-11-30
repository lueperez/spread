package com.luis.perez.data;

/**
 * @author Luis Eduardo Perez
 */
public class Data {

    private String[] data;
    private String[] spreadingMessage;
    private int spreadingCode;
    private int index;

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getSpreadingMessage() {
        return spreadingMessage;
    }

    public void setSpreadingMessage(String[] spreadingMessage) {
        this.spreadingMessage = spreadingMessage;
    }

    public int getSpreadingCode() {
        return spreadingCode;
    }

    public void setSpreadingCode(int spreadingCode) {
        this.spreadingCode = spreadingCode;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}