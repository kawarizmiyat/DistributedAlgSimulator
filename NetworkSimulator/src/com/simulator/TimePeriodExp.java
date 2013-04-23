package com.simulator; 

import java.util.Random;

public class TimePeriodExp {
	private double rate;
	Random r;
	
	public TimePeriodExp( double i ){
		rate = i;
		r = new Random();
	}
	
	public double delay(){
		//exponential		
		return -(1/rate)*Math.log(1-r.nextDouble());
		//uniform
//		return 2/rate*Math.random();
		//deterministic
//		return 1/rate;
	}
}

