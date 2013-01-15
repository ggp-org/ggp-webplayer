package player.event;

import org.ggp.galaxy.shared.observer.Event;

public final class PlayerSentMessageEvent extends Event
{

	private final String message;

	public PlayerSentMessageEvent(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

}
