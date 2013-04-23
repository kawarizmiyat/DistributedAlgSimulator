package com.protocol;

import java.io.PrintStream;
import java.util.ArrayList;


import com.simulator.Event;
import com.simulator.EventHandler;
import com.simulator.EventQueue;
import com.simulator.EventType;
import com.simulator.FutureEventList;
import com.simulator.Message;


public class ExchangeNode extends EventHandler {


	// Variables needed to any Event handler in this simulation
	private EventQueue queue;
	private FutureEventList future;
	private double now;
	private PrintStream a = System.out; 

	
	public String status; 
	public ArrayList<Integer> neighbors; 
	
	
	public ExchangeNode(FutureEventList events) { 
		future = events; 
		queue = new EventQueue();
		neighbors = new ArrayList<Integer>();
	}
	
	public void setNeighbors(ArrayList<Integer> neighbors) { 
		this.neighbors = neighbors; 
	}
	
	protected void handleEvent(Event e) { 
		
		System.out.println("Server: action " 
				+ e.action + " ID " + e.data + " time " + e.time);
		
		// update time:
		now = e.time;

		switch (e.action) {
		
		case EventType.INIT: 
			initProtocol(); 
			break; 
			
		case EventType.MESSAGE: 
			handleReceivedMessage(e.message);
			break;
			
			default: 
				a.printf("error: action %d is not recognized \n", e.action);
				System.exit(0);
				
		}
		
		
	}

	private void handleReceivedMessage(Message message) {
		if (status == "Receiver") { 
			handleStatusReceiver(message); 
		} else if (status == "Sender") { 
			handleStatusSender(message);
		} else { 
			System.out.printf("Can not handle msg: %s in status: %s", 
					message, status);
		}
	}

	private void handleStatusSender(Message message) {
		// TODO Auto-generated method stub
		
	}

	private void handleStatusReceiver(Message message) {
		// TODO Auto-generated method stub
		
	}

	private void initProtocol() {
		
		
	}
	
}
