package player.gamer;

import java.util.ArrayList;
import java.util.List;

import player.gamer.exception.MetaGamingException;
import player.gamer.exception.MoveSelectionException;

import org.ggp.galaxy.shared.gdl.grammar.GdlConstant;
import org.ggp.galaxy.shared.gdl.grammar.GdlTerm;
import org.ggp.galaxy.shared.match.Match;
import org.ggp.galaxy.shared.observer.Event;
import org.ggp.galaxy.shared.observer.Observer;
import org.ggp.galaxy.shared.observer.Subject;

/**
 * The Gamer class defines methods for both meta-gaming and move selection in a
 * pre-specified amount of time. The Gamer class is based on the <i>algorithm</i>
 * design pattern.
 */
public abstract class Gamer implements Subject
{	
	private Match match;
	private GdlConstant roleName;

	public Gamer()
	{
		observers = new ArrayList<Observer>();
		
		// When not playing a match, the variables 'match'
		// and 'roleName' should be NULL. This indicates that
		// the player is available for starting a new match.
		match = null;
		roleName = null;
	}

	/* The following values are recommendations to the implementations
	 * for the minimum length of time to leave between the stated timeout
	 * and when you actually return from metaGame and selectMove. They are
	 * stored here so they can be shared amongst all Gamers. */
    public static final long PREFERRED_METAGAME_BUFFER = 3900;
    public static final long PREFERRED_PLAY_BUFFER = 1900;    
	
	// ==== The Gaming Algorithms ====
	public abstract void metaGame(long timeout) throws MetaGamingException;
	
	public abstract GdlTerm selectMove(long timeout) throws MoveSelectionException;
	
	// ==== Gamer Profile and Configuration ====
	public abstract String getName();

	// ==== Accessors ====	
	public final Match getMatch() {
		return match;
	}
	
	public final void setMatch(Match match) {
		this.match = match;
	}

	public final GdlConstant getRoleName() {
		return roleName;
	}
	
	public final void setRoleName(GdlConstant roleName) {
		this.roleName = roleName;
	}
	
	// ==== Observer Stuff ====
	private final List<Observer> observers;
	public final void addObserver(Observer observer)
	{
		observers.add(observer);
	}
	
	public final void notifyObservers(Event event)
	{
		for (Observer observer : observers) {
			observer.observe(event);
		}
	}	
}