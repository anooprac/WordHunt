import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class WordHuntTrie {

	private Set<String> words;
	private TrieNode<Character> head;
	private char[][] wordMap;
	private final int ROWS = 4;
	private final int[][] MOVES = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 },
			{ 1, 1 }, { 1, 0 }, { 1, -1 } };

	public WordHuntTrie(Set<String> words) {
		head = new TrieNode<>(null);
		this.words = Set.copyOf(words);
		wordMap = new char[ROWS][ROWS];
	}

	public void addWords() {
		for (String word : words) {
			word = word.toLowerCase();
			TrieNode<Character> track = head;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				track = addHelp(c, track);
			}
			track.complete = true;
		}

	}

	private TrieNode<Character> addHelp(char c, TrieNode<Character> track) {
		if (track == null) {
			TrieNode<Character> val = new TrieNode<>(c);
			return val;
		} else {
			TrieNode<Character> child = track.containsVal(c);
			if (child == null) {
				track.children.add(new TrieNode<>(c));
			}
			return track.containsVal(c);
		}
	}

	public void getWords() {
		for (int i = 0; i < head.children.size(); i++) {
			getWordsHelp(head.children.get(i), "");
		}
	}

	private void getWordsHelp(TrieNode<Character> trieNode, String s) {
		s = s + trieNode.val;
		if (trieNode.complete) {
			System.out.println(s);
		}
		for (int i = 0; i < trieNode.children.size(); i++) {
			getWordsHelp(trieNode.children.get(i), s);
		}
		s = s.substring(0, s.length() - 1);
	}

	public void loadBoard(char[] letters) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				wordMap[i][j] = (char)(letters[4 * i + j] + 32);
			}
		}
	}

	public ArrayList<MoveHistory> getSolutionsOrganized() {
		ArrayList<MoveHistory> solutions = new ArrayList<>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				MoveHistory temp = new MoveHistory();
				boolean[][] beenHere = new boolean[ROWS][ROWS];
				TrieNode<Character> track = head.containsVal(wordMap[i][j]);
				if (track != null) {
					getSolutionOrganized(i, j, beenHere, solutions, track, temp);
				}
			}
		}
		Collections.sort(solutions);
		return solutions;
	}
	
	public Set<String> getSolutions() {
		Set<String> solutions = new HashSet<>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				String temp = "";
				boolean[][] beenHere = new boolean[ROWS][ROWS];
				TrieNode<Character> track = head.containsVal(wordMap[i][j]);
				if (track != null) {
					getSolution(i, j, beenHere, solutions, track, temp);
				}
			}
		}
		return solutions;
	}
	
	private void getSolution(int i, int j, boolean[][] beenHere,
			Set<String> solutions, TrieNode<Character> track,
			String temp) {
		temp = temp + wordMap[i][j];
		beenHere[i][j] = true;
		if (track.complete) {
			if (!solutions.contains(temp)) {
				solutions.add(temp);
			}
		}
		for (int k = 0; k < MOVES.length; k++) {
			int newRow = i + MOVES[k][0];
			int newCol = j + MOVES[k][1];
			if (inBounds(newRow, newCol) && !beenHere[newRow][newCol]) {
				char val = wordMap[newRow][newCol];
				TrieNode<Character> cand = track.containsVal(val);
				if (cand != null) {
					getSolution(newRow, newCol, beenHere, solutions, cand, temp);
				}

			}

		}
		temp = temp.substring(0, temp.length() - 1);
		beenHere[i][j] = false;
	}

	private void getSolutionOrganized(int i, int j, boolean[][] beenHere,
			ArrayList<MoveHistory> solutions, TrieNode<Character> track,
			MoveHistory temp) {
		temp.addChar(wordMap[i][j], i, j);
		beenHere[i][j] = true;
		if (track.complete) {
			if (!solutions.contains(temp)) {
				solutions.add(new MoveHistory(temp));
			}
		}
		for (int k = 0; k < MOVES.length; k++) {
			int newRow = i + MOVES[k][0];
			int newCol = j + MOVES[k][1];
			if (inBounds(newRow, newCol) && !beenHere[newRow][newCol]) {
				char val = wordMap[newRow][newCol];
				TrieNode<Character> cand = track.containsVal(val);
				if (cand != null) {
					getSolutionOrganized(newRow, newCol, beenHere, solutions, cand, temp);
				}

			}

		}
		temp.removeLast();
		beenHere[i][j] = false;
	}

	private boolean inBounds(int newRow, int newCol) {
		if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= ROWS) {
			return false;
		}
		return true;
	}

	private class TrieNode<E extends Comparable<? super E>> {
		private E val;
		private boolean complete;
		private ArrayList<TrieNode<E>> children;

		public TrieNode(E val) {
			complete = false;
			children = new ArrayList<>();
			this.val = val;
		}

		private TrieNode<E> containsVal(E data) {
			for (int i = 0; i < children.size(); i++) {
				TrieNode<E> temp = children.get(i);
				if (temp.val.equals(data)) {
					return temp;
				}
			}
			return null;
		}
	}

	private class MoveHistory implements Comparable<MoveHistory> {
		private String word;
		private ArrayList<int[]> places;

		public MoveHistory() {
			word = new String();
			places = new ArrayList<>();
		}
		
		public MoveHistory(MoveHistory copy) {
			word = copy.word;
			places = new ArrayList<>(copy.places);
		}

		public void addChar(char c, int row, int col) {
			word = word + c;
			int[] move = new int[] { row, col };
			places.add(move);
		}

		public void removeLast() {
			word = word.substring(0, word.length() - 1);
			places.remove(places.size() - 1);
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(word);
			sb.append("\n");
			for (int i = 0; i < places.size(); i ++) {
				sb.append(Arrays.toString(places.get(i)));
				sb.append(", ");
			}
			return sb.toString();
		}

		@Override
		public int compareTo(MoveHistory o) {
			return  this.word.length() - o.word.length();
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof MoveHistory)) {
				return false;
			}
			MoveHistory oth = (MoveHistory) o;
			return oth.word.equals(word);
		}

	}

}
