package Client;

import il.technion.ewolf.http.HttpConnectorModule;
import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.openkad.KadNetModule;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import Mailbox.Algorithm.BucketManager;
import Mailbox.Algorithm.MailboxState;
import Mailbox.Messages.PollReplayMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;
import Parser.Event;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ClientTester {

	public ClientTester()
	{

	}
	public void PollManagerTester()
	{
		PollManager pm = new PollManager();
		BucketManager bm = new BucketManager();
		TopicRequest res = pm.getNextTopicRequest(2,bm);
		Assert.assertNotNull(res.getMyTopic());
		Assert.assertTrue(res.getMyTopic().size() >=0);
		Assert.assertTrue(res.getMyTopic().contains(2));
		Assert.assertTrue(res.publicationNumber ==0);

		PollReplayMessage prm = new PollReplayMessage();
		PublicationMessage pubm = new PublicationMessage();
		pubm.content = "Hello";
		pubm.topics = new ArrayList<Integer>();
		pubm.topics.add(2);

		List<PublicationMessage> pubml = new ArrayList<PublicationMessage>();
		pubml.add(pubm);

		prm.Content.put(res.topicID, pubml);
		//bm.rawTopics.add(2);
		pm.StorePollReplayInformation(null,prm, bm,null);

		TopicRequest res2 = pm.getNextTopicRequest(2,bm);
		Assert.assertTrue(res2.topicID == res.topicID);
		System.out.println(res.publicationNumber+" "+res2.publicationNumber);
		Assert.assertTrue(res2.publicationNumber== 1);

		List<PublicationMessage> list = bm.getPublications(2);
		//Assert.assertTrue(res2.gossip != null);

	}
	public void SmallClientTest() throws Exception
	{
		//		final int Nodes = 10;
		//		Properties[] props = new Properties[Nodes];
		//		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
		//		ChordData[] data = new ChordData[Nodes];
		//		Node[] node = new Node[Nodes];
		//		Mailbox[] Mailboxes = new Mailbox[Nodes];
		//		ClientApplication[] Clients = new ClientApplication[Nodes]; 
		//		// set kademlia TCP and udp ports and protocols
		//		Integer port =7870;
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
		//			Clients[i] = new ClientApplication(Mailboxes[i],"");
		//			Clients[i].registerEvents();
		//
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
		//			Clients[i].SubscribeTopic(1);
		//			Thread.sleep(5000);
		//		}
		//
		//		Clients[0].PublishTopic(1, "Hello All!");
		//		Thread.sleep(5000);
		//		for(int i=0; i<Nodes; i++)
		//		{
		//			Clients[i].myPollManager.PollRound();
		//			Thread.sleep(5000);
		//		}
		//
		//		int nummailbox = 0;
		//		for(int i1 =0; i1<Nodes;i1++)
		//		{
		//			if( (Mailboxes[i1]).getState() !=  MailboxState.Inactive)
		//			{
		//				System.out.println("Mailbox: " + Mailboxes[i1].myData.chordID+ "Has: "+Mailboxes[i1].buckets.rawTopics.size()+ " Topics.");
		//				nummailbox++;
		//			}
		//		}
		//		for(int i1 =0; i1<Nodes;i1++)
		//		{
		//			Clients[i1].PrintClientState();
		//		}


	}

	public void PollTest() throws Exception
	{
		//		final int Nodes = 20;
		//		Properties[] props = new Properties[Nodes];
		//		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
		//		ChordData[] data = new ChordData[Nodes];
		//		Node[] node = new Node[Nodes];
		//		Mailbox[] Mailboxes = new Mailbox[Nodes];
		//		ClientApplication[] Clients = new ClientApplication[Nodes]; 
		//
		//		// set kademlia TCP and udp ports and protocols
		//		Integer port =7777;
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
		//			Mailboxes[i] = new Mailbox(data[i],kbr[i],null);
		//			Clients[i] = new ClientApplication(Mailboxes[i],"");
		//			Clients[i].registerEvents();
		//
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
		//
		//
		//
		//		Clients[0].SubscribeTopic(1);
		//		System.out.println("Client: 0 subscribe: 1");
		//		Thread.sleep(10000);
		//		Clients[0].PublishTopic(1, "somthing");
		//		// see that I get the stuf. 
		//
		//		Clients[0].myPollManager.PollRound();
		//		Clients[0].PublishTopic(1, "somthing");
		//		// see that I get the stuf. 
		//



	}
	public static Map<Integer,Integer> stats = new HashMap<Integer,Integer>();
	@Test
	public void BigClientTestWithPublications() throws Exception
	{
		final int Nodes = 20;
		Properties[] props = new Properties[Nodes];
		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];

		ClientApplication[] Clients = new ClientApplication[Nodes]; 
		// set kademlia TCP and udp ports and protocols
		Integer port =8970;


		for(Integer i =0; i<Nodes; i++)
		{

			props[i] = new Properties();
			props[i].setProperty("openkad.net.udp.port", port.toString() );
			props[i].setProperty("httpconnector.net.port", port.toString() );
			props[i].setProperty("openkad.bucketsize", "4");
			props[i].setProperty("openkad.keyfactory.keysize", "6");
			props[i].setProperty("rs.server.nrthreads", "1");
			props[i].setProperty("openkad.net.concurrency", "3");
			props[i].setProperty("openkad.net.concurrency", "4");
			props[i].setProperty("gil.param", i.toString());
			props[i].setProperty("gil.client.name", i.toString());
			props[i].setProperty("openkad.executors.client.max_pending", "1024");
			props[i].setProperty("openkad.executors.server.max_pending","2048");
			props[i].setProperty("openkad.refresh.interval", "600000000");

			port++;

			Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
			kbr[i] = injector.getInstance(KeybasedRouting.class);
			kbr[i].create();




			Clients[i] = injector.getInstance(ClientApplication.class);
			Clients[i].registerEvents();


		}
		port-=Nodes;
		Random r = new Random();
		//Thread.sleep(10000);

		for(int i =1; i<Nodes; ++i)
		{
			try
			{

				int myPort = port + r.nextInt(i);

				ArrayList<URI> bootstrap = new ArrayList<URI>();
				bootstrap.add(new URI("udp://127.0.0.1:"+myPort+"/"));
				kbr[i].join(bootstrap);
				System.out.println("Join: "+i);
			}
			catch(Exception e)
			{
				i--;
				continue;
			}


		}
		Integer topic;

		topic = r.nextInt(4500);
		Clients[0].SubscribeTopic(topic);
		System.out.println("Client: "+ 0 + " subscribe: "+ topic);

		Thread.sleep(5000);

		for(int k =0;k<2;k++)
		{
			for(int i=0; i<Nodes; i++)
			{
				for(int j=0; j<10; j++)
				{

					topic = r.nextInt(4500);
					Clients[i].SubscribeTopic(topic);
					System.out.println("Client: "+ i + " subscribe: "+ topic);

				}
				Thread.sleep(1000);
			}
		}
		System.gc();
		Thread.sleep(60000);

		//Now Every Client say hello.
		int misses =0;
		int total =0;
		for(int i =0; i<Nodes;i++)
		{

			for (Integer t : Clients[i].debugSubscribedTopics) {

				//writepublishtostats(stats, t);
				Clients[i].PublishTopic(t, "Hello from Client: "+ i +" To topic: "+t );
				Thread.sleep(100);
			}


		}
		System.err.println("Finished publishing!!!!");
		System.err.println("***********************");
		System.err.println("***********************");
		System.err.println("***********************");



		System.err.println("Waiting 33 min");
		Thread.sleep(60*1000*33);

		//printMailboxes(Clients.);

		for(int i1 =0; i1<Nodes;i1++)
		{
			for (Integer tt : Clients[i1].debugSubscribedTopics) 
			{
				misses +=(stats.get(tt)-Clients[i1].GetNumberOfPublications(tt));
				total += stats.get(tt);
			}
		}

		System.out.println("out of total: " + total +" publications : "+ misses +"publications missed");


	}

	public static synchronized void writepublishtostats(Integer t) {
		if(stats.get(t)== null)
		{
			stats.put(t, 1);
		}
		else
		{
			Integer res = stats.get(t);
			stats.remove(t);
			res +=1;
			stats.put(t, res);
		}
	}





	@Test
	public void FewClientsOneMailbox() throws Exception
	{
		try
		{
			final int Nodes = 500;
			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			ClientTester.stats = new HashMap<Integer,Integer>();
			ClientApplication[] Clients = new ClientApplication[Nodes]; 
			ClientApplication[] Clients2 = new ClientApplication[Nodes]; 
			ClientApplication[] Clients3 = new ClientApplication[Nodes]; 
			// set kademlia TCP and udp ports and protocols
			Integer port =5970;


			for(Integer i =0; i<Nodes; i++)
			{

				props[i] = new Properties();
				props[i].setProperty("openkad.net.udp.port", port.toString() );
				props[i].setProperty("httpconnector.net.port", port.toString() );

				props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
				props[i].setProperty("openkad.keyfactory.keysize", "10");
				props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.forward.nrthreads", "1");
				props[i].setProperty("rs.server.nrthreads", "1");
				props[i].setProperty("openkad.net.concurrency", "3");

				props[i].setProperty("gil.param", i.toString());
				props[i].setProperty("gil.client.name", i.toString());
				props[i].setProperty("openkad.executors.client.max_pending", "512");
				props[i].setProperty("openkad.executors.server.max_pending","512");
				props[i].setProperty("openkad.refresh.interval", "600000000");
				props[i].setProperty("gil.cache.size", "10");
				props[i].setProperty("gil.client.filter.size", "150");
				port++;

				Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
				kbr[i] = injector.getInstance(KeybasedRouting.class);
				kbr[i].create();




				Clients[i] = injector.getInstance(ClientApplication.class);
				Clients[i].registerEvents();
				Clients2[i]= injector.getInstance(ClientApplication.class);
				Clients2[i].myname +="b";
				Clients2[i].registerEvents();
				Clients3[i]= injector.getInstance(ClientApplication.class);
				Clients3[i].myname +="c";
				Clients3[i].registerEvents();

			}
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);
			Integer topic;

			topic = r.nextInt(4500);
			Clients[0].SubscribeTopic(topic);
			System.out.println("Client: "+ 0 + " subscribe: "+ topic);

			Thread.sleep(5000);




			for(int i=0; i<Nodes; i++)
			{


				for(int k=0;k<10;k++)
				{
					topic = r.nextInt(4500);
					Clients[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients2[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients2[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients3[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients3[i].myname + " subscribe: "+ topic);
				}
				Thread.sleep(5100);
			}




			System.gc();
			Thread.sleep(60000);

			//Now Every Client say hello.

			publishonce(Nodes, Clients, Clients2, Clients3);
			System.gc();
			Thread.sleep(60000);

			System.err.println("Finished publishing!!!!");
			System.err.println("***********************");
			System.err.println("***********************");
			System.err.println("***********************");

			for(int i=0; i<Nodes;i++)
			{
				Clients[i].active.set(true);
				Clients2[i].active.set(true);
				Clients3[i].active.set(true);
				Clients[i].homeNode.debugIsOperating = true;
			}
			Collection<ClientApplication> col = new ArrayList<ClientApplication>();
			printMailboxes(col);
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(10));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 32 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("30 min");

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("28 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("24 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));

			System.err.println("22 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("20 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("18 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("16 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("14 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("12 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("8 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("6 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("4 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("2 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			System.err.println("0 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	@Test
	public void FewClientsOneMailboxBig() throws Exception
	{
		try
		{
			final int Nodes = 1500;
			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			ClientTester.stats = new HashMap<Integer,Integer>();
			ClientApplication[] Clients = new ClientApplication[Nodes]; 
			ClientApplication[] Clients2 = new ClientApplication[Nodes]; 
			ClientApplication[] Clients3 = new ClientApplication[Nodes]; 
			// set kademlia TCP and udp ports and protocols
			Integer port =5970;


			for(Integer i =0; i<Nodes; i++)
			{

				props[i] = new Properties();
				props[i].setProperty("openkad.net.udp.port", port.toString() );
				props[i].setProperty("httpconnector.net.port", port.toString() );

				props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
				props[i].setProperty("openkad.keyfactory.keysize", "10");
				props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.forward.nrthreads", "1");
				props[i].setProperty("rs.server.nrthreads", "1");
				props[i].setProperty("openkad.net.concurrency", "3");

				props[i].setProperty("gil.param", i.toString());
				props[i].setProperty("gil.client.name", i.toString());
				props[i].setProperty("openkad.executors.client.max_pending", "512");
				props[i].setProperty("openkad.executors.server.max_pending","512");
				props[i].setProperty("openkad.refresh.interval", "600000000");
				props[i].setProperty("gil.cache.size", "10");
				props[i].setProperty("gil.client.filter.size", "150");
				port++;

				Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
				kbr[i] = injector.getInstance(KeybasedRouting.class);
				kbr[i].create();




				Clients[i] = injector.getInstance(ClientApplication.class);
				Clients[i].registerEvents();
				Clients2[i]= injector.getInstance(ClientApplication.class);
				Clients2[i].myname +="b";
				Clients2[i].registerEvents();
				Clients3[i]= injector.getInstance(ClientApplication.class);
				Clients3[i].myname +="c";
				Clients3[i].registerEvents();

			}
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);
			Integer topic;

			topic = r.nextInt(4500);
			Clients[0].SubscribeTopic(topic);
			System.out.println("Client: "+ 0 + " subscribe: "+ topic);

			Thread.sleep(5000);




			for(int i=0; i<Nodes; i++)
			{


				for(int k=0;k<3;k++)
				{
					topic = r.nextInt(4500);
					Clients[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients2[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients2[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients3[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients3[i].myname + " subscribe: "+ topic);
				}
				Thread.sleep(5100);
			}




			System.gc();
			Thread.sleep(60000);

			//Now Every Client say hello.

			publishonce(Nodes, Clients, Clients2, Clients3);
			System.gc();
			Thread.sleep(60000);

			System.err.println("Finished publishing!!!!");
			System.err.println("***********************");
			System.err.println("***********************");
			System.err.println("***********************");

			for(int i=0; i<Nodes;i++)
			{
				Clients[i].active.set(true);
				Clients2[i].active.set(true);
				Clients3[i].active.set(true);
				Clients[i].homeNode.debugIsOperating = true;
			}



			Collection<ClientApplication> col = new ArrayList<ClientApplication>();
			printMailboxes(col);
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(10));
			PrintMailboxesSyntheticTest(Clients, Clients2, Clients3);


			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 32 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("30 min");

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("28 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("24 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));

			System.err.println("22 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("20 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("18 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("16 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("14 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("12 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("8 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("6 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("4 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("2 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			System.err.println("0 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@Test
	public void FewClientsOneMailbox3000Big() throws Exception
	{
		try
		{
			final int Nodes = 2000;
			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			ClientTester.stats = new HashMap<Integer,Integer>();
			ClientApplication[] Clients = new ClientApplication[Nodes]; 
			ClientApplication[] Clients2 = new ClientApplication[Nodes]; 
			ClientApplication[] Clients3 = new ClientApplication[Nodes]; 
			// set kademlia TCP and udp ports and protocols
			Integer port =7000;


			for(Integer i =0; i<Nodes; i++)
			{

				props[i] = new Properties();
				props[i].setProperty("openkad.net.udp.port", port.toString() );
				props[i].setProperty("httpconnector.net.port", port.toString() );

				props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
				props[i].setProperty("openkad.keyfactory.keysize", "10");
				props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.forward.nrthreads", "1");
				props[i].setProperty("rs.server.nrthreads", "1");
				props[i].setProperty("openkad.net.concurrency", "3");

				props[i].setProperty("gil.param", i.toString());
				props[i].setProperty("gil.client.name", i.toString());
				props[i].setProperty("openkad.executors.client.max_pending", "512");
				props[i].setProperty("openkad.executors.server.max_pending","512");
				props[i].setProperty("openkad.refresh.interval", "600000000");
				props[i].setProperty("gil.cache.size", "100");
				props[i].setProperty("gil.client.filter.size", "150");
				port++;

				Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
				kbr[i] = injector.getInstance(KeybasedRouting.class);
				kbr[i].create();




				Clients[i] = injector.getInstance(ClientApplication.class);
				Clients[i].registerEvents();
				Clients2[i]= injector.getInstance(ClientApplication.class);
				Clients2[i].myname +="b";
				Clients2[i].registerEvents();
				//				Clients3[i]= injector.getInstance(ClientApplication.class);
				//				Clients3[i].myname +="c";
				//				Clients3[i].registerEvents();

			}
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);
			Integer topic;

			topic = r.nextInt(4500);
			Clients[0].SubscribeTopic(topic);
			System.out.println("Client: "+ 0 + " subscribe: "+ topic);

			Thread.sleep(5000);




			for(int i=0; i<Nodes; i++)
			{


				for(int k=0;k<3;k++)
				{
					topic = r.nextInt(4500);
					Clients[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients2[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients2[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					//					Clients3[i].SubscribeTopic(topic);
					//					System.out.println("Client: "+ Clients3[i].myname + " subscribe: "+ topic);
				}
				Thread.sleep(2000);
			}




			System.gc();
			Thread.sleep(60000);

			//Now Every Client say hello.

			for(int i =0; i<Nodes;i++)
			{

				for (Integer t : Clients[i].debugSubscribedTopics) {
					writepublishtostats(t);
					Clients[i].PublishTopic(t, "Hello from Client: "+ Clients[i].myname +" To topic: "+t );
					Thread.sleep(100);
				}
				for (Integer t : Clients2[i].debugSubscribedTopics) {
					writepublishtostats( t);
					Clients2[i].PublishTopic(t, "Hello from Client: "+ Clients2[i].myname +" To topic: "+t );
					Thread.sleep(100);
				}
				//				for (Integer t : Clients3[i].debugSubscribedTopics) {
				//
				//					writepublishtostats(t);
				//					Clients3[i].PublishTopic(t, "Hello from Client: "+ Clients3[i].myname +" To topic: "+t );
				//					Thread.sleep(100);
				//				}


			}
			System.gc();
			Thread.sleep(60000);

			System.err.println("Finished publishing!!!!");
			System.err.println("***********************");
			System.err.println("***********************");
			System.err.println("***********************");

			for(int i=0; i<Nodes;i++)
			{
				Clients[i].active.set(true);
				Clients2[i].active.set(true);
				//Clients3[i].active.set(true);
				Clients[i].homeNode.debugIsOperating = true;
			}



			Collection<ClientApplication> col = new ArrayList<ClientApplication>();
			printMailboxes(col);
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(10));
			PrintMailboxesSyntheticTest(Clients, Clients2, Clients3);


			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 32 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("30 min");

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("28 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("24 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));

			System.err.println("22 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("20 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("18 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("16 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("14 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("12 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("8 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("6 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("4 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("2 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			System.err.println("0 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	@Test
	public void FewClientsOneMailbox1000() throws Exception
	{
		try
		{
			final int Nodes = 1500;
			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			ClientTester.stats = new HashMap<Integer,Integer>();
			ClientApplication[] Clients = new ClientApplication[Nodes]; 
			ClientApplication[] Clients2 = new ClientApplication[Nodes]; 
			ClientApplication[] Clients3 = new ClientApplication[Nodes]; 
			// set kademlia TCP and udp ports and protocols
			Integer port =5970;


			for(Integer i =0; i<Nodes; i++)
			{

				props[i] = new Properties();
				props[i].setProperty("openkad.net.udp.port", port.toString() );
				props[i].setProperty("httpconnector.net.port", port.toString() );

				props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
				props[i].setProperty("openkad.keyfactory.keysize", "10");
				props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.forward.nrthreads", "1");
				props[i].setProperty("rs.server.nrthreads", "1");
				props[i].setProperty("openkad.net.concurrency", "3");

				props[i].setProperty("gil.param", i.toString());
				props[i].setProperty("gil.client.name", i.toString());
				props[i].setProperty("openkad.executors.client.max_pending", "512");
				props[i].setProperty("openkad.executors.server.max_pending","512");
				props[i].setProperty("openkad.refresh.interval", "600000000");
				props[i].setProperty("gil.cache.size", "10");
				props[i].setProperty("gil.client.filter.size", "150");
				port++;

				Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
				kbr[i] = injector.getInstance(KeybasedRouting.class);
				kbr[i].create();




				Clients[i] = injector.getInstance(ClientApplication.class);
				Clients[i].registerEvents();
				Clients2[i]= injector.getInstance(ClientApplication.class);
				Clients2[i].myname +="b";
				Clients2[i].registerEvents();
				Clients3[i]= injector.getInstance(ClientApplication.class);
				Clients3[i].myname +="c";
				Clients3[i].registerEvents();

			}
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);
			Integer topic;

			topic = r.nextInt(4500);
			Clients[0].SubscribeTopic(topic);
			System.out.println("Client: "+ 0 + " subscribe: "+ topic);

			Thread.sleep(5000);




			for(int i=0; i<Nodes; i++)
			{


				for(int k=0;k<3;k++)
				{
					topic = r.nextInt(4500);
					Clients[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients2[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients2[i].myname + " subscribe: "+ topic);
					topic = r.nextInt(4500);

					Clients3[i].SubscribeTopic(topic);
					System.out.println("Client: "+ Clients3[i].myname + " subscribe: "+ topic);
				}
				Thread.sleep(5100);
			}




			System.gc();
			Thread.sleep(60000);

			//Now Every Client say hello.

			publishonce(Nodes, Clients, Clients2, Clients3);
			System.gc();
			Thread.sleep(60000);

			System.err.println("Finished publishing!!!!");
			System.err.println("***********************");
			System.err.println("***********************");
			System.err.println("***********************");

			for(int i=0; i<Nodes;i++)
			{
				Clients[i].active.set(true);
				Clients2[i].active.set(true);
				Clients3[i].active.set(true);
				Clients[i].homeNode.debugIsOperating = true;
			}



			Collection<ClientApplication> col = new ArrayList<ClientApplication>();
			printMailboxes(col);
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(10));
			PrintMailboxesSyntheticTest(Clients, Clients2, Clients3);


			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 32 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("30 min");

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("28 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("24 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));

			System.err.println("22 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("20 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("18 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("16 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("14 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			System.err.println("12 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("10 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("8 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("6 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("4 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("2 min");
			Thread.sleep(TimeUnit.MINUTES.toMillis(2));
			System.err.println("0 min");
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@Test
	public void ChurnTest() throws Exception
	{
		try
		{
			final int Nodes = 100;
			final int minutesToWait =50;
			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			ClientTester.stats = new HashMap<Integer,Integer>();
			ClientApplication[] Clients = new ClientApplication[Nodes]; 
			ClientApplication[] Clients2 = new ClientApplication[Nodes]; 
			ClientApplication[] Clients3 = new ClientApplication[Nodes]; 
			// set kademlia TCP and udp ports and protocols
			Integer port =5970;


			port = initTest(Nodes, props, kbr, Clients, Clients2, Clients3,
					port);
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);
			Integer topic;

			topic = r.nextInt(4500);
			Clients[0].SubscribeTopic(topic);
			System.out.println("Client: "+ 0 + " subscribe: "+ topic);

			Thread.sleep(5000);
			subscribeToTopics(Nodes, Clients, Clients2, Clients3, r);
			System.gc();
			Thread.sleep(60000);

			//Now Every Client say hello.

			publishonce(Nodes, Clients, Clients2, Clients3);
			System.gc();
			Thread.sleep(60000);

			System.err.println("Finished publishing!!!!");
			System.err.println("***********************");
			System.err.println("***********************");
			System.err.println("***********************");

			for(int i=0; i<Nodes;i++)
			{
				Clients[i].active.set(true);
				Clients2[i].active.set(true);
				Clients3[i].active.set(true);
				Clients[i].homeNode.debugIsOperating = true;
			}



			Collection<ClientApplication> col = new ArrayList<ClientApplication>();
			printMailboxes(col);
			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 5 min grace");
			Thread.sleep(TimeUnit.MINUTES.toMillis(5));
			PrintMailboxesSyntheticTest(Clients, Clients2, Clients3);


			printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
			System.err.println("Waiting 32 min");
			for(int i=minutesToWait/2;i>0;i-=2)
			{
				Thread.sleep(TimeUnit.MINUTES.toMillis(2));
				System.err.println("Minutes Left: "+i);
				printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);

			}
			Random rand = new Random();
			for(int reps = 0; reps<1000; reps++)
			{

				System.out.println("Crushing 10% of mailboxes/clients: ");

				for(int i=0; i<Nodes;i++)
				{
					//if(rand.nextInt(10)==0){
						// mailbox is lost. 
						Clients[i].homeNode.isDown.set(true);
						//assuming these clients are also new now. 
						Clients[i].myPollManager.forget();
						Clients2[i].myPollManager.forget();
						Clients3[i].myPollManager.forget();
						
					//}
					
				}



				for(int i =0; i<Nodes;i++)
				{

					for (Integer t : Clients[i].debugSubscribedTopics) {

						writepublishtostats(t);
						Clients[i].PublishTopic(t, reps+"Hello from Client: "+ Clients[i].myname +" To topic: "+t );
						Thread.sleep(100);
					}
					for (Integer t : Clients2[i].debugSubscribedTopics) {

						writepublishtostats( t);
						Clients2[i].PublishTopic(t, reps+"2Hello from Client: "+ Clients2[i].myname +" To topic: "+t );
						Thread.sleep(100);
					}
					for (Integer t : Clients3[i].debugSubscribedTopics) {

						writepublishtostats(t);
						Clients3[i].PublishTopic(t, reps+"2Hello from Client: "+ Clients3[i].myname +" To topic: "+t );
						Thread.sleep(100);
					}
				}

				;

				System.err.println("Finished publishing 2nd time!!!!");
				System.err.println("***********************");
				System.err.println("***********************");
				System.err.println("***********************");

				System.err.println("Waiting 32 min");
				for(int i=minutesToWait;i>0;i-=2)
				{
					System.err.println("Minutes Left: "+i);
					Thread.sleep(TimeUnit.MINUTES.toMillis(2));
					printStats(Nodes, stats, Clients, Clients2, Clients3, 0, 0);
				}
				PrintMailboxesSyntheticTest(Clients, Clients2, Clients3);
			}



		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void publishonce(final int Nodes, ClientApplication[] Clients,
			ClientApplication[] Clients2, ClientApplication[] Clients3)
			throws InterruptedException {
		for(int i =0; i<Nodes;i++)
		{

			for (Integer t : Clients[i].debugSubscribedTopics) {

				writepublishtostats(t);
				Clients[i].PublishTopic(t, "Hello from Client: "+ Clients[i].myname +" To topic: "+t );
				Thread.sleep(100);
			}
			for (Integer t : Clients2[i].debugSubscribedTopics) {

				writepublishtostats( t);
				Clients2[i].PublishTopic(t, "Hello from Client: "+ Clients2[i].myname +" To topic: "+t );
				Thread.sleep(100);
			}
			for (Integer t : Clients3[i].debugSubscribedTopics) {

				writepublishtostats(t);
				Clients3[i].PublishTopic(t, "Hello from Client: "+ Clients3[i].myname +" To topic: "+t );
				Thread.sleep(100);
			}


		}
	}
	private void subscribeToTopics(final int Nodes,
			ClientApplication[] Clients, ClientApplication[] Clients2,
			ClientApplication[] Clients3, Random r) throws Exception,
			InterruptedException {
		Integer topic;
		for(int i=0; i<Nodes; i++)
		{


			for(int k=0;k<20;k++)
			{
				topic = r.nextInt(4500);
				Clients[i].SubscribeTopic(topic);
				System.out.println("Client: "+ Clients[i].myname + " subscribe: "+ topic+" "+k);
				topic = r.nextInt(4500);

				Clients2[i].SubscribeTopic(topic);
				System.out.println("Client: "+ Clients2[i].myname + " subscribe: "+ topic+" "+k);
				topic = r.nextInt(4500);

				Clients3[i].SubscribeTopic(topic);
				System.out.println("Client: "+ Clients3[i].myname + " subscribe: "+ topic+" "+k);
			}
			Thread.sleep(5100);
		}
	}
	private Integer initTest(final int Nodes, Properties[] props,
			KeybasedRouting[] kbr, ClientApplication[] Clients,
			ClientApplication[] Clients2, ClientApplication[] Clients3,
			Integer port) throws IOException {
		for(Integer i =0; i<Nodes; i++)
		{

			props[i] = new Properties();
			props[i].setProperty("openkad.net.udp.port", port.toString() );
			props[i].setProperty("httpconnector.net.port", port.toString() );

			props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
			props[i].setProperty("openkad.keyfactory.keysize", "10");
			props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
			props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
			props[i].setProperty("openkad.executors.forward.nrthreads", "1");
			props[i].setProperty("rs.server.nrthreads", "1");
			props[i].setProperty("openkad.net.concurrency", "3");

			props[i].setProperty("gil.param", i.toString());
			props[i].setProperty("gil.client.name", i.toString());
			props[i].setProperty("openkad.executors.client.max_pending", "512");
			props[i].setProperty("openkad.executors.server.max_pending","512");
			props[i].setProperty("openkad.refresh.interval", "600000000");
			props[i].setProperty("gil.cache.size", "1500");
			props[i].setProperty("gil.client.filter.size", "150");
			port++;

			Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
			kbr[i] = injector.getInstance(KeybasedRouting.class);
			kbr[i].create();




			Clients[i] = injector.getInstance(ClientApplication.class);
			Clients[i].registerEvents();
			Clients2[i]= injector.getInstance(ClientApplication.class);
			Clients2[i].myname +="b";
			Clients2[i].registerEvents();
			Clients3[i]= injector.getInstance(ClientApplication.class);
			Clients3[i].myname +="c";
			Clients3[i].registerEvents();

		}
		return port;
	}
	private void JoinNetwork(final int Nodes, KeybasedRouting[] kbr,
			Integer port, Random r) throws URISyntaxException {
		for(int i =1; i<Nodes; ++i)
		{

			int myPort = port + r.nextInt(i);

			ArrayList<URI> bootstrap = new ArrayList<URI>();
			bootstrap.add(new URI("udp://127.0.0.1:"+myPort+"/"));
			kbr[i].join(bootstrap);
			System.out.println("Join: "+i);

		}
	}

	public static void main(String [] args)
	{
		try {
			ClientTester ct = new ClientTester();
			ct.TweeterTestRegularDay();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Test
	public void TweeterTestRegularDay() throws Exception
	{
		try
		{
			final int Nodes = 300;
			Double FromDay =7.4;
			Double ToDay = 7.45;
			int MinutesToWaitAfterSubscribe = 4;
			Integer port =5970;


			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			stats = new ConcurrentHashMap<Integer,Integer>();
			ArrayList<ClientApplication> clients = new ArrayList<ClientApplication>();

			// set Kademlia TCP and udp ports and protocols

			Map<Integer, List<Event>> allEventsInExperiment = Parser.CentralizedParser.parseDirectory("/home/gilga/Dropbox/Dropbox/Workspace/ConformityParser/event/1", FromDay, ToDay);
			System.out.println("There are : " + allEventsInExperiment.size() +" subscriptions");

			Map<Integer, List<Integer>> allReigstrations = Parser.CentralizedParser.parseRegistrations("/home/gilga/Dropbox/Dropbox/Workspace/ConformityParser/follower/1", allEventsInExperiment.keySet());
			System.out.println("There are : " + allReigstrations.size() + "subscribers");

			for(Integer i =0; i<Nodes; i++)
			{

				props[i] = new Properties();
				props[i].setProperty("openkad.net.udp.port", port.toString() );
				props[i].setProperty("httpconnector.net.port", port.toString() );

				props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
				props[i].setProperty("openkad.keyfactory.keysize", "10");
				props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.forward.nrthreads", "1");
				props[i].setProperty("rs.server.nrthreads", "1");
				props[i].setProperty("openkad.net.concurrency", "3");

				props[i].setProperty("gil.param", i.toString());
				props[i].setProperty("gil.client.name", i.toString());
				props[i].setProperty("openkad.executors.client.max_pending", "512");
				props[i].setProperty("openkad.executors.server.max_pending","512");
				props[i].setProperty("openkad.refresh.interval", "600000000");
				props[i].setProperty("gil.cache.size", "1500");
				props[i].setProperty("gil.client.filter.size", "150");
				port++;

				Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
				kbr[i] = injector.getInstance(KeybasedRouting.class);
				kbr[i].create();
				List<Integer> clientIds = new LinkedList<Integer>(allReigstrations.keySet());

				for(int j =0; j<((allReigstrations.size()/400)+1) ; j++)
				{
					if(clientIds.isEmpty())
						break;
					int id = clientIds.remove(0);
					//initialize the client;


					ClientApplication application = injector.getInstance(ClientApplication.class);
					application.setFrom(FromDay).setMyId(id).setMyEvents(allEventsInExperiment.remove(id))
					.setMysubscriptions(allReigstrations.remove(id));
					application.myname += "_"+id;
					application.registerEvents();
					clients.add(application);
				}



			}
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);


			boolean needToContinue =true;
			int iter =0;
			while(needToContinue)
			{

				for(int i = 0; i<clients.size();i++)
				{
					needToContinue = false;
					boolean check = false;
					if(clients.get(i).getMysubscriptions().size() >0)
					{
						System.out.println(clients.get(i).myname + " Has " + clients.get(i).getMysubscriptions().size()+ " subscriptions to do");
						for(int j =0; j<15;j++)
							clients.get(i).nextSubscribe();
						check = !clients.get(i).getMysubscriptions().isEmpty();
						if(check)
							needToContinue = true;
						if(iter<10)
							Thread.sleep(5000);
						else
							Thread.sleep(600);
					}
				}

				iter++;
			}
			for (ClientApplication clientApplication : clients) {
				clientApplication.homeNode.debugIsOperating  = true;
				clientApplication.active.set(true);
			}

			System.out.println("Finished Subscribing: ");
			System.gc();

			// wait for network to stabalize after minutes to wait. 
			for(int i =0; i<MinutesToWaitAfterSubscribe; i++)
			{
				System.gc();
				System.out.println("Sleeping: "+ (MinutesToWaitAfterSubscribe - i) + "More minutes");
				Thread.sleep(1000*60);
			}


			// send start signal to all clients. 
			for (ClientApplication clientApplication : clients) {
				clientApplication.StartEventManager();
			}

			Double timeofExperiment = (ToDay*24 -FromDay*24);
			System.out.println("Experiment should take: "+ timeofExperiment +"hours");
			double timepassed =0;


			// wait for experiment to end; 
			for( timepassed =0; timepassed<timeofExperiment+(0.3);timepassed+= (0.1))
			{
				int misses = 0;
				int total =0;
				printStats(clients, misses, total);

				
				System.out.println("****** Time Passed: "+ timepassed + "hours: experiment length: "+ timeofExperiment + "hours");
				Thread.sleep(10*1000*60);

			}

//			// collecting the rest of the publications.
//			for (ClientApplication clientApplication : clients) {
//				clientApplication.active.set(true);
//				clientApplication.myPollManager.PollRound();
//			}


			System.err.println("Waiting 10 minutes");
			Thread.sleep(1000*60*10);

			printStats(clients, 0, 0);
			printMailboxes(clients);

			//TODO:  collect statistics after experiment;
			int misses = 0;
			int total =0;
			printStats(clients, misses, total);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void TweeterWithMailboxChurn() throws Exception
	{
		try
		{
			final int Nodes = 300;
			Double FromDay =9.4;
			Double ToDay = 9.45;
			int MinutesToWaitAfterSubscribe = 25;
			Integer port =5970;


			Properties[] props = new Properties[Nodes];
			KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
			stats = new ConcurrentHashMap<Integer,Integer>();
			ArrayList<ClientApplication> clients = new ArrayList<ClientApplication>();

			// set Kademlia TCP and udp ports and protocols

			Map<Integer, List<Event>> allEventsInExperiment = Parser.CentralizedParser.parseDirectory("/home/gilga/Dropbox/Dropbox/Workspace/ConformityParser/event/1", FromDay, ToDay);
			System.out.println("There are : " + allEventsInExperiment.size() +" subscriptions");

			Map<Integer, List<Integer>> allReigstrations = Parser.CentralizedParser.parseRegistrations("/home/gilga/Dropbox/Dropbox/Workspace/ConformityParser/follower/1", allEventsInExperiment.keySet());
			System.out.println("There are : " + allReigstrations.size() + "subscribers");

			for(Integer i =0; i<Nodes; i++)
			{

				props[i] = new Properties();
				props[i].setProperty("openkad.net.udp.port", port.toString() );
				props[i].setProperty("httpconnector.net.port", port.toString() );

				props[i].setProperty("openkad.bucket.kbuckets.maxsize", "10");
				props[i].setProperty("openkad.keyfactory.keysize", "10");
				props[i].setProperty("openkad.executors.server.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.client.nrthreads", "1"); 
				props[i].setProperty("openkad.executors.forward.nrthreads", "1");
				props[i].setProperty("rs.server.nrthreads", "1");
				props[i].setProperty("openkad.net.concurrency", "3");

				props[i].setProperty("gil.param", i.toString());
				props[i].setProperty("gil.client.name", i.toString());
				props[i].setProperty("openkad.executors.client.max_pending", "512");
				props[i].setProperty("openkad.executors.server.max_pending","512");
				props[i].setProperty("openkad.refresh.interval", "600000000");
				props[i].setProperty("gil.cache.size", "1500");
				props[i].setProperty("gil.client.filter.size", "150");
				port++;

				Injector injector = Guice.createInjector(new KadNetModule(props[i]), new HttpConnectorModule(props[i]), new ClientApplicationModule(props[i]));
				kbr[i] = injector.getInstance(KeybasedRouting.class);
				kbr[i].create();
				List<Integer> clientIds = new LinkedList<Integer>(allReigstrations.keySet());

				for(int j =0; j<((allReigstrations.size()/400)+1) ; j++)
				{
					if(clientIds.isEmpty())
						break;
					int id = clientIds.remove(0);
					//initialize the client;


					ClientApplication application = injector.getInstance(ClientApplication.class);
					application.setFrom(FromDay).setMyId(id).setMyEvents(allEventsInExperiment.remove(id))
					.setMysubscriptions(allReigstrations.remove(id));
					application.myname += "_"+id;
					application.registerEvents();
					clients.add(application);
				}



			}
			port-=Nodes;
			Random r = new Random();
			//Thread.sleep(10000);

			JoinNetwork(Nodes, kbr, port, r);

			System.gc();
			Thread.sleep(10000);


			boolean needToContinue =true;
			int iter =0;
			while(needToContinue)
			{

				for(int i = 0; i<clients.size();i++)
				{
					needToContinue = false;
					boolean check = false;
					if(clients.get(i).getMysubscriptions().size() >0)
					{
						System.out.println(clients.get(i).myname + " Has " + clients.get(i).getMysubscriptions().size()+ " subscriptions to do");
						for(int j =0; j<15;j++)
							clients.get(i).nextSubscribe();
						check = !clients.get(i).getMysubscriptions().isEmpty();
						if(check)
							needToContinue = true;
						if(iter<10)
							Thread.sleep(5000);
						else
							Thread.sleep(600);
					}
				}

				iter++;
			}
			for (ClientApplication clientApplication : clients) {
				clientApplication.homeNode.debugIsOperating  = true;
				clientApplication.active.set(true);
			}

			System.out.println("Finished Subscribing: ");
			System.gc();

			// wait for network to stabalize after minutes to wait. 
			for(int i =0; i<MinutesToWaitAfterSubscribe; i++)
			{
				System.gc();
				System.out.println("Sleeping: "+ (MinutesToWaitAfterSubscribe - i) + "More minutes");
				Thread.sleep(1000*60);
			}
			Random rnd = new Random();

			// send start signal to all clients. 
			for (ClientApplication clientApplication : clients) {
				clientApplication.StartEventManager();
			}

			Double timeofExperiment = (ToDay*24 -FromDay*24);
			System.out.println("Experiment should take: "+ timeofExperiment +"hours");
			int victim = rnd.nextInt(clients.size());
			// kill the victim. 
			clients.get(victim).homeNode.isDown.set(true);

			double timepassed =0;


			// wait for experiment to end; 
			for( timepassed =0; timepassed<timeofExperiment+(0.3);timepassed+= (0.1))
			{
				int misses = 0;
				int total =0;
				printStats(clients, misses, total);

				printMailboxes(clients);

				System.out.println("****** Time Passed: "+ timepassed + "hours: experiment length: "+ timeofExperiment + "hours");
				Thread.sleep(10*1000*60);

			}

			// collecting the rest of the publications.
			for (ClientApplication clientApplication : clients) {
				clientApplication.active.set(true);
				clientApplication.myPollManager.PollRound();
			}


			System.err.println("Waiting 10 minutes");
			Thread.sleep(1000*60*10);

			printStats(clients, 0, 0);
			printMailboxes(clients);

			//TODO:  collect statistics after experiment;
			int misses = 0;
			int total =0;
			printStats(clients, misses, total);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void printStats(Collection<ClientApplication> clients, int misses,
			int total) {
		int badClients=0;

		for (ClientApplication clientApplication : clients) {
			misses+= clientApplication.getMisses();
			total += clientApplication.getTotal(); 

			if(clientApplication.getMisses()> 0)
				badClients++;

		}
		FileWriter outfile;
		try {
			outfile = new FileWriter("res_102.txt",true);
			outfile.write("out of total: " + total +" publications : "+ misses +" publications missed\n");
			outfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("out of total: " + total +" publications : "+ misses +" publications missed\n");
		System.out.println("out of total: " + clients.size() +" cleints : "+ badClients +" missed publications\n");
	}


	private void printStats(final int Nodes, Map<Integer, Integer> stats,
			ClientApplication[] Clients, ClientApplication[] Clients2,
			ClientApplication[] Clients3, int misses, int total) {


		for(int i1 =0; i1<Nodes;i1++)
		{
			for (Integer tt : Clients[i1].debugSubscribedTopics) 
			{
				misses +=(stats.get(tt)-Clients[i1].GetNumberOfPublications(tt));
				total += stats.get(tt);
			}
			for (Integer tt : Clients2[i1].debugSubscribedTopics) 
			{
				misses +=(stats.get(tt)-Clients2[i1].GetNumberOfPublications(tt));
				total += stats.get(tt);
			}
			for (Integer tt : Clients3[i1].debugSubscribedTopics) 
			{
				misses +=(stats.get(tt)-Clients3[i1].GetNumberOfPublications(tt));
				total += stats.get(tt);
			}
		}


		FileWriter outfile;
		try {
			outfile = new FileWriter("stats.txt",true);
			outfile.write("out of total: " + total +" publications : "+ misses +" publications missed\n");
			outfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("out of total: " + total +" publications : "+ misses +"publications missed");
	}
	private void PrintMailboxesSyntheticTest(ClientApplication[] Clients,
			ClientApplication[] Clients2, ClientApplication[] Clients3) {
		List<ClientApplication> list = Arrays.asList(Clients);
		printMailboxes(list);
		list = Arrays.asList(Clients2);
		printMailboxes(list);
		list = Arrays.asList(Clients3);
		printMailboxes(list);
		list = null;
	}
	private void printMailboxes( Collection<ClientApplication> Clients) {
		int nummailbox = 0;
		List<Integer> alreadyChecked = new ArrayList<Integer>();
		List<Integer> alreadyChecked2 = new ArrayList<Integer>();
		FileWriter MailboxNumberOfTopics;
		try {
			MailboxNumberOfTopics = new FileWriter("res_Mailboxes.txt",true);
			MailboxNumberOfTopics.write("**********************************************\n");

			FileWriter ClientsNumberOfTopics = new FileWriter("res_Clients.txt",true);
			ClientsNumberOfTopics.write("**********************************************\n");

			FileWriter MailboxStats = new FileWriter("StatsMailboxes.txt",true);
			MailboxStats.write("**********************************************\n");
			FileWriter AllStats = new FileWriter("StatsAll.txt",true);
			AllStats.write("**********************************************\n");




			for (ClientApplication Capp : Clients) {
				ClientsNumberOfTopics.write(Capp.PrintClientState());

				if(!alreadyChecked2.contains(Capp.homeNode.myData.chordID)&&!Capp.homeNode.isDown.get())
				{
					alreadyChecked2.add(Capp.homeNode.myData.chordID);
					AllStats.write(Capp.homeNode.myMonitor.getStatusLine());
				}


				if(Capp.homeNode.getState() != MailboxState.Inactive)
				{

					if(alreadyChecked.contains(Capp.homeNode.myData.chordID))
					{

						continue;
					}
					alreadyChecked.add(Capp.homeNode.myData.chordID);
					MailboxStats.write(Capp.homeNode.myMonitor.getStatusLine());
					MailboxNumberOfTopics.write(Capp.homeNode.myData.chordID + " " + Capp.homeNode.getNumberOfTopics() + "\n");

					nummailbox++;
				}

			}
			MailboxNumberOfTopics.close();
			ClientsNumberOfTopics.close();
			MailboxStats.close();
			AllStats.close();
			System.out.println("There are: "+ nummailbox + "Mailboxes");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}

