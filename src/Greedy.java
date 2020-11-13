import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

//for writing the output files
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Greedy { // PROPOSED SOLUTION NUMBER 1
	
	// c1, c2: the two centers of the tree

	public static int c1 = 0; 
	public static int c2 = 0; 
	
	// Which test file we are using
    public static int fileNum = 1;
	
	// path: list where we store all vertices on the path from c1 to c2
	// useful in order to know which center a vertex is closer to

	public static LinkedList<Integer> path = new LinkedList<Integer>();
	
	// The tree in which we are looking for the two centers

	public static Tree tree;

	// adj_lists : stores the edges of "tree" in the form of adjacency lists

	public static Vector<Vector<Integer>> adj_lists; // adj_lists.get(i) is a vector containing all the neighbors of vertex i

	public static boolean possible_to_improve = true; // when possible_to_improve = false, we stop updating c1 and c2
	
	public static long score; // S(c1,c2) (score of the current centers)
	
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.nanoTime(); // to compute the runtime of the algorithm
		
		// Step 1: Reading the file
		
		
		File file = new File("C:/Users/Diane/eclipse-workspace/INF 421 project/tests/centers."+fileNum+".in");
		
		Greedy.tree = new Tree(file); // tree has 3 attributes : "N", "w" (array storing the weights of the vertices) and "edges" (array storing all edges once)
	
		// ***Step 2: Computing the adjacency lists*** (complexity O(n))

		adj_lists = tree.adj_lists();
		
		//***Step 3: Find the diameter*** (complexity O(n))
				
		// It can be shown that if we start from any vertex (for instance, 0) and find the farthest away vertex (v1) from 0, and then again find the farthest away vertex (v2) from v1...
		//... then v1_v2 is the diameter - ie the longest path - of the graph
		
		int v1 = Greedy.BFS(0); // vertex farthest away from 0
		int v2 = Greedy.BFS(v1);
		
				
		//We now need to do a DFS because we also want the path v1 - v2 (complexity O(n))
		
		
		LinkedList<Integer> diameter = Greedy.DFS(v1,v2);
		 
		//Step 4: Greedy algorithm
		
		
		int longDiam = diameter.size();
		
		Greedy.c1 = diameter.get(longDiam/2-1);
		Greedy.c2 = diameter.get(longDiam/2); // we start with the centers in the middle of the diameter

		Greedy.path.add(c1);
		Greedy.path.add(c2);
		Greedy.score = S(c1,c2);
		
		while (Greedy.possible_to_improve == true) {
			Greedy.update();
			
		}
		
		long endTime   = System.nanoTime();
		long runTime = endTime - startTime;
		
		c1+=1;
		c2+=1;
		
		System.out.println("c1:");
		System.out.println(c1);
		System.out.println("c2:");
		System.out.println(c2);
		System.out.println("S:");
		System.out.println(Greedy.score);
		System.out.println("Run time:");
		System.out.println(runTime);
		//Greedy.writeOutput(runTime); //we used this line of code to write the output files
	}
	
	
	
	
	public static void update() { //improving the score by moving c1 and/or c2 to one of their neighbors
		int newc1 = c1;
		int newc2 = c2;
		long newS;
		
		adj_lists.get(c1).add(c1);
		adj_lists.get(c2).add(c2);
		int x = 0;
		int y = 0;

		for (int i = 0; i < adj_lists.get(c1).size(); i++)  {
			x = adj_lists.get(c1).get(i); 
			if (x != Greedy.path.get(1)) {
				for (int j = 0; j < adj_lists.get(c2).size(); j++)  {
					y = adj_lists.get(c2).get(j); 
					if (y != Greedy.path.get(path.size()-2)) {
						newS =  S(x,y);

						if (newS < Greedy.score) {
					    	Greedy.score = newS;
					    	newc1 = x;
					    	newc2 = y;
					    }
					}
				}
			}
		}
		
		adj_lists.get(c2).removeElementAt(adj_lists.get(c2).size()-1);
		adj_lists.get(c1).removeElementAt(adj_lists.get(c1).size()-1);

		if (newc1 == c1 && newc2 == c2) {
			possible_to_improve = false; // no movement of the centers to their neighbors could improve the score : we stop
		}
		if (newc1 != c1) {
			Greedy.c1 = newc1;
			Greedy.path.addFirst(c1); // we update the path
		}
		
		if (newc2 != c2) {
			Greedy.c2 = newc2;
			Greedy.path.add(c2);
		}
		
			
	}
	
	public static long S(int x, int y) { // computes S(x,y) where x (resp. y) is c1 (resp. c2) or one of its neighbors
		if (x!= c1) {
			path.addFirst(x);
		}
		
		if (y!=c2) {
			path.add(y);
		}
		int break_point_x = path.get(path.size()/2);
		// we will calculate S(x,y) first by a BFS starting from x and stopping at break_point_y (excluded) ; then by a BFS starting from y and stopping at break_point_y (excluded)
		// if some vertices are equally distant from x and y, we count them in the BFS from y
		
		int break_point_y = path.get(path.size()/2 -1);
		
		if (x!= c1) {
			path.removeFirst();
		}
		
		if (y!=c2) {
			path.removeLast();
		}
		
		return Greedy.S_BFS(x, break_point_x) + Greedy.S_BFS(y, break_point_y);
	}
	
	public static long S_BFS(int start_pt, int break_pt) { // computes the weighted sum of distances from start_pt to any vertex that is not beyond break_pt
		
		long S = 0;
		
	    // array to store level of each node  
		int level[] = new int[Greedy.tree.N]; 
		
		// create a queue  
	    LinkedList<Integer> que = new LinkedList<Integer>(); 
	    // enqueue element start_pt 
	    que.add(start_pt);  
	    
	    boolean[] seen = new boolean[Greedy.tree.N];
	    
	    seen[start_pt] = true;
	    seen[break_pt] = true;
	    level[break_pt] = 0;
	    
	    // do until queue is empty  
	    while (que.size() > 0)  
	    {  
	        // get the first element of queue and dequeue it 
	        int y = que.poll();  
	  
	        
	        // traverse neighbors of node y 
	        for (int i = 0; i < adj_lists.get(y).size(); i++)  {
	        	
	            // b is neighbor of node y
	            int b = adj_lists.get(y).get(i); 
	  
	            // if b has not been visited yet
	            if (!seen[b]) 
	            {  
	  
	                // enqueue b in queue  
	                que.add(b);  
	                seen[b] = true;
	                level[b] = level[y] + 1;
	                S += level[b] * Greedy.tree.w[b];
	  	                
	            }  
	            
	        }  
	    } 
		return S;
	}

	
	public static LinkedList<Integer> DFS(int v1, int v2){ // Depth First Search to find the path v1-v2, starting from v1
		
		LinkedList<Integer> diameter = new LinkedList<Integer>();
		diameter.add(v1);
		boolean[] marked = new boolean[tree.N];
		marked[v1] = true;
		
		int cnt = 0;
		
		aa:
		while (cnt < tree.N*10) {
			
			cnt+=1;
			int y = diameter.getLast();
			
			for (int i = 0; i < adj_lists.get(y).size(); i++)  {
	        	
	            // b is neighbor of node y
	            int b = adj_lists.get(y).get(i); 
	  
	            
	            if (!marked[b]) {
	            	
	            	marked[b] = true;
	            	diameter.add(b);
	            	if (b==v2) {
	            		break aa;
	            	}
	            	
	            	continue aa;
	            }
	        
			}
			
			diameter.removeLast();
			
		}
		
		return diameter;
	}
	
	public static int BFS(int x){ // Using Breadth First Search, finds the vertex the farthest away from x
		
		// create a queue  
	    LinkedList<Integer> que = new LinkedList<Integer>(); 
	    // enqueue element x  
	    que.add(x);  
	    
	    boolean[] seen = new boolean[Greedy.tree.N];
	    
	    seen[x] = true;
	    int farthest = 0;
	    // do until queue is empty  
	    while (que.size() > 0)  
	    {  
	        // get the first element of queue and dequeue it 
	        int y = que.poll();  
	        farthest = y;
	  
	        
	        // traverse neighbors of node y 
	        for (int i = 0; i < Greedy.adj_lists.get(y).size(); i++)  {
	        	
	            // b is neighbor of node y
	            int b = Greedy.adj_lists.get(y).get(i); 
	  
	            // if b has not been visited yet
	            if (!seen[b]) 
	            {  
	  
	                // enqueue b in queue  
	                que.add(b);  
	                seen[b] = true;
	  	                
	            }  
	            
	        }  
	    } 
	    
	    return farthest; // the last to be unqueued is the farthest from x
		
	}
	
	public static void writeOutput(long runTime) { //writes the output in text files
		try {
            File statText = new File("C:/Users/Diane/eclipse-workspace/INF 421 project/outputGreedy/output."+fileNum+".txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write("c1\n");
            w.write(c1+"\n");
            w.write("c2\n");
            w.write(c2+"\n");
            w.write("S\n");
            w.write(Long.toString(score)+"\n");
            w.write("Runtime\n");
            w.write(Long.toString(runTime)+"\n");

            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
	}
}
