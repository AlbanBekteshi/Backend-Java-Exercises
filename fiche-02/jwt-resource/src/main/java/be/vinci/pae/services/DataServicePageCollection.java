package be.vinci.pae.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.Page;
import be.vinci.pae.domain.PublicUser;
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
		return pages.stream().filter(item -> (item.getId() == id)
				&& (item.getPublicationStatus().contentEquals("published") ))
				.findAny().orElse(null);
	}

	public static Page getPage(int id, User user) {
		PublicUser currentUser = new PublicUser(user);
		return pages.stream().filter(item -> (item.getId() == id)
				&& (item.getPublicationStatus().contentEquals("published") || item.getAuthor().equals(currentUser)))
				.findAny().orElse(null);
	}

	public static List<Page> getPages() {
		return pages.stream().filter(item -> item.getPublicationStatus().contentEquals("published"))
				.collect(Collectors.toList());
	}

	public static List<Page> getPages(User user) {
		PublicUser currentUser = new PublicUser(user);
		return pages.stream().filter(item -> item.getPublicationStatus().contentEquals("published")
				|| (item.getAuthor().equals(currentUser))).collect(Collectors.toList());
	}

	public static Page addPage(Page page, User user) {
		page.setId(nexPageId());
		// escape dangerous chars to protect against XSS attacks
		page.setTitle(StringEscapeUtils.escapeHtml4(page.getTitle()));
		page.setUri(StringEscapeUtils.escapeHtml4(page.getUri()));
		page.setContent(StringEscapeUtils.escapeHtml4(page.getContent()));
		page.setAuthor(new PublicUser(user));
		pages.add(page);
		saveDataToFile();
		return page;
	}

	public static int nexPageId() {
		if (pages.size() == 0)
			return 1;
		return pages.get(pages.size() - 1).getId() + 1;
	}

	public static Page deletePage(int id, User user) {
		if (pages.size() == 0 | id == 0)
			return null;
		Page pageToDelete = getPage(id, user);
		if (pageToDelete == null)
			return null;
		int index = pages.indexOf(pageToDelete);
		pages.remove(index);
		saveDataToFile();
		return pageToDelete;
	}

	public static Page updatePage(Page page, User user) {
		if (pages.size() == 0 | page == null)
			return null;
		Page pageToUpdate = getPage(page.getId(), user);
		if (pageToUpdate == null)
			return null;
		// escape dangerous chars to protect against XSS attacks
		if (page.getTitle() != null)
			pageToUpdate.setTitle(StringEscapeUtils.escapeHtml4(page.getTitle()));
		if (page.getUri() != null)
			pageToUpdate.setUri(StringEscapeUtils.escapeHtml4(page.getUri()));
		if (page.getContent() != null)
			pageToUpdate.setContent(StringEscapeUtils.escapeHtml4(page.getContent()));
		// set the author
		if (page.getAuthor() != null)
			pageToUpdate.setAuthor(pageToUpdate.getAuthor());
		// update the data structure
		int index = pages.indexOf(pageToUpdate);
		pages.set(index, pageToUpdate);
		saveDataToFile();
		return pageToUpdate;
	}

	private static List<Page> loadDataFromFile() {
		try {
			JsonNode node = jsonMapper.readTree(Paths.get(DB_FILE_PATH).toFile());
			JsonNode collection = node.get(COLLECTION_NAME);
			if (collection == null)
				return new ArrayList<Page>();
			return jsonMapper.readerForListOf(Page.class).readValue(node.get(COLLECTION_NAME));

		} catch (FileNotFoundException e) {
			return new ArrayList<Page>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Page>();
		}
	}

	private static void saveDataToFile() {
		try {

			// get all collections
			Path pathToDb = Paths.get(DB_FILE_PATH);
			if (!Files.exists(pathToDb)) {
				// write a new collection to the db file
				ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(COLLECTION_NAME, pages);
				jsonMapper.writeValue(pathToDb.toFile(), newCollection);
				return;

			}
			// get all collections
			JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

			if (allCollections.has(COLLECTION_NAME)) {// remove current collection
				((ObjectNode) allCollections).remove(COLLECTION_NAME);
			}

			// create a new JsonNode and add it to allCollections
			String currentCollectionAsString = jsonMapper.writeValueAsString(pages);
			JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
			((ObjectNode) allCollections).putPOJO(COLLECTION_NAME, updatedCollection);

			// write to the db file
			jsonMapper.writeValue(pathToDb.toFile(), allCollections);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
