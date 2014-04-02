package Multicast.Messages;

import java.io.Serializable;

public class BasicStringContainer implements MessageContainer, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2088717691038181572L;
	private String content;
	public BasicStringContainer(String message)
	{
		this.content = message;
	}
	@Override
	public int GetMessageType() {
		return MessageType.String;
	}

	@Override
	public Object GetMessage() {
		return this.content;
	}
	@Override
	public String toString()
	{
		return this.content;
	}

}
