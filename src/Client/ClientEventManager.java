package Client;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Parser.Event;

public class ClientEventManager {
	private final ScheduledExecutorService pool;
	private final List<Event> events;
	private final ClientApplication Capp;
	private final int MyId;
	double TimeNow;
	double LastDelta;
	
	
	
	ClientEventManager(ClientApplication app)
	{
		MyId = app.getMyId();
		Capp = app;
		events = app.getMyEvents();
		TimeNow = app.getFrom()*1000*60*60*24;
		Event next = events.get(0);
		
		
		
		pool =  Executors.newScheduledThreadPool(1);
		//todo: fix. 
		pool.schedule(new Runnable() {
			
			@Override
			public void run() {
				if(events.size()>0)
				{
					Event e = events.remove(0);
					doEvent(e);
					
					if(events.isEmpty())
					{
						Capp.calculateHitRate();
						
					}
					
				}
				
			}


		},next.delta,TimeUnit.MICROSECONDS);
		
	}
	
	
	private void doEvent(Event toDo)
	{
		TimeNow += toDo.delta;
		switch(toDo.EventType)
		{
		case 0: 
			Capp.doLogin();
			break;
		case 2: 
			Capp.PublishTopic(MyId, toDo.Tweet);
			// for debug only
			ClientTester.writepublishtostats(MyId);
			break;
			
		case 1: 
			Capp.doLogout();
			break;
		}
		Event e = events.get(0);
		pool.schedule(new Runnable() {
			@Override
			public void run() {
				if(events.size()>0)
				{
					Event e = events.remove(0);
					doEvent(e);
				}
				
			}
		},e.delta,TimeUnit.MILLISECONDS);
		
	}
}
