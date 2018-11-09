package sam.ferg.wordcounter;

public class WordCounter {
	public static void main(String[] args) {
		WordList list = new WordList();
		list.add("Hello");
		list.add("hello");
		list.add("goodbye");
		list.sortByWordFrequency();
	}
}
