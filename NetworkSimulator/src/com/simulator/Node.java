 package com.simulator; 

import java.io.PrintStream;
import java.util.ArrayList;

public abstract class Node {
	
	public SimSystem sim; 
	public int id; 
	public ArrayList<Integer> neighbors;
	
	// Variables needed to any Event handler in this simulation
	protected double now;
	protected PrintStream log = System.out; 
	private boolean D = true;
	
	public String status; 

	protected static final String MSG_INIT = "init";
	
	public Node(SimSystem sim, int id) { 
		this.sim = sim; 
		this.id = id; 
		neighbors = new ArrayList<Integer>();
	}
	
	protected abstract void handleEvent(Event e);
	protected abstract void initProtocol() ;
	protected abstract void handleReceivedMessage(Message message);
	
	protected void sendMessage(Message msg) { 
		
		if (D) { 
			System.out.printf("%d sends message to %d \n", 
					msg.senderId, msg.receiverId);
		}
		
		Event e = new Event(); 
		e.action = EventType.MESSAGE; 
		e.message = msg; 
		e.time = now + msgDelay();
		
		sim.future.enter(e);
	}
	
	protected double msgDelay() { 
		return 1.0;
	}
	
	public void addNeighbor(int i) {
		neighbors.add(i);
	}

}
