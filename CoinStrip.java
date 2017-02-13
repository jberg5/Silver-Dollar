/*
1) Flip a fair coin and initialize a coin variable at 3. If the coin lands on heads, stop flipping. Otherwise, increment the coin value by one and flip again. My underlying data structure would remain unchanged; it performs best (relative to alternative data structures) when the board is large but there are only a few coins.
2) I handled this, but you can guarantee a game is not an immediate win by initializing the first coin to position 2. You can guarantee n moves by incrementing the distance between n coins by 1 or more.
3) I can't tell what the strategy for this game might be, but an easy opportunity would be if there is only one move left to make; that is, all coins are aligned to the left except one.
4) Suppose also that there are two methods, optimalCoin() and optimalSpaces() that select a coin and move it, respectively. computerPlay() would call optimalSpaces(optimalCoin()) and then makeMove(). optimalSpaces would also use a modified version of checkMove() to determine the distance between the optimal coin and the coin before it (or the start of the board). Smarter, future me will solve the game such that the opportunity I defined in question 3 will be recognized by optimalSpaces and optimalCoin.
5) Yes, I would have to change the checkMove() method so that it only makes sure a move doesn't land on an occupied space or a space that doesn't exist (i.e. is off the board). 
 */

import java.util.Scanner;
import java.util.Random;

public class CoinStrip {
    private int[] strip;
    private int coins;
    private int length;

    public CoinStrip(int c, int l) {
	coins = c;
	length = l;
	strip = new int[c];
	Random rng = new Random();
	for (int i = 0; i < coins; i ++) {
	    int remainder = coins-i-1;
	    if (i == 0){
		strip[i] = rng.nextInt(length-remainder);
        	} else {
		//each subsequent coin is initialized such that strip[i] > strip[i-1], leaving enough room for future coins
		strip[i] = Math.max(1,(rng.nextInt(Math.max(1,length-strip[i-1]-remainder))))+strip[i-1];
            }
       }
    }

    public static void main(String args[]) {
	int maxLength = 20; //Change me to make the board larger or smaller! choose  maxLength > minLength
	int minLength = 5; 
	int minCoins = 3; //choose minCoins < minLength
	
	Scanner in = new Scanner(System.in);
	Random rng = new Random();

        int l = rng.nextInt(maxLength-minLength)+minLength;
        int c = rng.nextInt(l-minCoins)+minCoins;
	CoinStrip strip = new CoinStrip(c,l);
	
        boolean win = false;
        int player = 0;
	
	while (win == false) {
	    boolean move = false;
	    strip.printBoard();
	    while (move == false) {
		System.out.println("Player " + Integer.toString(player % 2) + " make your move!");
		System.out.println("Choose coin:");
		//absolute value handles negative inputs
		int coin = Math.abs(in.nextInt());
		System.out.println("How far?:");
		int distance = Math.abs(in.nextInt());
		if (strip.checkMove(coin,distance)) {
		    strip.makeMove(coin,distance);
		    move = true;
		    player += 1;
		    System.out.println("Great move!");
		} else {
		    System.out.println("That move is illegal! Try again");
		}
	    }
	    win = strip.checkWin();
	}
	strip.printBoard();
	System.out.println("Congrats player " + Integer.toString((player-1) % 2) + ", you win!");
    }



    public boolean checkMove(int coin,int distance) {
	if (coin > 0) {
	    return (strip[coin]-distance) > strip[coin-1];
	} else {
	    return (strip[coin] - distance) > - 1;
	}
    }

    public void makeMove(int coin,int distance) {
	strip[coin] -= distance;
    }

    public void printBoard() {
	String board = "";
	for(int i = 0; i < strip.length; i++) {
	    String spaces="";
	    String end="";
	    String u = "_";
	    if (i == 0) {
		//creates blank spaces to represent empty squares on the board
		for (int j = 0; j < strip[i]; j++) {
		    spaces += u;
		}
		board = board + spaces + "[" + Integer.toString(i) + "]";
		
	    } else if (i==strip.length-1) {
		for (int j = 0; j < strip[i]-strip[i-1]-1; j++) {
		    spaces += u;
		    
		}
		for (int j = 0; j < strip.length-strip[i]; j++) {
		    end += u;
		    
		}
	        board = board + spaces + "[" + Integer.toString(i) + "]" + end;
	    }
	    else {
		 for (int j = 0; j < strip[i]-strip[i-1]-1; j++) {
		     spaces += u;
		    
		 }
		  board = board + spaces + "[" + Integer.toString(i) + "]";
	    }
		
	}
	System.out.println(board);
    }

    public boolean checkWin() {
        boolean win = true;
	for (int i = 0; i <  strip.length; i++){
	    if (strip[i] != i){
		win = false;
	    }
	    /*
	    Useful for debugging:
	    System.out.println("Coin number " + Integer.toString(i) + " is at " + Integer.toString(strip[i]));
	    */   
	}
	return win;
    }
}
