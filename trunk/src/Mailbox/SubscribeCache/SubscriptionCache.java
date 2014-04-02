package Mailbox.SubscribeCache;

import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TemporarySubscribeMessage;

public interface SubscriptionCache 
{
	 void DistributePublish(PublicationMessage pm);
	 void TemporarySubscribe(TemporarySubscribeMessage tsm);
}
