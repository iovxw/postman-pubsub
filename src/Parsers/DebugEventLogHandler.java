package Parsers;

public class DebugEventLogHandler implements EventLogHandler {

	@Override
	public void HandleJoin(String time) {
		System.out.println("Handle join at:" + time);
		
	}

	@Override
	public void HandleLeave(String time) {
		System.out.println("Handle leave at:" + time);
		
	}

	@Override
	public void HandleTweet(String time, String tweet) {
		System.out.println("Handle tweet at:" + time +": " + tweet );
		
	}
	

}
