package com.roveapps.hScheduler;

import java.util.Date;

public class Timing{
    public Date startDate;

    public Date endDate;

    public Timing(Date startDate, Date endDate) {

    	
        this.startDate = startDate;
        this.endDate = endDate;
        
 

    }

    public String toString(){

        return Subroutines.dateFormatter.format(startDate) + " " + Subroutines.dateFormatter.format(endDate);
    }
}