package player.event;

import org.ggp.galaxy.shared.observer.Event;

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
