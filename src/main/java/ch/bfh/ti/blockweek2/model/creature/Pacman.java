package ch.bfh.ti.blockweek2.model.creature;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.ti.blockweek2.GameController;
import ch.bfh.ti.blockweek2.model.maze.MazeActor;

import java.io.IOException;

/**
 * Actor Class for Pacman
 */
public class Pacman extends AbstractBehavior<Pacman.Walk> {
	ActorRef<MazeActor.Command> maze = getContext().spawn(MazeActor.create(), "Maze");
	private static int posX;
	private static int posY;
	private static int requestWalk;
	private static int points = 0;

	/**
	 * Interface for Pacman class
	 */
	public interface Walk {
	}

	/**
	 * Enum class for to help pacman walk
	 */
	public enum Turn implements Walk {
		LEFT, RIGHT, UP, DOWN
	}

	/**
	 * Enum class for to help pacman walk
	 */
	public enum Go implements Walk {
		OK, NO
	}

	/**
	 * Help class for Position
	 */
	public static class Position implements Walk {
		public final int x;
		public final int y;

		public Position(int posX, int posY) {
			this.x = posX;
			this.y = posY;
		}
	}

	/**
	 * Create an Actor type Pacman
	 *
	 * @return This Behavior
	 */
	public static Behavior<Walk> create() {
		return Behaviors.setup(Pacman::new);
	}

	/**
	 * Class Constructor
	 */
	private Pacman(ActorContext<Walk> context) {
		super(context);
	}

	/**
	 * This Method will be called Each time a Message is passed to this Actor
	 *
	 * @return Call a method based on the received Message
	 */
	@Override
	public Receive<Walk> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals(Turn.LEFT, this::walkLeft)
				.onMessageEquals(Turn.RIGHT, this::turnRight)
				.onMessageEquals(Turn.UP, this::turnUp)
				.onMessageEquals(Turn.DOWN, this::turnDown)
				.onMessageEquals(Go.OK, this::onOK)
				.onMessageEquals(Go.NO, this::onNO)
				.onMessage(Position.class, this::setPosition)
				.build();
	}

	/**
	 * Setter for Position
	 *
	 * @param p contain data
	 * @return the new Pos
	 */
	private Behavior<Walk> setPosition(Position p) {
		posX = p.x;
		posY = p.y;
		return this;
	}

	/**
	 * Walk Left and Send the Message to MazeActor to be checked if its OK not NO
	 *
	 * @return this Behavior
	 */
	private Behavior<Walk> walkLeft() {
		requestWalk = 0;
		System.out.println("PacMan say: Can I go LEFT?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 0));
		return this;
	}
	/**
	 * to Walk Up and Send the Message to MazeActor to be checked if its OK not NO
	 *
	 * @return this Behavior
	 */
	private Behavior<Walk> turnUp() {
		requestWalk = 1;
		System.out.println("PacMan say: Can I go UP?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 1));
		return this;
	}
	/**
	 * to Walk Right and Send the Message to MazeActor to be checked if its OK not NO
	 *
	 * @return this Behavior
	 */
	private Behavior<Walk> turnRight() {
		requestWalk = 2;
		System.out.println("PacMan say: Can I go RIGHT?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 2));
		return this;
	}
	/**
	 * to Walk Down and Send the Message to MazeActor to be checked if its OK not NO
	 *
	 * @return this Behavior
	 */
	private Behavior<Walk> turnDown() {
		requestWalk = 3;
		System.out.println("PacMan say: Can I go DOWN?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 3));
		return this;
	}

	/**
	 * This method will be called in case pacman can walk
	 *
	 * @return the Behavior
	 */
	private Behavior<Walk> onOK() throws IOException {
		switch (requestWalk) {
			case 0:
				posY--;
				break;
			case 2:
				posY++;
				break;
			case 1:
				posX--;
				break;
			case 3:
				posX++;
				break;
		}
		points++;
		System.out.println("PacMan say: MOVED My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		System.out.println("Total Points --> " + points);
		System.out.println();
		GameController.setPacmanImg(posX, posY, requestWalk);
		return this;
	}

	/**
	 * This method will be called in case pacman cant walk
	 *
	 * @return the Behavior
	 */
	private Behavior<Walk> onNO()  {
		System.out.println("PacMan say: NO MOVED My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		int truePos = 987;
		System.out.println(truePos);
		System.out.println();
		return this;
	}
}

