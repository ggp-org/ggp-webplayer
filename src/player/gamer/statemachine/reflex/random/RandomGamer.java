package player.gamer.statemachine.reflex.random;

import java.util.List;
import java.util.Random;

import player.gamer.statemachine.StateMachineGamer;
import player.gamer.statemachine.reflex.event.ReflexMoveSelectionEvent;
import org.ggp.galaxy.shared.statemachine.Move;
import org.ggp.galaxy.shared.statemachine.StateMachine;
import org.ggp.galaxy.shared.statemachine.exceptions.GoalDefinitionException;
import org.ggp.galaxy.shared.statemachine.exceptions.MoveDefinitionException;
import org.ggp.galaxy.shared.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.galaxy.shared.statemachine.implementation.prover.ProverStateMachine;

/**
 * RandomGamer is a very simple state-machine-based Gamer that will always
 * pick randomly from the legal moves it finds at any state in the game. This
 * is one of a family of simple "reflex" gamers which act entirely on reflex
 * (picking the first legal move, or a random move) regardless of the current
 * state of the game.
 * 
 * This is not really a serious approach to playing games, and is included in
 * this package merely as an example of a functioning Gamer.
 */
public final class RandomGamer extends StateMachineGamer
{
	/**
	 * Does nothing
	 */
	@Override
	public void stateMachineMetaGame(long timeout) throws TransitionDefinitionException, MoveDefinitionException, GoalDefinitionException
	{
		// Do nothing.
	}
	/**
	 * Selects a random legal move
	 */
	@Override
	public Move stateMachineSelectMove(long timeout) throws TransitionDefinitionException, MoveDefinitionException, GoalDefinitionException
	{
		long start = System.currentTimeMillis();

		List<Move> moves = getStateMachine().getLegalMoves(getCurrentState(), getRole());
		Move selection = (moves.get(new Random().nextInt(moves.size())));

		long stop = System.currentTimeMillis();

		notifyObservers(new ReflexMoveSelectionEvent(moves, selection, stop - start));
		return selection;
	}

	@Override
	public StateMachine getInitialStateMachine() {
		return new ProverStateMachine();
	}

	@Override
	public String getName() {
		return "Random";
	}
}