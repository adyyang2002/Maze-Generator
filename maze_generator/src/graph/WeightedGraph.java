package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

//the Weight Graph class represents a directed graph using maps that contains vertices and edges
//the graph contains weights that are positive numbers and have vertexes that are never duplicates
//the Weighted Graph can perform Depth-First-Search, Breadth-First-Search, and Djikatra's.
/**
 * <P>
 * The Weighted Graph will maintain a collection of "GraphAlgorithmObservers",
 * which will be notified during the performance of the graph algorithms to
 * update the observers on how the algorithms are progressing.
 * </P>
 */
public class WeightedGraph<V> {

	// initialized a map with a vertex as the key and another map with a vertex
	// and integer to represent the weight to another vertex
	public Map<V, Map<V, Integer>> map;
	/*
	 * Collection of observers. Be sure to initialize this list in the
	 * constructor. The method "addObserver" will be called to populate this
	 * collection. Your graph algorithms (DFS, BFS, and Dijkstra) will notify
	 * these observers to let them know how the algorithms are progressing.
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;

	//constructor for the weighted graph
	public WeightedGraph() {
		// initialize the map as a hashmap and the observerlist
		map = new HashMap<>();
		observerList = new ArrayList<GraphAlgorithmObserver<V>>();
	}

	/**
	 * Add a GraphAlgorithmObserver to the collection maintained by this graph
	 * (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	// adds a vertex to the graph, but if it is a duplicate, then it will throw
	// an illegal argument exception
	public void addVertex(V vertex) {
		// checks if the map contains the vertex in the parameter
		if (map.containsKey(vertex)) {
			throw new IllegalArgumentException();
		} else {
			// if the map does not contain the vertex, add it to the map with
			// the value as a new hashmap
			map.put(vertex, new HashMap<>());
		}
	}

	// returns true if the map contains the vertex in the parameter
	public boolean containsVertex(V vertex) {
		return map.containsKey(vertex);
	}

	// adds an edge with the weight in the parameter between two vertexes if
	// they both exist
	public void addEdge(V from, V to, Integer weight) {
		// there are three conditions, one where the weight is a positive number
		// and the map must have both the from and to get to add an edge between
		// them, with the weight
		if (weight < 0 || !map.containsKey(from) || !map.containsKey(to)) {
			throw new IllegalArgumentException();
		} else {
			map.get(from).put(to, weight);
		}
	}

	// returns the weight between two vertexes if they both exist
	public Integer getWeight(V from, V to) {
		// checks if there is a from and to vertex, and get the weight between
		// them if there is
		if (!map.containsKey(from) || !map.containsKey(to)) {
			throw new IllegalArgumentException();
		} else {
			return map.get(from).get(to);
		}
	}

	// this method will perform Breadth-First Search at the starting vertex in
	// the parameter and end once it finds the end vertex in the parameter
	public void DoBFS(V start, V end) {
		// notify Breadth-First Search has started
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyBFSHasBegun();
		}
		// initialized a visited set as a hashset and a queue as a linked list
		Set<V> visitedSet = new HashSet<>();
		Queue<V> queue = new LinkedList<>();
		// the queue begins with the start vertex in the parameter
		queue.add(start);

		// makes sure that the queue isn't empty
		while (!queue.isEmpty()) {
			// retrieve the head of the queue as the current vertex
			V currVertex = queue.poll();
			// add the current vertex to the visited set if it hasn't already
			// been visited
			if (!visitedSet.contains(currVertex)) {
				// when a vertex is visited, notify the observers in the
				// observerlist
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(currVertex);
				}
				visitedSet.add(currVertex);

				// if the Breadth-First Search reaches the end vertex in the
				// parameter, it stops the search
				if (currVertex.equals(end)) {
					break;
				}
				// gets all the adjacencies of the current vertex and add them
				// to the queue
				for (V adj : map.get(currVertex).keySet()) {
					queue.add(adj);
				}
			}
		}
		// notify the Breadth-First Search is over
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifySearchIsOver();
		}
	}

	// this method will perform Depth-First Search at the starting vertex in
	// the parameter and end once it finds the end vertex in the parameter
	public void DoDFS(V start, V end) {
		// notify the Depth-First Search has begun
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDFSHasBegun();
		}
		// initialized a visited set as a hashset and a stack as a stack
		Set<V> visitedSet = new HashSet<>();
		Stack<V> stack = new Stack<V>();
		// the stack begins with the start vertex in the parameter
		stack.push(start);

		// makes sure the stack isn't empty
		while (!stack.isEmpty()) {
			// retrieve the head of the stack as the current vertex
			V currVertex = stack.pop();
			// add the current vertex to the visited set if it hasn't already
			// been visited
			if (!visitedSet.contains(currVertex)) {
				// when a vertex is visited, notify the observers in the
				// observerlist
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(currVertex);
				}
				visitedSet.add(currVertex);
				// if the Depth-First Search reaches the end vertex in the
				// parameter, it stops the search
				if (currVertex.equals(end)) {
					break;
				}
				// gets all the adjacencies of the current vertex and add them
				// to the stack if they are not already in the visited set
				for (V adj : map.get(currVertex).keySet()) {
					if (!visitedSet.contains(adj)) {
						stack.push(adj);
					}
				}
			}
		}
		// notify the Depth-First Search is over
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifySearchIsOver();
		}
	}

	// this method will perform Dijsktra's algorithm to find the fastest route
	// with the weights to get from start to end

	// this method will continue even after finding the fastest route to find
	// all the the cost to get to every vertex from start
	public void DoDijsktra(V start, V end) {
		// notify the Dijsktra has begun
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraHasBegun();
		}
		// initialize a linked list called route to store the fastest route from
		// start to end
		LinkedList<V> route = new LinkedList<V>();

		// initialized a visited set as a hashset, a hashmap with the vertex and
		// its predecessor, and another hashmap with the vertex and weight as
		// the cost
		Set<V> finishedSet = new HashSet<>();
		Map<V, V> predMap = new HashMap<>();
		Map<V, Integer> cost = new HashMap<>();

		// adds all the vertexes in the map to the predecessor map with their
		// predecessor as null and add all the vertexes in the map to the cost
		// with its weight as infinity
		for (V vertex : map.keySet()) {
			predMap.put(vertex, null);
			cost.put(vertex, Integer.MAX_VALUE);
		}

		// changes the start predecessor to itself and the cost to itself to
		// zero
		cost.put(start, 0);
		predMap.put(start, start);

		// makes sure the finished set contains all the vertexes
		while (finishedSet.size() != predMap.size()) {
			// initialize a current vertex as the start and the smallest number
			// as infinity
			V curr = start;
			int smallestNumber = Integer.MAX_VALUE;
			// checks every vertex in the map to see if the cost is less than
			// the smallest number and the finished set doesn't contain the
			// current vertex
			for (V currVertex : map.keySet()) {

				if (cost.get(currVertex) < smallestNumber
						&& !finishedSet.contains(currVertex)) {
					// update the curr variable and smallest number variable
					curr = currVertex;
					smallestNumber = cost.get(currVertex);
				}
			}
			// add the curr to the finished set
			finishedSet.add(curr);
			// notify the observerlist that a vertex has been finished, with its
			// cost
			for (GraphAlgorithmObserver<V> observer : observerList) {
				observer.notifyDijkstraVertexFinished(curr, cost.get(curr));
			}
			// initialize a set to get all the adjacencies of the curr vertex
			Set<V> adj = map.get(curr).keySet();
			// checks every adjacencies
			for (V neighbor : adj) {
				// creates an int as the the weight to get between the curr and
				// neighbor vertex and another int as the cost it gets to the
				// curr
				int weight = map.get(curr).get(neighbor);
				int currCost = cost.get(curr);
				// if the curr cost plus the weight is less than the neighbor's
				// stored cost, it replaces the cost with the new cost and the
				// predecessor with the new predecessor
				if (weight + currCost < cost.get(neighbor)) {
					cost.replace(neighbor, weight + currCost);
					predMap.replace(neighbor, curr);
				}
			}
		}

		// after the dijsktra algorithm has been performed on all of the
		// vertexes, add the end vertex to the shortest route
		route.add(end);
		V curr = end;
		//traces back to predecessors until it reaches the start vertex
		while (route.get(0) != start) {
			V pred = predMap.get(curr);
			//add the predecessor to the front
			route.addFirst(pred);
			//ends after it finds the start
			if (pred.equals(start)) {
				break;
			}
			curr = pred;
		}
		// notify Dijsktra is over with the shortest route
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraIsOver(route);
		}

	}
}
