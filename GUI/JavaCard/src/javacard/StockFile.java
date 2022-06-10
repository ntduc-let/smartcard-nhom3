/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import java.io.Serializable;

/**
 *
 * @author kqhuynh
 */
public class StockFile implements Serializable{
    private final String dateString;
    private final String startTimeString;
    private final String endTimeString;
    public StockFile(String dateString,String startTimeString,String endTimeString){
        this.dateString = dateString;
        this.startTimeString = startTimeString;
        this.endTimeString = endTimeString;
    }
    @Override
    public String toString(){
        return dateString + " : " + startTimeString + " | " + endTimeString;
    }
}
