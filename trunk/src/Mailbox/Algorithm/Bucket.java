package Mailbox.Algorithm;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import Mailbox.Messages.PublicationMessage;

/*
 * To fit into the bucket manager the bucket is also lock free. 
 * the only mutable part of this class is content and poll rate. 
 */
public class Bucket {
	public final List<Integer> interectingtopics;
	AtomicInteger pollRate;
	//int id;
	public Set<PublicationMessage> content;
	public Bucket(Integer topic)
	{
		
		interectingtopics = new ArrayList<Integer>();
		interectingtopics.add(topic);
		init();
	}

	private void init() {
		pollRate = new AtomicInteger(0);
		this.content = new ConcurrentSkipListSet<PublicationMessage>();
	}
	
	public Bucket(List<Integer> topics) {
		interectingtopics =topics;
		init();
	}
	
	
	//***************** return: false - didn't add duplicate
	public boolean AddIfMatch(PublicationMessage p)
	{
		try{
			
			// first I check if the message is suitable for the bucket. 
			// the cheap check. 
			if(p.topics.containsAll(this.interectingtopics))
			{
				return content.add(p);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// not relevant. 
		return false;
	}

	public List<PublicationMessage> GetPublicationMessages(int Id)
	{
		
		pollRate.getAndIncrement();
		ArrayList<PublicationMessage> arrayList = new ArrayList<PublicationMessage>(content);
		Assert.assertNotNull(arrayList);
		// sublist is not serilizable!
		if(Id >= 0)
			return new ArrayList<PublicationMessage>(arrayList.subList(Id, content.size()));
		else
			return new ArrayList<PublicationMessage>(arrayList);
	}
}
