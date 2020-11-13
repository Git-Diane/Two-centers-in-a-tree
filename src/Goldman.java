import java.io.File;
import java.util.Vector;

public class Goldman { // GOLDMAN'S ALGORHTM TO FIND THE 1-CENTER OF A TREE
	// HELPS BETTER UNDERSTAND METHOD "goldman" IN CLASS LinkDeletion

	// Which test file we are using
    public static int fileNum = 10;

	// The tree in which we are looking for the center

    public static Tree tree;

	// adj_lists : stores the edges of "tree" in the form of adjacency lists

	public static Vector<Vector<Integer>> adj_lists; // adj_lists.get(i) is a vector containing all the neighbors of vertex i
	
	public static int still_in_tree; // a vertex that is still in the stree, that we use as a startpoint for a DFS to find an end-vertex in method Step2
	// at the end of the algorithm, still_in_tree will be the center of the tree
	
	public static int[] weights; // weights of the nodes
	
	public static int W; //weight (sum of the individual nodes' weights) of the tree

	public static boolean stop = false;
		
	public static void main(String[] args) throws Exception {
				
		// Step 1: Reading the file
		
		
		File file = new File("C:/Users/Diane/eclipse-workspace/INF 421 project/tests/centers."+fileNum+".in");
		
		Goldman.tree = new Tree(file); // tree has 3 attributes : "N", "w" (array storing the weights of the vertices) and "edges" (array storing all edges once)
		
		Goldman.weights = tree.w;
		
		for (int i =0; i < tree.N;i++) {
			Goldman.W +=weights[i];
		}
	
		// ***Step 2: Calculate the adjacency lists*** (complexity O(n))

		adj_lists = tree.adj_lists();
		
		
		while (!stop) {
			stop = step();
		}
		
		System.out.println(still_in_tree+1);
	}
	
	public static boolean step() {
		if (adj_lists.get(still_in_tree).size() == 0) {
			return true;
		}
		int endVertex = Goldman.endVertex();
		if (2*weights[endVertex] >= Goldman.W) {
			still_in_tree = endVertex; //we stop : endVertex is a center
			return true;
		}
		else {
			int neighbor = adj_lists.get(endVertex).get(0); // the only neighbor of endVertex
			Object o = new Integer(endVertex);
			adj_lists.get(neighbor).remove(o); // we delete the link between neighbor and endVertex
			weights[neighbor] += weights[endVertex];
			still_in_tree = neighbor;
			return false;
		}
	}


		
	public static int endVertex() { //we start from "still_in_tree" and we follow any path until we find an end-vertex
		
		int last = still_in_tree;

		boolean[] marked = new boolean[tree.N];
		marked[still_in_tree] = true;
		
		int cnt = 0;
		
		aa:
		while (cnt < tree.N*10) {
			
			cnt+=1;
			
			for (int i = 0; i < adj_lists.get(last).size(); i++)  {
	        	
	            // b is neighbor of node y
	            int b = adj_lists.get(last).get(i); 
	  
	            
	            if (!marked[b]) {
	            	
	            	marked[b] = true;
	            	last = b;
	            	
	            	
	            	continue aa;
	            }
	        
			}
			
			break aa; // if "last" has no unmarked neighbor, then it is an end-vertex
		}
		
		return last;
	}
}
