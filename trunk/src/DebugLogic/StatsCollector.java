package DebugLogic;

import java.util.concurrent.atomic.AtomicInteger;

public class StatsCollector {
	public AtomicInteger nrMessageshandled;
	public AtomicInteger nrSubscribes;
	public AtomicInteger nrPolls;
	public AtomicInteger nrHints;
	public AtomicInteger nrPLS;
	public AtomicInteger nrPublish;
	public AtomicInteger nrTopologyGossip;
	public StatsCollector()
	{
		this.nrHints = new AtomicInteger(0);
		this.nrSubscribes = new AtomicInteger(0);
		this.nrMessageshandled = new AtomicInteger(0);
		this.nrPolls = new AtomicInteger(0);
		this.nrPLS = new AtomicInteger(0);
		this.nrPublish = new AtomicInteger(0);
		this.nrTopologyGossip = new AtomicInteger(0);
		
	}
	public String getStatusLine() {
		return "HandledMessages: "+ nrMessageshandled.intValue() + " Subscriptions: "+ nrSubscribes.intValue() + " polls: "+nrPolls.intValue() 
				+ " Hints: "+ nrHints.intValue()+ " nrPLS: "
				+ nrPLS.intValue()
				+ " nrPublish: "
				+nrPublish.intValue() 
				+ " nrToplogyGossip: "
				+ nrTopologyGossip.intValue() +"\n";
	}
	
	
}
