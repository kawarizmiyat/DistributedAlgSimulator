package com.simulator; 

public class EventQueue {
		EventNode first = null;
		EventNode last = null;
		int size = 0;
		
		public void enter(Event e){
			size++;
			EventNode node = new EventNode(e);
			if ( first == null ){
				first = node; last = first;
			}
			else{last.next = node; last = node;}
		}
		
		public boolean isEmpty(){return(first == null); }
		public int length() {return(size);}

		public Event getNext(){
			size--;
			Event temp = first.event;
			first = first.next;
			return(temp);
		}

	}

