package com.simulator;

public class Message {

	public int senderId, receiverId; 
	public String msgType;
	public String msgContent;

	
	
	public String toString() { 
		String str = ""; 
		str += "s: " + senderId + " "; 
		str += "r: " + receiverId + " "; 
		str += "mt: " + msgType + " "; 
		str += "mc: " + msgContent + " ";
		
		return str;
	}
}
