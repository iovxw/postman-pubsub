package Mailbox.Messages;

import java.io.Serializable;

import Tools.MyBloomFilter;

import il.technion.ewolf.kbr.Node;

public class TemporarySubscribeMessage implements Serializable{
	
	private static final long serialVersionUID = -3689004118985564554L;
	private String ClientName;
	private Node ClientNode;
	private MyBloomFilter ClientFilter;
	
	public TemporarySubscribeMessage setClientName(String clientName) {
		ClientName = clientName;
		return this;
	}
	public String getClientName() {
		return ClientName;
	}
	public TemporarySubscribeMessage setClientNode(Node clientNode) {
		ClientNode = clientNode;
		return this;
	}
	public Node getClientNode() {
		return ClientNode;
	}
	public TemporarySubscribeMessage setClientFilter(MyBloomFilter clientFilter) {
		ClientFilter = clientFilter;
		return this;
	}
	public MyBloomFilter getClientFilter() {
		return ClientFilter;
	}

}
