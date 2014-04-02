package ProbobalisticLookup.Algorithm;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.Node;
import il.technion.ewolf.kbr.openkad.KadNetModule;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;

import org.junit.Test;

import ProbobalisticLookup.Messages.HintMessage;
import ProbobalisticLookup.Messages.LookupMessage;
import Tools.MyBloomFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ProbabalisticLookupTester {

	@Test
	public void bloomFilterTest() throws Exception
	{
		MyBloomFilter b = new MyBloomFilter(25); 
		b.add(1);
		//System.out.println(b.toString());
		b.add(6);
		//System.out.println(b.toString());
		if(!b.contains(6))
		{
			throw new Exception("Bloom Failed!");
		}
		if(!b.contains(1))
		{
			throw new Exception("Bloom Failed!");
		}
		// assume the bloom filter should be large enough to distinguish.
		if(b.contains(8))
		{
			throw new Exception("Bloom Failed!");
		}
		MyBloomFilter b2 = new MyBloomFilter(25); 
		b2.add(8);
		//System.out.println(b2.toString());
		b.Merge(b2);
		if(!b.contains(8))
		{
			throw new Exception("Bloom Failed!");
		}
	}



	@Test
	public void BasicPlsTest() throws Exception
	{
//		final int Nodes =50;
//		Properties[] props = new Properties[Nodes];
//		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
//		//ChordData[] data = new ChordData[Nodes];
//		Node[] node = new Node[Nodes];
//		ProbobalisticLookupNode[] Rnode = new ProbobalisticLookupNode[Nodes];
//		// set kademlia TCP and udp ports and protocols
//		Integer port =8400;
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
//
//			Injector injector = Guice.createInjector(new KadNetModule(props[i]));
//			kbr[i] = injector.getInstance(KeybasedRouting.class);
//			kbr[i].create();
//
//
//			node[i] = kbr[i].getLocalNode();
//			Rnode[i] = new ProbobalisticLookupNode(kbr[i]);
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
//
//		}
//
//		// join all nodes: 
//
//
//		HintMessage message = new HintMessage();
//		message.owner = Rnode[0].myKbr.getLocalNode();
//		message.predicate = new MyBloomFilter(20);
//		message.predicate.add(1);
//		message.TTL = 6;
//		Rnode[0].handleHintMessage(Rnode[0].myKbr.getLocalNode(), message);
//
//
//		Thread.sleep(20000);		
//
//		for(int i =0; i<Nodes;i++)
//		{
//			if(Rnode[i].hints.size() == 0)
//			{
//				throw new Exception("Somthing went wrong!" + i);
//			}
//			if(Rnode[i].hints.size() > 1)
//			{
//				throw new Exception("to many: "+ Rnode[i].hints.size() );
//			}
//		}
	}

	@Test
	public void LookupTest() throws Exception
	{
//		final int Nodes =450;
//		Properties[] props = new Properties[Nodes];
//		KeybasedRouting[] kbr = new KeybasedRouting[Nodes];
//		//ChordData[] data = new ChordData[Nodes];
//		Node[] node = new Node[Nodes];
//		ProbobalisticLookupNode[] Rnode = new ProbobalisticLookupNode[Nodes];
//		// set kademlia TCP and udp ports and protocols
//		Integer port =9500;
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
//			Rnode[i] = new ProbobalisticLookupNode(kbr[i]);
//
//		}
//		port-=Nodes;
//		Random r = new Random();
//		//Thread.sleep(10000);
//		for(int i =1; i<Nodes; ++i)
//		{
//
//			int myPort = port + r.nextInt(i);
//
//			ArrayList<URI> bootstrap = new ArrayList<URI>();
//			bootstrap.add(new URI("udp://127.0.0.1:"+myPort+"/"));
//			kbr[i].join(bootstrap);
//
//
//
//		}
//
//		// join all nodes: 
//
//		int chosen = r.nextInt(450);
//		HintMessage message = new HintMessage();
//		message.owner = Rnode[chosen].myKbr.getLocalNode();
//		message.predicate = new MyBloomFilter(20);
//		message.predicate.add(1);
//		message.TTL = 2;
//
//		Rnode[chosen].debugTarget = true;
//		Rnode[chosen].handleHintMessage(Rnode[chosen].myKbr.getLocalNode(), message);
//
//
//		Thread.sleep(10000);		
//
//
//
//
//
//		for(int i =0; i<450; i++)
//		{
//			LookupMessage lookup = new LookupMessage();
//			lookup.alreadyVisited = new HashSet<Node>();
//			lookup.Content = null;
//			lookup.topic =1;
//			lookup.TTL = 8;
//			Rnode[i].handleLookupMessage(Rnode[30].myKbr.getLocalNode(), lookup );
//		}
//
//
//		Thread.sleep(15000);
//		System.out.println("Successfully discovered:  "+ Rnode[chosen].debugIndicator);
//		//Rnode[chosen].debugIndicator = 0;



	}
}
