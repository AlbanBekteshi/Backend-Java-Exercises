package be.vinci.pae.domain;

import java.util.Arrays;

public class Page {
	private int id;
	private String title;
	private String uri;
	private String content;
	private final static String[] POSSIBLE_PUBLICATION_STATUSES = {"hidden","published"};
	private String publicationStatus;
	private PublicUser author;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublicationStatus() {		
		return publicationStatus;
	}
	public void setPublicationStatus(String publicationStatus) {
		this.publicationStatus = Arrays.stream(POSSIBLE_PUBLICATION_STATUSES).filter(possibleStatus -> possibleStatus.equals(publicationStatus)).findFirst()
				.orElse(null);		
	}
	public PublicUser getAuthor() {
		return author;
	}
	public void setAuthor(PublicUser author) {
		this.author = author;
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
		Page other = (Page) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Page [id=" + id + ", title=" + title + ", uri=" + uri + ", content=" + content + ", publicationStatus="
				+ publicationStatus + ", author=" + author + "]";
	}
	
	

}
