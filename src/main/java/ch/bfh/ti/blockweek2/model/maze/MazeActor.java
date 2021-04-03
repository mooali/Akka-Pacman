package ch.bfh.ti.blockweek2.model.maze;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.ti.blockweek2.GameController;
import ch.bfh.ti.blockweek2.model.creature.Pacman;

import java.io.FileNotFoundException;

/**
 * Actor class for MazeActor
 */
public class MazeActor extends AbstractBehavior<MazeActor.Command> {
	ActorRef<Pacman.Walk> pacman = getContext().spawn(Pacman.create(), "PacMac");

	// Command for Maze
	public interface Command {
	}

	/**
	 * Help class for Position
	 */
	public static class Position implements Command {
		public final int x;
		public final int y;
		public int turn;

		public Position(int x, int y, int turn) {
			this.x = x;
			this.y = y;
			this.turn = turn;
		}
	}

	/**
	 * Create new Actor type MazeActor
	 *
	 * @return this Behaviors
	 */
	public static Behavior<Command> create() {
		return Behaviors.setup(MazeActor::new);
	}

	/**
	 * Class Constructor
	 */
	public MazeActor(ActorContext<Command> context) {
		super(context);
	}

	/**
	 * This Method will be called Each time a Message is passed to this Actor
	 *
	 * @return Call a method based on the received Message
	 */
	@Override
	public Receive<MazeActor.Command> createReceive() {
		return newReceiveBuilder()
				.onMessage(Position.class, this::onWalk)
				.build();
	}

	/**
	 * This Method checks if the pacman can walk or not and send the Message back to Pacman Actor
	 *
	 * @param p contain data
	 * @return Behavior
	 */
	private Behavior<Command> onWalk(Position p) {
		switch (p.turn) {
			case 0: // LEFT
				if (p.y == 0 || Maze.maze[p.x][p.y - 1] <= 1) {
					System.out.println("Maze Actor say: NO : ");
					pacman.tell(Pacman.Go.NO);
					break;
				} else {
					GameController.mazeClear(p.x, p.y);
					System.out.println("Maze Actor say: OK : ");
					pacman.tell(Pacman.Go.OK);
				}
				break;
			case 2: //RIGHT
				if (p.y == Maze.maze[0].length - 1 || Maze.maze[p.x][p.y + 1] <= 1) {
					System.out.println("Maze Actor say: NO : ");
					pacman.tell(Pacman.Go.NO);
					break;
				} else {
					GameController.mazeClear(p.x, p.y);
					System.out.println("Maze Actor say: OK : ");
					pacman.tell(Pacman.Go.OK);
				}
				break;
			case 1: //UP
				if (p.x == 1 || Maze.maze[p.x - 1][p.y] <= 1) {
					System.out.println("Maze Actor say: NO : ");
					pacman.tell(Pacman.Go.NO);
					break;
				} else {
					GameController.mazeClear(p.x, p.y);
					System.out.println("Maze Actor say: OK : ");
					pacman.tell(Pacman.Go.OK);
				}
				break;
			case 3: //DOWN
				if (p.x == Maze.maze.length - 1 || Maze.maze[p.x + 1][p.y] <= 1) {
					System.out.println("Maze Actor say: NO : ");
					pacman.tell(Pacman.Go.NO);
					break;
				} else {
					GameController.mazeClear(p.x, p.y);
					System.out.println("Maze Actor say: OK : ");
					pacman.tell(Pacman.Go.OK);
				}
				break;

		}
		return this;
	}

}