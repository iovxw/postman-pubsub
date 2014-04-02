package Configuration;


public class Configuration {
	
	

	
	
	//TAGS:
	public static final String TOPOLOGY_GOSSIP_TAG = "TopologyGossip";
	public static final String POLL_TAG ="PollRequest"; 
	public static final String POLL_RESULT_TAG = "PollResult";
	public static final String MULTICAST_TAG = "Multicast";
	public static final String ADV_MSG_TAG = "AdvertisementMessage";
	public static final String QUASAR_PUB_TAG ="QuasarPub";
	public static final String LOOKUP_RES_TAG = "LookupResult";
	public static final String CLIENT_PUBLISH = "ClientPublish";
	public static final String MAILBOX_NAME_TAG = "bob";
	public static final String TOPICS_GOSSIP_TAG= "TopicGossip";
	public static final String SUBSCRIBE_RESULT_TAG = "subscribeHit";
	public static final String SUBSCRIBE_TAG = "subscribe";
	/// Parameters.
	public static final int ADV_RAD = 2; // radius of advertise around mailbox.
	public static final int PUB_TTL = 8; // TTL of publication message on probabilistic algorithm.
	
	public static final int BLOOM_FILTER_SIZE = 150; 	// size of bloom filter.
	public static final int MULTICAST_FANOUT =2; 	// when using spanning tree algorithm, how many nodes you want to transfer the boradcast to.
	public static final int MAX_PUBLISH_FANOUT = 4; // when probabilistic algorithm hits - how many times to duplicate message. 
	public static final int GOSSIP_TO = 60; 			// seconds: delay between gossip.
	public static final int TOPICS_PER_MB =150;      // how many topics should each mailbox support. 
	public static final int CONTENT_GOSSIP_TO = 600;  // don't use...
	public static final int CLIENT_JOIN_TRIES = 3;       // how many concurrent queries client sends per topic join.
	public static final int BOX_LOOKUP_TRIES = 1; 
	public static final int LOOKUPTIMEOUT = 5;           // after how many seconds giveup... 
	public static final int POLLROUNDFREQUENCY =900;
	

}
