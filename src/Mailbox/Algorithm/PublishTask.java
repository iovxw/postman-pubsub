package Mailbox.Algorithm;

import Mailbox.Messages.PublicationMessage;

public class PublishTask implements Runnable{

	private PublicationMessage pm;
	private Mailbox homeNode;
	public PublishTask(Mailbox box)
	{
		
		homeNode = box;
	}
	public PublishTask setPublication(PublicationMessage p){
		this.pm = p;
		return this;
	}
	@Override
	public void run() {
		homeNode.Publish(pm, pm.topics.get(0));
		
	}

}
