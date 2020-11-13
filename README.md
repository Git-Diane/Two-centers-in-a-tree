# Two-centers-in-a-tree

*** HOW TO RUN ***

To run any algorithm (Goldman, Greedy, LinkDeletion or twoCentersFloydWharshall):
- open the corresponding class
- in line "public static int fileNum = ..;" (one of the first lines of the class):
replace what is in "..." by the number of the tree you want to test (1 to 10)
- run the class

***FINAL SOLUTIONS***

We present two algorithms as final solutions : Greedy and LinkDeletion.
Both of them have their outputs stored in specific files :
"outputGreedy" and "outputLinkDeletion"


- "Greedy" is a greedy algorithm that starts by finding the diameter of the tree (longest path) using two consecutive BFS;
then starts with c1 and c2 adjacent, in the middle of the diameter;
then updates c1 and/or c2 to one of their neighbors, choosing (x,y) such that :
x is c1 or one of its neighbors
y is c2 or one of its neighbors
and S(x,y) is the lowest possible
When it is not possible to decrease the score S anymore, we stop and return c1 and c2

This algorithm is not always correct (maximum error 9% on the test trees) 
but it runs extremly fast (in a couple of seconds)

- "LinkDeletion" : gives exact solution but runs slower (complexity O(N^2))



***INTERMEDIARY STEPS IN OUR REFLEXION - Just for information if you wish to take a look***

- class "TwoCentersFloydWarshall" implements a naive algorithm in time O(N^3):
computes a distance matrix using Floyd Warhsall algorithm  (complexity O(N^3));
which it uses to compute the score for every possible pair of centers (complexity O(N^3))
It does not run (too slow) for tests 5-10

- class "Goldman" finds the 1-center of the tree (complexity O(N))
It is the basis of method "goldman" in class "LinkDeletion", which uses Goldman's algorithm on subtrees of the tree


