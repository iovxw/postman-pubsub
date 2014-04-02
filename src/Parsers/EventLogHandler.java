package Parsers;

public interface EventLogHandler {
	public void HandleJoin(String time);
	public void HandleLeave(String time);
	public void HandleTweet(String time, String tweet);

}
