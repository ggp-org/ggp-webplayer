package player.gamer.event;

import org.ggp.shared.gdl.grammar.GdlProposition;
import org.ggp.shared.match.Match;
import org.ggp.shared.observer.Event;

public final class GamerNewMatchEvent extends Event
{

	private final Match match;
	private final GdlProposition roleName;

	public GamerNewMatchEvent(Match match, GdlProposition roleName)
	{
		this.match = match;
		this.roleName = roleName;
	}

	public Match getMatch()
	{
		return match;
	}

	public GdlProposition getRoleName()
	{
		return roleName;
	}

}
