package Client;

import java.util.Properties;

import Client.MultipleTopicContainer.CompactTopologyDataStructure;
import Client.MultipleTopicContainer.TopologyDataStructure;
import Mailbox.Algorithm.Mailbox;
import Mailbox.SubscribeCache.LazyUpdateSubscriptionCache;
import Mailbox.SubscribeCache.SubscriptionCache;
import Multicast.Algorithm.ChordData;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

public class ClientApplicationModule extends AbstractModule {

private final Properties properties;
	
	private Properties getDefaultProperties() {
		Properties defaultProps = new Properties();
		
		defaultProps.setProperty("gil.param", "1");
		defaultProps.setProperty("gil.client.name", "someName");
		return defaultProps;
	}
	
	public ClientApplicationModule() {
		this(new Properties());
	}
	
	public ClientApplicationModule(Properties properties) {
		this.properties = getDefaultProperties();
		this.properties.putAll(properties);
	}
	
	public ClientApplicationModule setProperty(String name, String value) {
		this.properties.setProperty(name, value);
		return this;
	}
	
	
	@Override
	protected void configure() {
		Names.bindProperties(binder(), properties);
		
		bind(ChordData.class).in(Scopes.SINGLETON);
		bind(Mailbox.class).in(Scopes.SINGLETON);
		bind(ClientApplication.class);
		bind(TopologyDataStructure.class).to(CompactTopologyDataStructure.class);
		bind(SubscriptionCache.class).to(LazyUpdateSubscriptionCache.class).in(Scopes.SINGLETON);
	}

}
