package player.gamer.event;

import org.ggp.galaxy.shared.gdl.grammar.GdlConstant;
import org.ggp.galaxy.shared.match.Match;
import org.ggp.galaxy.shared.observer.Event;

public final class GamerNewMatchEvent extends Event
{

	private final Match match;
	private final GdlConstant roleName;

	public GamerNewMatchEvent(Match match, GdlConstant roleName2)
	{
		this.match = match;
		this.roleName = roleName2;
	}

	public Match getMatch()
	{
		return match;
	}

	public GdlConstant getRoleName()
	{
		return roleName;
	}

}
