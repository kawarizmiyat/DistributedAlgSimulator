package com.simulator;

public class BroadcastNode extends Node {

	
	private static final boolean D = true;
	
	private static final String STAT_IDLE = "idle";
	private static final String STAT_DONE = "done";
	
	private static final String MSG_BCAST = "bcast";
	
	
	
	
	public BroadcastNode(SimSystem sim, int id) {
		super(sim, id);
		this.status = BroadcastNode.STAT_IDLE;
	}

	
	protected void handleEvent(Event e) { 
		
		// update time:
		now = e.time;

		switch (e.action) {
		
		
		case EventType.MESSAGE: 
			handleReceivedMessage(e.message);
			break;
			
			default: 
				log.printf("error: action %d is not recognized \n", e.action);
				System.exit(0);
				
		}
		
		
	}


	protected void handleReceivedMessage(Message message) {
		
		if (this.id != message.receiverId) { 
			log.printf("Error: received message is not destined to" +
					"the correct destination (%d != %d) \n", 
					this.id, message.receiverId);
		}
		
		if (D) { 
			log.printf("%d received a message from %d at %f \n",
					this.id, message.senderId, now);
		}
		
		if (status == BroadcastNode.STAT_IDLE) { 
			handleStatusIdle(message);
		
		} else if (status == BroadcastNode.STAT_DONE) { 
			handleStatusDone(message);
		
		} else {
				log.printf("Can not handle msg: %s in status: %s", 
						message, status);
		}
		
	}


	



	private void handleStatusIdle(Message message) {
		
		if (message.msgType == BroadcastNode.MSG_INIT) { 
			if (D) { 
				log.printf("%d is initiator \n", this.id);
			}
			
			initProtocol();
			
			
		} else if (message.msgType == BroadcastNode.MSG_BCAST) { 
			
			// TODO Forward: send to all neighbors, except the sender. 
			foreward(message); 

		} else {
			
			log.printf("Error: %d cannot accept msgType: %s at status %s \n",
					this.id, message.msgType, this.status);
			System.exit(0);
		}
		
	}

	
	

	private void foreward(Message message) {
		for (int i = 0; i < neighbors.size(); i++) { 
			if (neighbors.get(i) != message.senderId) { 
				Message m = new Message(this.id, neighbors.get(i),
						BroadcastNode.MSG_BCAST, message.msgContent);
				sendMessage(m);

				
				if (D) { 
					log.printf("%d foreward message to %d \n", 
							this.id, neighbors.get(i));
				}
			}

		}

		status = BroadcastNode.STAT_DONE;
		
	}

	private void handleStatusDone(Message message) {

		if (message.msgType == BroadcastNode.MSG_BCAST) { 
			
			if (D) { 
				log.printf("Do nothing \n");
			}

		} else { 
		
			log.printf("Error: %d cannot accept msgType: %s at status %s \n",
					this.id, message.msgType, this.status);
			System.exit(0);
		}
		
	}


	@Override
	protected void initProtocol() {
		for (int i = 0; i < neighbors.size(); i++) { 

			Message m = new Message(this.id, neighbors.get(i),
					BroadcastNode.MSG_BCAST, "msg from " + this.id);
			sendMessage(m);
			
			if (D) { 
				log.printf("%d foreward message to %d \n", 
						this.id, neighbors.get(i));
			}
			
			status = BroadcastNode.STAT_DONE;
			
		}
		
	}
		
		
}
