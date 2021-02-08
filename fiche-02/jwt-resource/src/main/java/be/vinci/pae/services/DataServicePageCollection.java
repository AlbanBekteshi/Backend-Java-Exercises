package be.vinci.pae.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.vinci.pae.domain.Page;
import be.vinci.pae.domain.User;

public class DataServicePageCollection {
	private static final String DB_FILE_PATH = "db.json";
	private static final String COLLECTION_NAME = "pages";
	private final static ObjectMapper jsonMapper = new ObjectMapper();
	
	private static List<Page> pages;
	static {
		pages = loadDataFromFile();
	}
	
	public static Page getPage(int id) {
		return pages.stream().filter(item -> item.getId()== id).findAny().orElse(null);
	}
	
	public static List<Page> getUserPages(int id){
		return pages.stream().filter(item -> item.getAuteur().getID()==id).collect(Collectors.toList());
	}
	
	public static List<Page> getPublishedPages(){
		return pages.stream().filter(item -> item.getStatut().equals("published")).collect(Collectors.toList());
	}
	
	public static Page addPage(Page page, User user) {
		page.setId(nexPageId());
		page.setContenu(StringEscapeUtils.escapeHtml4(page.getContenu()));
		page.setStatut("hidden");
		page.setTitre(StringEscapeUtils.escapeHtml4(page.getTitre()));
		page.setAuteur(user);
		pages.add(page);
		saveDataToFile();
		return page;
		
	}
	
	public static Page deletePage(int id) {
		if(pages.size())
	}
	
	

	private static int nexPageId() {
		if (pages.size() == 0)
			return 1;
		return pages.get(pages.size() - 1).getId() + 1;
	}

	private static List<Page> loadDataFromFile() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static void saveDataToFile() {
		// TODO Auto-generated method stub
		
	}

}
