package Mailbox.SubscribeCache;

import il.technion.ewolf.kbr.Node;
import Tools.MyBloomFilter;

public class CacheLine {
	private MyBloomFilter Filter;
	private String ClientName;
	private Node ClientNode;
	private Long ExpirationData;
	public MyBloomFilter getFilter()
	{
		return Filter;
	}
	public CacheLine setFilter(MyBloomFilter filter) {
		Filter = filter;
		return this;
	}
	
	public CacheLine setClientName(String clientName) {
		ClientName = clientName;
		return this;
	}
	public String getClientName() {
		return ClientName;
	}
	public CacheLine setClientNode(Node clientNode) {
		ClientNode = clientNode;
		return this;
	}
	public Node getClientNode() {
		return ClientNode;
	}
	public CacheLine setExpirationData(Long expirationData) {
		ExpirationData = expirationData;
		return this;
	}
	public Long getExpirationData() {
		return ExpirationData;
	}
	
}
