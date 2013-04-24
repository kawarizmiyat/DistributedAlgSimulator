package com.simulator; 

public class FutureEventList {
	private EventNode first;
	private int nbEvents;
	
	public FutureEventList(  ){
		first = null;
		nbEvents = 0;
	}
	
	public void enter ( Event e ){
		
		System.out.println("Event entered for " + e.time +  " action " + e.action);
		if ( first == null ){
			first = new EventNode(e);
		}
		else{
			if ( first.event.time > e.time){
				EventNode temp = new EventNode(e);
				temp.next = first;
				first = temp;
			}
			else{
				EventNode temp1 = first;
				while( temp1.next != null ){
					if ( temp1.next.event.time > e.time ){
						break;
					}
					temp1 = temp1.next;
				}
				EventNode temp2 = new EventNode(e);				
				temp2.next = temp1.next;
				temp1.next = temp2;
			}
		}
		nbEvents++;
	}
	
	public Event getNext(){
		EventNode temp = first; 		
		first = first.next;
		nbEvents--;
		return(temp.event);
	}
	
	public int getNbEvents(){
		return nbEvents;
	}
}
