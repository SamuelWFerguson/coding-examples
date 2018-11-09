package sam.ferg.wordcounter;

public class Word implements Comparable<Word>{
	
	private String word;

	public Word(String string) {
		setWord(string);
	}

	@Override
	public int compareTo(Word otherWord) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		word = word.toLowerCase();
		this.word = word;
	}

}
