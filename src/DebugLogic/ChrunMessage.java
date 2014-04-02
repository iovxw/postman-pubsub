package DebugLogic;

import java.io.Serializable;

import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;

public class ChrunMessage implements Serializable,MessageContainer {

	private static final long serialVersionUID = -1208229888314032296L;
	public ChrunMessage()
	{
		
	}
	@Override
	public int GetMessageType() {
		return MessageType.Debug_Churn;
	}

	@Override
	public Object GetMessage() {
		return this;
	}
	
	
}
