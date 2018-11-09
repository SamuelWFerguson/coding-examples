package sam.ferg.wordcounter;

import java.util.*;

public class WordList {
	
	private ArrayList<Word> list;
	
	public WordList() {
		list = new ArrayList<Word>();
	}
	
	public void add(Word word) {
		list.add(word);
	}
	
	public void add(String string) {
		list.add(new Word(string));
	}
	
	public void sortByWordFrequency() {
		Collections.sort(list, new WordFrequencyComparator());
	}
}
