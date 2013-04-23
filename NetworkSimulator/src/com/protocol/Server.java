package com.protocol; 
 
import com.simulator.*;
import com.analysis.*;

public class Server extends EventHandler {
	private EventQueue queue;
	private Server nextServer;
	private FutureEventList future;
	private TimePeriodExp ran;
	private boolean free = true;
	private double now;
	public CalculateAverage averageTotalDelay; 
	
	public Server(FutureEventList events, double avServiceTime, Server next){
		future = events;
		double rate = 1 / avServiceTime;
		ran = new TimePeriodExp(rate);
		System.out.println("Server Rate: " + rate);
		queue = new EventQueue();
		averageTotalDelay = new CalculateAverage();
	}
	
	// the server handles events with the following actions:
	//   NEW_REQ comes from outside; service will be performed immediately if server is free; 
	//      otherwise it will be placed in the EventQueue of the server (to be processed later)
	//      Note: whenever the service performance will be started, an event SERVE_END will be 
	//            scheduled after the expected service time
	//   SERVE_END was scheduled when a service started. When this event occurs the server is 
	//      free and looks in its EventQueue to see whether other requests are waiting. If so, 
	//      the next service request is started and an SERVE_END is scheduled.
	
	// Note that the Event attribute totalDelay is used to carry the time 
	//    when the user request was made, and it is used (when the request has been served) 
	//    to calculate the total service time, which is then stored to calculate the average
	//    of these delays when the simulation terminates.
	protected void handleEvent(Event e){
		System.out.println("Server: action " + e.action + " ID " + e.data + " time " + e.time);

		now = e.time;
		if (e.action == Event.NEW_REQ){ // a new request has arrived: if free, schedule the service start
			e.totalDelay = now;
			if(free){
				e.action = Event.SERVE_END;
				e.time = now + ran.delay();
				future.enter(e);
				free = false;
			} else {queue.enter(e);}
			
		} else if (e.action == Event.SERVE_END){ // end of servicing period. 
			// Forward the unit of work to the next server and check whether another request is waiting
			e.totalDelay = now - e.totalDelay;
			averageTotalDelay.newCase(e.totalDelay);
			free = true;
			if(nextServer != null) {e.target = nextServer; future.enter(e);}
			if(!queue.isEmpty()) {
				Event waitingEvent = queue.getNext();
				waitingEvent.action = Event.SERVE_END;
				waitingEvent.time = now + ran.delay();
				future.enter(waitingEvent);
				free = false;
			} 
		}
	}

	public boolean isFree(){return(free);}
	public int queueSize() {return(queue.length());}
}

