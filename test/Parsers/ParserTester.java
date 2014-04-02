package Parsers;


public class ParserTester {
	/*
	@Test
	public void EventLogParserTest()
	{
		EventLogParser elp = new EventLogParser(new DebugEventLogHandler());
		elp.Parse("C:/Users/gilga/Documents/���/Trace/1902_event");
	}
	@Test
	public void InputFromFileTest() throws Exception
	{
		Properties[] props = new Properties[20];
		KeybasedRouting[] kbr = new KeybasedRouting[20];
		ChordData[] data = new ChordData[20];
		Node[] node = new Node[20];
		//ConformityNodeWithDAS[] Mailboxes = new ConformityNodeWithDAS[20];
		// set kademlia TCP and udp ports and protocols
		Integer port =8830;
		
		for(int i =0; i<7; i++)
		{
			port++;
			props[i] = new Properties();
			props[i].setProperty("openkad.net.otcpkad.port", port.toString() );
			props[i].setProperty("kadnet.oudpkad.port", port.toString() );

			Injector injector = Guice.createInjector(new KadNetModule(props[i]));
			kbr[i] = injector.getInstance(KeybasedRouting.class);
			kbr[i].create();

			data[i] = new ChordData(i+1, kbr[i].getLocalNode());
			node[i] = kbr[i].getLocalNode();
			//Mailboxes[i] = new ConformityNodeWithDAS(data[i], kbr[i]);

		}

		for(int i =0; i<7; i++)
		{
			if(1!= 0)
			{
				kbr[i].join(new URI("otcpkad://127.0.0.1:8831/")).get();
			}

		}
		NetworkMock.Instance = new NetworkMock();
		NetworkMock.Instance.bootStrap = data[0];
		// only some of the nodes are on the ring. 
		for(int i=0; i<7; i++)
		{
			if(i <= 3)
			{
				Mailboxes[i].topologyGossip(data[1], 1);
				Thread.sleep(3000);
				//Mailboxes[0].NotifyTopologyChange(data[i], 1, ChangeType.Join);	
				Thread.sleep(4000);
			}
		}
		for(int i1 =1; i1<7;i1++)
		{

			PollMessage p = new PollMessage();
			TopicRequest tr = 	new TopicRequest();
			tr.bucketID = -1;
			tr.replayID = "go";
			tr.requestedID = 0;
			tr.topicID = new ArrayList<Integer>();
			tr.topicID.add(1);
			p.requestedTopics.add(tr);
			PollReplayMessage prm = Mailboxes[i1].handlePoll(p);


		}
		BasicUnitTestEventLogHandler handler = new BasicUnitTestEventLogHandler();
		handler.myNode = Mailboxes[0];
		
		EventLogParser elp = new EventLogParser(handler);
		elp.Parse("C:/Users/gilga/Documents/���/Trace/1902_event");

		for(int i1 =1; i1<7;i1++)
		{

			PollMessage p = new PollMessage();
			TopicRequest tr = 	new TopicRequest();
			tr.bucketID = -1;
			tr.replayID = "go";
			tr.requestedID = 0;
			tr.topicID = new ArrayList<Integer>();
			tr.topicID.add(1);
			p.requestedTopics.add(tr);
			PollReplayMessage prm = Mailboxes[i1].handlePoll(p);
			try{
				System.out.println((prm.Content.get(0).get(0).content));
			}
			catch(Exception e)
			{
				System.err.println("Id: "+ (i1 +1)+" Did not get any!");
			}
			
			


		}
		for(int i =0; i<7; i++)
		{
			//kbr[i].shutdown();
		}
	}
	*/
}
