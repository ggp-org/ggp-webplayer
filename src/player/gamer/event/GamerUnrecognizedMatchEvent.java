package player.gamer.event;

import org.ggp.shared.observer.Event;

public final class GamerUnrecognizedMatchEvent extends Event
{

	private final String matchId;

	public GamerUnrecognizedMatchEvent(String matchId)
	{
		this.matchId = matchId;
	}

	public String getMatchId()
	{
		return matchId;
	}

}
