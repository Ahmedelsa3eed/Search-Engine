package searchEngine;

import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;

import BTree.BTree;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import org.w3c.dom.Document; 
import java.io.File;
import java.util.HashMap;

public class XMLParser {

    public HashMap<String, BTree> readXML(String path) {

		HashMap<String, BTree> contents = new HashMap<>(); // the documents saved in B-trees
		try {
			
			// open XML file
			File wikiDoc = new File(path);
		
			// build the parser
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(wikiDoc);
		
			// list of all documents in the file
			NodeList nodes = doc.getElementsByTagName("doc");
		
			// go through all the documents in the file to save it
			for(int i = 0; i < nodes.getLength(); i++) {
				
				// get docs one by one
				Node node = nodes.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element data = (Element) node;
					
					//get the id of the doc
					String id = data.getAttribute("id");
					
					//get the context of the doc
					String context = data.getTextContent();

					//turn the context into array of strings to save it word by word in the tree
					String[] contextWords = context.split("\\s");

					//create new B-tree for each new documentation
					BTree btree = new BTree(4);

					//save the words into the tree
					for (String contextWord : contextWords) {
						btree.insert(contextWord.toLowerCase(), "");
					}
					
					//save the tree
					contents.put(id, btree);
				}
			}
			return contents;
		}
		catch(Exception e) {
			System.out.println("couldn't find or open the file");
			e.printStackTrace();
			return null;
		}
	}
}
