package Mailbox.SubscribeCache;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;
import static org.mockito.Mockito.*;
import org.junit.Test;

import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TemporarySubscribeMessage;
import Tools.MyBloomFilter;

public class SubscribeCacheTester {
	@Test
	public void matchingSubscribeShouldbeSent()
	{
		try {
			KeybasedRouting kbr = mock(KeybasedRouting.class);
			Node node = mock(Node.class);
			MyBloomFilter mbf = new MyBloomFilter(40);
			mbf.add(1);
			mbf.add(2);
			SubscriptionCache sc = new LazyUpdateSubscriptionCache(10, kbr);

			TemporarySubscribeMessage tsm = new TemporarySubscribeMessage();
			tsm.setClientFilter(mbf)
			.setClientName("alice")
			.setClientNode(node);

			sc.TemporarySubscribe(tsm);
			
			PublicationMessage pm = new PublicationMessage();
			pm.content = "testing testing 123";
			pm.topics = new ArrayList<Integer>();
			pm.topics.add(1);
			pm.topics.add(2);

			sc.DistributePublish(pm);
			
			//verify that the subscription cache registered the correct event. (treat it as documentation.) 
			verify(kbr).register(eq(Configuration.Configuration.SUBSCRIBE_TAG), (MessageHandler) isNotNull());
			//the first time the publication matches our node... so we want the kbr to send the message. 
			verify(kbr,times(1)).sendMessage(eq(node), eq(Configuration.Configuration.SUBSCRIBE_RESULT_TAG+"alice"),  (Serializable) isNotNull());
			pm.topics.add(3);
			
			// this time the publication don't match our node... so we want the kbr not to send the message. 
			// (this is where the times(1) comes into play!
			sc.DistributePublish(pm);
			verify(kbr,times(1)).sendMessage(eq(node),anyString(),  (byte[]) isNotNull());
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
