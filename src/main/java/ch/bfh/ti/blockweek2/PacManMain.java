package ch.bfh.ti.blockweek2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.ti.blockweek2.model.creature.Pacman;
import ch.bfh.ti.blockweek2.model.maze.MazeActor;

/**
 * Pacman Actor Manager
 */
public class PacManMain extends AbstractBehavior<PacManMain.InitMessage> {
	ActorRef<Pacman.Walk> pacman = getContext().spawn(Pacman.create(), "PacMan");

	/**
	 * Help class for PacmanMain
	 */
	public static final class InitMessage {
		private final int turn;

		public InitMessage(int turn) {
			this.turn = turn;
		}
	}

	/**
	 * Create new Actor type PacmanMain
	 *
	 * @return this Behavior
	 */
	public static Behavior<InitMessage> create() {
		return Behaviors.setup(PacManMain::new);
	}

	/**
	 * Class Constructor
	 */
	public PacManMain(ActorContext<InitMessage> context) {
		super(context);
	}

	/**
	 * This Method will be called Each time a Message is passed to this Actor
	 *
	 * @return Call a method based on the received Message
	 */
	@Override
	public Receive<PacManMain.InitMessage> createReceive() {
		return newReceiveBuilder()
				.onMessage(InitMessage.class, this::onInitMessage)
				.build();
	}

	/**
	 * Take the KeyStroke and send the Message to Pacman Actor
	 *
	 * @param command contain data
	 * @return This Behavior
	 */
	private Behavior<InitMessage> onInitMessage(InitMessage command) {

		switch (command.turn) {
			case 0:
				pacman.tell(Pacman.Turn.LEFT);
				break;
			case 1:
				pacman.tell(Pacman.Turn.UP);
				break;
			case 2:
				pacman.tell(Pacman.Turn.RIGHT);
				break;
			case 3:
				pacman.tell(Pacman.Turn.DOWN);
				break;
			case 9: // Start position of pacman
				pacman.tell(new Pacman.Position(12, 39));
		}
		return this;
	}
}
