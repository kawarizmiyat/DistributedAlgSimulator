package com.simulator;

import java.io.PrintStream;
import java.util.ArrayList;




public class ExchangeNode extends Node {


	public static final String STAT_SENDER = "sender";
	public static final String STAT_RECEIVER = "receiver";
	public static final String STAT_TERMINATE = "terminate";
	
	public static final boolean D = true;
	
	public static final String MSG_A = "A";
	public static final String MSG_AREPLY = "AREPLY";
	// public static final String MSG_INIT = "INIT";
	
	 


	// Do we need to have a point to simulator system ? 
	// Yes: for the reception and sending of messages ?
	public ExchangeNode(SimSystem sim, int id) {
		super(sim, id);
		this.status = ExchangeNode.STAT_RECEIVER; 
	
	}
		
	public void setNeighbors(ArrayList<Integer> neighbors) { 
		this.neighbors = neighbors; 
	}
	
	protected void handleEvent(Event e) { 
		

		
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
				log.printf("error: action %d is not recognized \n", e.action);
				System.exit(0);
				
		}
		
		
	}

	protected void handleReceivedMessage(Message message) {
		if (status == ExchangeNode.STAT_RECEIVER  ) { 
			handleStatusReceiver(message); 
			
		} else if (status == ExchangeNode.STAT_SENDER ) { 
			handleStatusSender(message);
			
		} else { 
			System.out.printf("Can not handle msg: %s in status: %s", 
					message, status);
		}
	}

	private void handleStatusSender(Message message) {
		
		if (message.msgType == ExchangeNode.MSG_AREPLY) { 
			System.out.printf("%d received msg from %d: %s \n", 
					this.id, message.receiverId, message.msgContent);
			terminateProtocol();
		} 
		
	}

	private void terminateProtocol() {
		
		if (D) {
			System.out.printf("%d terminated the protocol at %f \n",
					this.id, this.now);
		}
		status = ExchangeNode.STAT_TERMINATE; 
		
	}

	private void handleStatusReceiver(Message message) {
		
		if (message.msgType == ExchangeNode.MSG_INIT) { 
			if (D) { 
				System.out.printf("%d is initiator \n", this.id);
			}
			
			initProtocol();
			
		} else if (message.msgType == ExchangeNode.MSG_A) { 
			
			if (D) { 
			System.out.printf("%d received msg A from %d: %s \n", 
					this.id, message.senderId, message.msgContent);
			}
			
			int receiver = message.senderId; 
			String msgType = MSG_AREPLY; 
			String msgContent = "Hellow to you too !";
			Message m = new Message(this.id, receiver, 
					msgType, msgContent);
			sendMessage(m);
			
			terminateProtocol();

		} else { 
			System.out.printf("%d cannot accept msgType: %s at status %s \n",
					this.id, message.msgType, this.status);
			System.exit(0);
		}
		
	}

	protected void initProtocol() {
		
		if (D) { 
			System.out.printf("%d is at init protocol \n", this.id);
		}
		
		this.status = ExchangeNode.STAT_SENDER;
		
		// TODO: needs to be fixed. Neighbors for each node.
		int receiver = neighbors.get(0);
		String msgType = ExchangeNode.MSG_A; 
		String msgContent = "Hellow !";
		
		Message m = new Message(this.id, 
				receiver, msgType, msgContent);
		
		sendMessage(m);
		
	}
	

	

	


}
