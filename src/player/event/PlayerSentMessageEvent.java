package player.event;

import org.ggp.shared.observer.Event;

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
