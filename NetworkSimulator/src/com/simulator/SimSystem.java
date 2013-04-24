package com.simulator; 

import java.util.ArrayList;



public class SimSystem  {
	public FutureEventList future;

	// for keeping statistics
	private final double END = 2000; // marks the end of the simulation period
	private final int numNodes = 4; 
	private ArrayList<BroadcastNode> nodesTable;


	public SimSystem() { 
		setupSimulator(); 
		setupNodes(numNodes);
		run();
	}

	public void setupNodes(int n) {
		nodesTable = new ArrayList<BroadcastNode>();
		for (int i = 0; i < n; i++) { 
			nodesTable.add(new BroadcastNode(this, i));
		}

		setupNeighbors();


		setInitiator(0);
	}

	private void setupNeighbors() {

		// TODO: 
		nodesTable.get(0).addNeighbor(1); 
		nodesTable.get(1).addNeighbor(0); 


		nodesTable.get(0).addNeighbor(2); 
		nodesTable.get(2).addNeighbor(0); 

		nodesTable.get(1).addNeighbor(3); 
		nodesTable.get(3).addNeighbor(1);

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
