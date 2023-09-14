import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SolutionRunner {
	
	public static void main(String[] args) throws FileNotFoundException {
		Set<String> words = new HashSet<>();
		Scanner scan = new Scanner(new File("scrabble.txt"));
		while (scan.hasNextLine()) {
			String word = scan.nextLine();
			words.add(word);
		}
		
		WordHuntTrie test = new WordHuntTrie(words);
		test.addWords();
		
		test.loadBoard();
		test.getSolutions();
		
	}

}
