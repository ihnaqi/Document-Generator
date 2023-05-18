/*
 * I can remove this file too.
 */
package com.pdfsolutions.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private float ID;
    private String ID_FILE;
    private String TIME_ADD;
    private String TIME_CONVERTED;
    private String CONVERTED_FILE;
    private String STATUS;
    private float AUTHOR;
    private float CONVERTED_SIZE;

    // Getter Methods

    public float getID() {
        return ID;
    }

    public String getID_FILE() {
        return ID_FILE;
    }

    public String getTIME_ADD() {
        return TIME_ADD;
    }

    public String getTIME_CONVERTED() {
        return TIME_CONVERTED;
    }

    public String getCONVERTED_FILE() {
        return CONVERTED_FILE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public float getAUTHOR() {
        return AUTHOR;
    }

    public float getCONVERTED_SIZE() {
        return CONVERTED_SIZE;
    }

    // Setter Methods

    public void setID(float ID) {
        this.ID = ID;
    }

    public void setID_FILE(String ID_FILE) {
        this.ID_FILE = ID_FILE;
    }

    public void setTIME_ADD(String TIME_ADD) {
        this.TIME_ADD = TIME_ADD;
    }

    public void setTIME_CONVERTED(String TIME_CONVERTED) {
        this.TIME_CONVERTED = TIME_CONVERTED;
    }

    public void setCONVERTED_FILE(String CONVERTED_FILE) {
        this.CONVERTED_FILE = CONVERTED_FILE;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public void setAUTHOR(float AUTHOR) {
        this.AUTHOR = AUTHOR;
    }
    public void setCONVERTED_SIZE(float CONVERTED_SIZE) {
        this.CONVERTED_SIZE = CONVERTED_SIZE;
    }
}
