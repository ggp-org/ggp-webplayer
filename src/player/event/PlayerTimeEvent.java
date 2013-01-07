package player.event;

import org.ggp.shared.observer.Event;

public final class PlayerTimeEvent extends Event
{

	private final long time;

	public PlayerTimeEvent(long time)
	{
		this.time = time;
	}

	public long getTime()
	{
		return time;
	}

}
