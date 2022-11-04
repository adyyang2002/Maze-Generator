package graph;

import maze.Juncture;
import maze.Maze;

//this class is a constructor for the maze by creating a maze with the weighted graph class
public class MazeGraph extends WeightedGraph<Juncture> {
	// this constructor takes a maze as the parameter and uses method for in the
	// maze class to create a maze
	public MazeGraph(Maze maze) {
		// add vertexes with a nested for-loop to find the height and weight
		for (int y = 0; y < maze.getMazeHeight(); y++) {
			for (int x = 0; x < maze.getMazeWidth(); x++) {
				addVertex(new Juncture(x, y));
			}
		}
		// nested for-loop to get all the adjacencies of the curr vertex
		for (Juncture curr : super.map.keySet()) {
			for (Juncture adj : super.map.keySet()) {
				// if the curr and adj are the same, continue
				if (curr == adj) {
					continue;
				}
				// checks if the adjacent vertex is above the curr vertex
				if (curr.getY() == adj.getY() + 1
						&& curr.getX() == adj.getX()) {
					// adds an edge if there is no wall in between them
					if (!maze.isWallAbove(curr)) {
						addEdge(curr, adj, maze.getWeightAbove(curr));
					}
					// checks if the adjacent vertex is below the curr vertex
				} else if (curr.getY() + 1 == adj.getY()
						&& curr.getX() == adj.getX()) {
					// adds an edge if there is no wall in between them
					if (!maze.isWallBelow(curr)) {
						addEdge(curr, adj, maze.getWeightBelow(curr));
					}
					// checks if the adjacent vertex is to the left of the curr
					// vertex
				} else if (curr.getY() == adj.getY()
						&& curr.getX() == adj.getX() + 1) {
					// adds an edge if there is no wall in between them
					if (!maze.isWallToLeft(curr)) {
						addEdge(curr, adj, maze.getWeightToLeft(curr));
					}
					// checks if the adjacent vertex is to the right of the curr
					// vertex
				} else if (curr.getY() == adj.getY()
						&& curr.getX() + 1 == adj.getX()) {
					// adds an edge if there is no wall in between them
					if (!maze.isWallToRight(curr)) {
						addEdge(curr, adj, maze.getWeightToRight(curr));
					}
				}
			}
		}
	}
}
