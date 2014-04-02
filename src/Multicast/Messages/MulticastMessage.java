package Multicast.Messages;

import java.io.Serializable;


public class MulticastMessage implements Serializable{

	private static final long serialVersionUID = 1L;

	public Integer Topic;
	public Integer maxID;
	public MessageContainer Content;
	
	
}
