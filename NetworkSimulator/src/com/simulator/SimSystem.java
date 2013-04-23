package com.simulator; 

import com.protocol.Server;
import com.protocol.User;

public class SimSystem extends EventHandler {
	private FutureEventList future;
	
	private Server s;
	private User user;
	
	// for keeping statistics
	final double averageServiceTime = 0.9;
	final double averageRequestRate = 1;
	final double END = 2000; // marks the end of the simulation period
	private double measurementPeriod = 5;

	private final int STAT_LENGTH = 10000;
	private int[] stat_Q_Length = new int[STAT_LENGTH];
	private boolean[] stat_S_Free = new boolean[STAT_LENGTH];
	private int nbStats = 0;
		
	public SimSystem(){
		System.out.println("simulation starts");		
		
		future = new FutureEventList();

		// This simulated system includes one server (which includes a queue for requests)
		// and one "User" generating service requests with Poisson arrival distribution.
		s = new Server(future, averageServiceTime, null);
		user = new User(s, averageRequestRate, future);

		// The following event is introduced to start the activity of the simulation process. 
		// It is the first event to be processed (initially, the only one; 
		//     but others will be produced as this one is being processed)
		Event e1 = new Event();
		e1.time = 0; e1.target = user; e1.action = Event.USER_REQ; e1.data = 1;
		future.enter(e1); 

		// the following event starts the regular status checking for statistics
		Event e2 = new Event();
		e1.time = 0; e2.target = this; e2.action = Event.STAT; e2.data = 0;
		future.enter(e2); 

		// The following is the simulating engine using a virtual time recorded in the events 
		// that are stored in the FutureEventList and other event queues. 
		// The events represent data flow; in this example the only data is an ID. 
		// The information about total delay encountered for a data object (also foreseen) 
		//     has not be programmed.
		while(true){
			if(0 == future.getNbEvents()) {System.out.println("no events - deadlock"); break;}
			Event nextE = future.getNext();
			if(nextE == null) {System.out.println("null pointer"); break;}
			if(nextE.time > END) {System.out.println("END time = " + nextE.time); break;}
			nextE.target.handleEvent(nextE);
			
		}
		// printStat();
	}

	// this procedure looks after status checking for statistics
	protected void handleEvent(Event e){
		// e.action should be STAT
		System.out.println("SimSystem: measurement " + e.action + " ID " + e.data + " time " + e.time);
		stat_Q_Length[e.data] = s.queueSize();
		stat_S_Free[e.data] = s.isFree();
		nbStats++;
		e.data++;
		e.time = e.time + measurementPeriod;
		future.enter(e);

	}

	// print-out of statistics information at the end of the simulation
	private void printStat() {
		int sumQSize = 0;
		int sumFree = 0;
		for(int i=0; i<nbStats; i++){
			sumQSize = sumQSize + stat_Q_Length[i];
			if(stat_S_Free[i]) { sumFree = sumFree + 1;}
		}
		
		Float avQSize = (new Integer(sumQSize)).floatValue(); avQSize = avQSize / nbStats;
		Float avFree = (new Integer(sumFree)).floatValue(); avFree = avFree  / nbStats;

		
		System.out.println("SimSystem Statistics: nb observations " + nbStats);
		System.out.println("SimSystem Statistics: av QSize " + avQSize + " av free " + avFree);
		System.out.println("SimSystem Statistics Average Service Time: " + s.averageTotalDelay.getAverage());
		
		System.out.println("SimSystem Statistics ALL QLengths: ");
		for(int i = 0; i<nbStats; i++){System.out.print(" " + stat_Q_Length[i]);}
		System.out.println("");
		System.out.println("SimSystem Statistics ALL Free: "); 
		for(int i = 0; i<nbStats; i++)
			{int value = 0; if(stat_S_Free[i]){value = 1;} System.out.print(" " + value);}
		System.out.println("");
	}
}
