package be.vinci.pae.domain;

import java.util.Arrays;

public class Page {
	private int id;
	private String titre;
	private String contenu;
	private User auteur;
	private final static String[] POSSIBLE_STATUTS = {"hidden","published"};
	private String statut;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public User getAuteur() {
		return auteur;
	}
	
	public void setAuteur(User auteur) {
		this.auteur = auteur;
	}
	public String getStatut() {
		return statut;
	}
	
	public void setStatut(String statut) {
		this.statut = Arrays.stream(POSSIBLE_STATUTS).filter(possibleStatuts -> possibleStatuts.equals(statut)).findFirst().orElse(null);	
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
	
	
	
	

}
