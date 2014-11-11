package pushlet;

import java.util.Map;
import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventPullSource;
import org.rzy.util.ServerInfo;

public class ServerInfoEvent extends EventPullSource
{
	@Override
	protected long getSleepTime()
	{
		return 1000;
	}

	@Override
	protected Event pullEvent()
	{
		Event event = Event.createDataEvent("/serverinfo");
		Map<String, Object> map = ServerInfo.mem();
		for (Map.Entry<String, Object> eEntry : map.entrySet())
		{
			event.setField(eEntry.getKey(), eEntry.getValue().toString());
		}
		return event;
	}
}
