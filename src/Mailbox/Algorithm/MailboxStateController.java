package Mailbox.Algorithm;

public class MailboxStateController {
	private Mailbox mBox;
	private boolean isActive;
	public MailboxStateController(Mailbox m)
	{
		mBox = m;
	}
	public synchronized int getState()
	{
		if(!isActive)
			return MailboxState.Inactive;
		if(mBox.buckets.rawTopics.size() <Configuration.Configuration.TOPICS_PER_MB)
				return MailboxState.Recruting;
		
		return MailboxState.Full;
	}
	public void SetActive()
	{
		isActive =true;
	}
	
}
