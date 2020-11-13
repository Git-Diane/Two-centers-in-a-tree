import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TwoCentersFloydWarshall { // A NAIVE IMPLEMENTATION - FOR INFORMATION ONLY
	
	// Which test file we are using
    public static int fileNum = 1;

	
	// algorithm with time complexity O(N^3)
	// first step : compute a matrix with all the distances d(x,y) using the Floyd-Warshall algorithm -> O(N^3)
	// second step : calculate the smallest score S(x,y) using the matrix from step 1 -> O(N^3) again
	// this algorithm is a naive one : natural but sub-optimal
	
	public static void main(String[] args) throws Exception {
		

		File file = new File("C:/Users/Diane/eclipse-workspace/INF 421 project/tests/centers."+fileNum+".in");
		Tree tree = new Tree(file);
		
		//Step 1
		int[][] dist_matrix = tree.distance_matrix_FM();
		
		
		//Step 2: compare the weighted sums of the minimums of two columns of dist_matrix
		
		int x0 = 0;
		int y0 = 0;
		int S0 = 0;
		for (int k = 0; k< tree.N;k++) {
			S0 += tree.w[k]* Math.min(dist_matrix[k][0], dist_matrix[k][1]) ;
		}
		
		for (int x =0; x < tree.N ; x++) {
			for (int y=x+1; y< tree.N ; y++) {
				int S = 0;
				for (int k = 0; k< tree.N;k++) {
					S += tree.w[k]* Math.min(dist_matrix[k][x], dist_matrix[k][y]) ;
				}
				if (S< S0) {
					S0 = S;
					x0 = x;
					y0 = y;
				}
			}
		}
		
		x0 +=1; // to go back from the array numbering to the label of the vertex
		y0 +=1;
		System.out.println("c1:");
		System.out.println(x0);
		System.out.println("c2:");
		System.out.println(y0);
		System.out.println("S:");
		System.out.println(S0);
		

	  }
	

}
