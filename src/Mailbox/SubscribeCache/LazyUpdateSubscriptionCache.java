package Mailbox.SubscribeCache;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Mailbox.Algorithm.Mailbox;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TemporarySubscribeMessage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class LazyUpdateSubscriptionCache implements SubscriptionCache  {

	private final List<CacheLine> cache;
	private int maxSize;
	KeybasedRouting Kbr;
	Mailbox mb;

	// lets hope GUICE knows what kbr to bring me ;)
	@Inject
	LazyUpdateSubscriptionCache(@Named("gil.cache.size") int size, KeybasedRouting kbr)
	{
		// making a synchronized list so we don't need to explicitly lock it. 
		cache = new ArrayList<CacheLine>();
		maxSize = size;
		Kbr = kbr;
		registerEvents();
		
	}

	private void registerEvents()
	{
		this.Kbr.register(Configuration.Configuration.SUBSCRIBE_TAG, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from, String tag, Serializable content) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onIncomingMessage(Node from, String tag,  Serializable content) {
				try
				{
					
					
					TemporarySubscribeMessage tss = (TemporarySubscribeMessage)content;
					TemporarySubscribe(tss);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public synchronized void DistributePublish(PublicationMessage pm) {

		CacheLine line = null;
		for (Iterator<CacheLine> iter = cache.iterator(); iter.hasNext(); ) {
			line = iter.next();
			if(line== null)
				continue;

			// if the line is expired remove it. 
			if(line.getExpirationData()<System.currentTimeMillis())
			{
				iter.remove();
				continue;
			}

			//otherwise: if it is suitable send: 
			if(line.getFilter().containsAll(pm.topics))
			{
				try {
					Kbr.sendMessage(line.getClientNode(), Configuration.Configuration.SUBSCRIBE_RESULT_TAG+line.getClientName(), pm);	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public synchronized void TemporarySubscribe(TemporarySubscribeMessage tsm) {
		if(maxSize <=  cache.size())
		{
			CacheLine victim = cache.get(0);
			//if the oldest entry is still valid return.
			//if(victim.getExpirationData()>System.currentTimeMillis())
			//	return;
			//otherwise remove the entry before adding new. 
			cache.remove(0);
		}


		CacheLine cl = new CacheLine()
		.setClientName(tsm.getClientName())
		.setClientNode(tsm.getClientNode())
		.setFilter(tsm.getClientFilter())
		.setExpirationData(getExpirationData());
		cache.add(cl);
	}

	private long getExpirationData() {
		return System.currentTimeMillis()+Configuration.Configuration.POLLROUNDFREQUENCY*1000;
	}

}
