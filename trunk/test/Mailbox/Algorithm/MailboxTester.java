package Mailbox.Algorithm;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.Node;
import il.technion.ewolf.kbr.openkad.KadNetModule;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import Mailbox.Messages.PollMessage;
import Mailbox.Messages.PublicationMessage;
import Multicast.Algorithm.ChordData;
import Multicast.Messages.BasicStringContainer;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MailboxTester {

	/*
	 * Creating network on the mailbox level. this is high level interface that is actually publish/subscribe.
	 */
	@Test
	public void PublicationMessageTest()throws Exception
	{
		PublicationMessage p = new PublicationMessage();
		p.content = "Hello";
		p.topics = new ArrayList<Integer>();
		p.topics.add(1);

		PublicationMessage pp = new PublicationMessage();
		pp.content = "Hello";
		pp.topics = new ArrayList<Integer>();
		pp.topics.add(1);
		pp.topics.add(2);

		if(!p.equals(pp))
			throw new Exception("Publication: Equals fails");
		if(p.compareTo(pp) != 0)
			throw new Exception("Publication: Compere fails");


		PublicationMessage ppp = new PublicationMessage();
		ppp.content = "HelloWorld";
		ppp.topics = new ArrayList<Integer>();
		ppp.topics.add(1);
		ppp.topics.add(2);

		if(p.equals(ppp))
			throw new Exception("Publication: Equals fails");
		if(p.compareTo(ppp) == 0)
			throw new Exception("Publication: Compere fails");

	}


	@Test
	public void BucketTest() throws Exception
	{
		// question 1: why does bucket need to know index.
		// question 2: why topic an int. 
		Bucket b = new Bucket(1);
		PublicationMessage p = new PublicationMessage();
		p.content = "Hello";
		p.topics = new ArrayList<Integer>();
		p.topics.add(1);

		// make sure the publication matches.
		boolean res = b.AddIfMatch(p);
		if(!res)
			throw new Exception("Bucket Failed to match publication");

		List<PublicationMessage> l = b.GetPublicationMessages(0);
		if(!l.contains(p))
			throw new Exception("Buckets contnet incorrect");
		res = b.AddIfMatch(p);
		if(res)
			throw new Exception("Bucket Failed to identify duplicate");

		p = new PublicationMessage();
		p.topics = new ArrayList<Integer>();
		p.topics.add(2);
		p.topics.add(1);
		p.content = "Somthing";
		res = b.AddIfMatch(p);
		if(!res)
			throw new Exception ("Bucket failed to be updated by a complex topic");
		l = b.GetPublicationMessages(0);
		if(!l.contains(p))
			throw new Exception("Buckets contnet incorrect");



		p = new PublicationMessage();
		p.content = "So";
		p.topics = new ArrayList<Integer>();
		p.topics.add(1);
		res = b.AddIfMatch(p);
		if(!res)
			throw new Exception ("Bucket wrongly identified a publication as duplicate");

		l = b.GetPublicationMessages(0);
		if(!l.contains(p))
			throw new Exception("Buckets contnet incorrect");
	}
	@Test
	public void PollTest() throws Exception
	{
//		int Nodes = 10;
//		Properties[] props = new Properties[Nodes];
//		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
//		ChordData[] data = new ChordData[Nodes];
//		Node[] node = new Node[Nodes];
//		Mailbox[] Mailboxes = new Mailbox[Nodes];
//		// set kademlia TCP and udp ports and protocols
//		Integer port =8830;
//
//
//		for(int i =0; i<Nodes; i++)
//		{
//
//			props[i] = new Properties();
//			props[i].setProperty("openkad.net.udp.port", port.toString() );
//			props[i].setProperty("openkad.net.http.port", port.toString() );
//			props[i].setProperty("openkad.bucketsize", "2");
//			props[i].setProperty("openkad.keyfactory.keysize", "3");
//			props[i].setProperty("rs.server.nrthreads", "1");
//			props[i].setProperty("openkad.net.concurrency", "3");
//			props[i].setProperty("openkad.net.concurrency", "4");
//			port++;
//
//			Injector injector = Guice.createInjector(new KadNetModule(props[i]));
//			kbr[i] = injector.getInstance(KeybasedRouting.class);
//			kbr[i].create();
//
//
//			node[i] = kbr[i].getLocalNode();
//			data[i] = new ChordData(i,node[i]);
//			Mailboxes[i] = new Mailbox(data[i],kbr[i]);
//
//		}
//		port-=Nodes;
//		Random r = new Random();
//		for(int i =1; i<Nodes; ++i)
//		{
//
//			int myPort = port + r.nextInt(i);
//			ArrayList<URI> bootstrap = new ArrayList<URI>();
//			bootstrap.add(new URI("udp://127.0.0.1:"+myPort+"/"));
//			kbr[i].join(bootstrap);	
//		}
//		//PollMessage m = new PollMessage();
//
//		//Mailboxes[0].handlePoll(null);


	}	@Test
	public void BasicMailboxTest() throws Exception
	{
		//		final int Nodes = 10;
		//		Properties[] props = new Properties[Nodes];
		//		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
		//		ChordData[] data = new ChordData[Nodes];
		//		Node[] node = new Node[Nodes];
		//		Mailbox[] Mailboxes = new Mailbox[Nodes];
		//		// set kademlia TCP and udp ports and protocols
		//		Integer port =8870;
		//		
		//		
		//		for(int i =0; i<Nodes; i++)
		//		{
		//			
		//			props[i] = new Properties();
		//			props[i].setProperty("openkad.net.udp.port", port.toString() );
		//			props[i].setProperty("openkad.net.http.port", port.toString() );
		//			props[i].setProperty("openkad.bucketsize", "2");
		//			props[i].setProperty("openkad.keyfactory.keysize", "3");
		//			props[i].setProperty("rs.server.nrthreads", "1");
		//			props[i].setProperty("openkad.net.concurrency", "3");
		//			props[i].setProperty("openkad.net.concurrency", "4");
		//			port++;
		//			
		//			Injector injector = Guice.createInjector(new KadNetModule(props[i]));
		//			kbr[i] = injector.getInstance(KeybasedRouting.class);
		//			kbr[i].create();
		//
		//			
		//			node[i] = kbr[i].getLocalNode();
		//			data[i] = new ChordData(i,node[i]);
		//			Mailboxes[i] = new Mailbox(data[i],kbr[i]);
		//			Mailboxes[i].RegisterEvents();
		//			//Mailboxes[i].activateContentGossip();
		//			Mailboxes[i].Activate(1);
		//
		//		}
		//		port-=Nodes;
		//		Random r = new Random();
		//		//Thread.sleep(10000);
		//		for(int i =1; i<Nodes; ++i)
		//		{
		//			
		//			int myPort = port + r.nextInt(i);
		//			ArrayList<URI> bootstrap = new ArrayList<URI>();
		//			bootstrap.add(new URI("udp://127.0.0.1:"+myPort+"/"));
		//			kbr[i].join(bootstrap);
		//			
		//				
		//		}
		//		for(int i=0; i<Nodes; i++)
		//		{
		//			Mailboxes[i].Subscribe(1,Mailboxes[i].buckets, new ArrayList<PublicationMessage>());
		//		}
		//		Thread.sleep(2000);
		//		
		//		PublicationMessage m = new PublicationMessage();
		//		
		//		// node 0 will eventually gossip with all nodes.
		//		for(int i=0; i<Nodes; i++)
		//		{
		//			
		//		}
		//		
		//		
		//		m.content = "Hello World";
		//		m.topics = new ArrayList<Integer>();
		//		m.topics.add(1);
		//		
		//		
		//		
		//		Mailboxes[0].Publish(m, 1);
		//		Thread.sleep(60000);
		//		System.out.println("Table Size: "+ Mailboxes[0].perTopicFingerTable.get(1).Table.size());
		//		Mailboxes[3].TopicMulticast(1, new BasicStringContainer("Hello!"));
		//		m.content = "Bybbye!";
		//		Mailboxes[0].Publish(m, 1);
		//		Thread.sleep(4000);
		//		int miss =0;
		//		for(int i1 =1; i1<Nodes;i1++)
		//		{
		//			if((Mailboxes[i1]).virgin)
		//			{
		//				miss++;
		//				//System.out.println("not everyone got message: " +i1);
		//				//throw new Exception("not everyone got message: " +i1);
		//			}
		//		}
		//		// it should be 0, but actually can't gurantee it. - so I use the <2.
		//		Assert.assertTrue(miss<2);
		//		
	}
	//
	//	


}
