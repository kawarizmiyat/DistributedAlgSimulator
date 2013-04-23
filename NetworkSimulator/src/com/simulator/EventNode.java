 package com.simulator; 

public class EventNode {
	public Event event;
	public EventNode next;
	
	public EventNode(Event e){
		event = e;
		next = null;
	}

}
