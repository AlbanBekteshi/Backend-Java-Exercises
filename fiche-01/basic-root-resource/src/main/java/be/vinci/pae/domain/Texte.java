package be.vinci.pae.domain;

public class Texte {
	
	private int id;
	private String content;
	public static String [] LEVELS = {"easy","medium","hard"};
	private String level;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
		return "Texte [id=" + id + ", content=" + content +", level="+level +"]";
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Texte other = (Texte) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}

