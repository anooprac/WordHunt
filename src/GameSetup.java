import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameSetup {
	
	final char[] cube0 = {'A', 'A', 'E', 'E', 'G', 'N'};
	final char[] cube1 = {'A', 'B', 'B', 'J', 'O', 'O'};
	final char[] cube2 = {'A', 'C', 'H', 'O', 'P', 'S'};
	final char[] cube3 = {'A', 'F', 'F', 'K', 'P', 'S'};
	final char[] cube4 = {'A', 'O', 'O', 'T', 'T', 'W'};
	final char[] cube5 = {'C', 'I', 'M', 'O', 'T', 'U'};
	final char[] cube6 = {'D', 'E', 'I', 'L', 'R', 'X'};
	final char[] cube7 = {'D', 'E', 'L', 'R', 'V', 'Y'};
	final char[] cube8 = {'D', 'I', 'S', 'T', 'T', 'Y'};
	final char[] cube9 = {'E', 'E', 'G', 'H', 'N', 'W'};
	final char[] cube10 = {'E', 'E', 'I', 'N', 'S', 'U'};
	final char[] cube11 = {'E', 'H', 'R', 'T', 'V', 'W'};
	final char[] cube12 = {'E', 'I', 'O', 'S', 'S', 'T'};
	final char[] cube13 = {'E', 'L', 'R', 'T', 'T', 'Y'};
	final char[] cube14 = {'H', 'I', 'M', 'N', 'Q', 'U'};
	final char[] cube15 = {'H', 'L', 'N', 'N', 'R', 'Z'};
	ArrayList<Integer> order;
	final char[][] cubes = {cube0, cube1, cube2, cube3, cube4, cube5, cube6, cube7, cube8, cube9, cube9, cube10,
			cube11, cube12, cube13, cube14, cube15};
	char[] letters;
	
	public GameSetup() {
		System.out.println("Hello");
		this.order = new ArrayList<>();
		for (int i = 0; i < 16; i ++) {
			order.add(i);
		}
		Collections.shuffle(order);
		this.letters = new char[16];
	}
	
	public void setLetters() {
		for (int i = 0; i < 16; i ++) {
			int randIndex = (int) (Math.random() * 6);
			this.letters[i] = cubes[order.get(i)][randIndex];
		}
		System.out.println(Arrays.toString(this.letters));
	}
	
	public char[] getLetters() {
		return this.letters;
	}
	
	
	

}
