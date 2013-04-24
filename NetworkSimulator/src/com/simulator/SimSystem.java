package com.simulator; 

import java.util.ArrayList;
import java.util.HashMap;



public class SimSystem extends EventHandler {
	public FutureEventList future;

	// for keeping statistics
	private final double END = 2000; // marks the end of the simulation period
	private final int numNodes = 2; 
	private ArrayList<ExchangeNode> nodesTable;


	public SimSystem() { 
		setupSimulator(); 
		setupNodes(numNodes);
		run();
	}

	public void setupNodes(int n) {
		nodesTable = new ArrayList<ExchangeNode>();
		for (int i = 0; i < n; i++) { 
			nodesTable.add(new ExchangeNode(this, i));
		}
		
		nodesTable.get(0).addNeighbor(1);
		nodesTable.get(1).addNeighbor(0);
		

		setInitiator(0);
	}

	private void setInitiator(int i) {
		Event e = new Event(); 
		e.time = 0; 
		e.action = EventType.MESSAGE; 
		Message m = new Message(-1, i, ExchangeNode.MSG_INIT, "" );
		e.message = m;
		this.future.enter(e);
	}

	public void setupSimulator() { 
		future = new FutureEventList();
	}

	public void run(){
		System.out.println("simulation starts");		


		while(true){

			if(0 == future.getNbEvents()) { 
				System.out.println("no events - deadlock"); 
				break;
			}

			Event nextE = future.getNext();


			if(nextE == null) {
				System.out.println("null pointer"); 
				break;
			}

			if(nextE.time > END) {
				System.out.println("END time = " + nextE.time); 
				break;
			}

			if (nextE.action == EventType.MESSAGE) { 
				int handlerId = nextE.message.receiverId;
				nodesTable.get(handlerId).handleEvent(nextE);
			}
		}

	}


}
