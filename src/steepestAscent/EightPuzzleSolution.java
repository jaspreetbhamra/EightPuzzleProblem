package steepestAscent;

import java.util.Scanner;

public class EightPuzzleSolution {

    //Possible heuristic functions:
    //h1: number of misplaced tiles
    //h2: total manhattan distance of all tiles from their goal positions

    public static TileConfig goalState;
    public static String [] goalPositions;
    public static final int SIZE_OF_BOARD = 3;

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the initial configuration (state) of the puzzle tiles\nThe numbers should be in a range of 1 to 8");
        System.out.println("Enter -1 to indicate the position of the blank location");
        TileConfig tc = new TileConfig(new int[SIZE_OF_BOARD][SIZE_OF_BOARD], new int[2]);
        for (int i = 0; i < SIZE_OF_BOARD; i++)
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                tc.tileConfig[i][j] = sc.nextInt();
                if(tc.tileConfig[i][j] == -1){
                    tc.blankLocation[0] = i;
                    tc.blankLocation[1] = j;
                }
            }
        populateGoalStateConfigurationArrays();
        tc.setCost(calculateManhattanDistance(tc));
	    solve(tc);
    }

    public static void solve(TileConfig tc /*, int costOfParent*/){
        //Possible moves:
        //The function has been implemented in an iterative manner instead of in a recursive manner as nothing being
        // returned by subcalls is to be used in the parent call
        int stepsRequired = 0;
        while(true){
	        System.out.println("***** " + stepsRequired +  " *****");
	        printTileConfig(tc);
	        System.out.println("**********");
	        if(checkIfGoalState(tc)) {
	            System.out.println("Goal state found");
	            break;
            } else if(stepsRequired>1000){
	            System.out.println("Solution not found even after 1000 steps");
	            return;
            }
            else{
	            TileConfig moveUp, moveDown, moveLeft, moveRight;
                moveUp = moveTiles(tc, -1, 0);
                moveDown = moveTiles(tc, 1, 0);
                moveLeft = moveTiles(tc, 0, -1);
                moveRight = moveTiles(tc, 0, 1);
                TileConfig nextStep = null;
                nextStep = findMin(moveUp, nextStep);
                nextStep = findMin(moveDown, nextStep);
                nextStep = findMin(moveLeft, nextStep);
                nextStep = findMin(moveRight, nextStep);
                if(tc.getCost()>nextStep.getCost())
                    tc = nextStep;
                else {
	                System.out.println("The algorithm encountered a local minima after "+stepsRequired+" steps");
	                return;
                }
                stepsRequired++;
            }
        }
	    System.out.println("The number of steps required to solve the given problem is " + stepsRequired);
    }

    public static void printTileConfig(TileConfig tc){
    	for(int i=0; i<SIZE_OF_BOARD; ++i) {
		    for (int j = 0; j < SIZE_OF_BOARD; ++j)
		    	if(tc.tileConfig[i][j] == -1)
				    System.out.print("* ");
		        else
		            System.out.print(tc.tileConfig[i][j] + " ");
		    System.out.println();
	    }
    }

    public static boolean checkIfGoalState(TileConfig tc){
    	for(int i=0; i<SIZE_OF_BOARD; ++i)
    		for(int j=0; j<SIZE_OF_BOARD; ++j)
    			if(tc.tileConfig[i][j] != goalState.tileConfig[i][j])
    				return false;
    	return true;
    }

    public static TileConfig findMin(TileConfig tc, TileConfig minSoFar){
        if(tc==null && minSoFar==null)
            return null;
        if(tc==null)
            return minSoFar;
        if(minSoFar==null)
        	return tc;
        if(tc.getCost()<minSoFar.getCost())
            return tc;
        return minSoFar;
    }

    public static TileConfig moveTiles(TileConfig tc, int x, int y){
        /*
        x and y will take values as follows:
            -1 0 => move up
            1 0 => move down
            0 -1 => move left
            0 1 => move right
         */
        TileConfig temp = new TileConfig(new int[SIZE_OF_BOARD][SIZE_OF_BOARD], new int[SIZE_OF_BOARD]);
        for(int i=0; i<SIZE_OF_BOARD; ++i)
        	for(int j=0; j<SIZE_OF_BOARD; ++j)
        	    temp.tileConfig[i][j] = tc.tileConfig[i][j];
        temp.blankLocation[0] = tc.blankLocation[0];
        temp.blankLocation[1] = tc.blankLocation[1];
	    if((temp.blankLocation[0]+x>(SIZE_OF_BOARD-1) || temp.blankLocation[0]+x<0) || (temp.blankLocation[1]+y>(SIZE_OF_BOARD-1) || temp.blankLocation[1]+y<0)){
            temp = null;
        } else{
            temp.tileConfig[temp.blankLocation[0]][temp.blankLocation[1]] = temp.tileConfig[temp.blankLocation[0]+x][temp.blankLocation[1]+y];
		    temp.blankLocation[0] += x;
		    temp.blankLocation[1] += y;
		    temp.tileConfig[temp.blankLocation[0]][temp.blankLocation[1]] = -1;
		    temp.setCost(calculateManhattanDistance(temp));
        }
        return temp;
    }

    public static int calculateManhattanDistance(TileConfig tc){
        //If manhattan distance for tc = 0 => goal state reached
        int cost = 0, k ;
        for(int i=0; i<SIZE_OF_BOARD; ++i){
            for(int j=0; j<SIZE_OF_BOARD; ++j){
            	k = tc.tileConfig[i][j];
            	if(k == -1)
            		k = 0;
                int x = (int) goalPositions[k].charAt(0) - 48;
                int y = (int) goalPositions[k].charAt(2) - 48;
//                if(x!=i && y!=j){
                    cost += Math.abs(i-x) + Math.abs(j-y);
//                }
            }
        }
        return cost;
    }

    public static void populateGoalStateConfigurationArrays(){
        goalState = new TileConfig(new int[SIZE_OF_BOARD][SIZE_OF_BOARD], new int[2]);
        goalState.tileConfig[0][0] = -1; goalState.tileConfig[0][1] = 1; goalState.tileConfig[0][2] = 2;
        goalState.tileConfig[1][0] = 3; goalState.tileConfig[1][1] = 4; goalState.tileConfig[1][2] = 5;
        goalState.tileConfig[2][0] = 6; goalState.tileConfig[2][1] = 7; goalState.tileConfig[2][2] = 8;
        goalState.blankLocation[0] = 0; goalState.blankLocation[1] = 0;

        goalPositions = new String[9];                                                  //goalPositions[0] => location of blank
        goalPositions[0] = "0,0"; goalPositions[1] = "0,1"; goalPositions[2] = "0,2";
        goalPositions[3] = "1,0"; goalPositions[4] = "1,1"; goalPositions[5] = "1,2";
        goalPositions[6] = "2,0"; goalPositions[7] = "2,1"; goalPositions[8] = "2,2";
    }

}
