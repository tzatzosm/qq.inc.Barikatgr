package jellyfish.pirates.barikat.utilities;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jellyfish.pirates.barikat.model.Article;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLParser {

	private String xml;
	
	public XMLParser(String Xml){
		this.xml = Xml;
	}
	
	public ArrayList<Article> ParseXML() throws Exception
	{
		ArrayList<Article> articles = new ArrayList<Article>();
		
		Document doc = getDomElement();
		
		NodeList nl = doc.getElementsByTagName("item");
		
		for (int i=0;i<nl.getLength();i++) {
//			Article article = new Article();
//			article.setTitle(getValue((Element)nl.item(i), "title"));
//			article.setLink(getValue((Element)nl.item(i), "link"));
//			article.setDescription(HtmlUtils.FixHtml(getValue((Element)nl.item(i), "description")));
//			article.setDescriptionText(HtmlUtils.getDescriptionText(article.getDescription()));
//			article.setImage(HtmlUtils.getImage(article.getDescription()));
//			article.setPubDate(getValue((Element)nl.item(i), "pubDate"));
//			article.setCreator(getValue((Element)nl.item(i), "dc:creator"));
//			article.setGuid(getValue((Element)nl.item(i), "guid"));
//			article.setComments(getValue((Element)nl.item(i), "comments"));
//			articles.add(article);
		}

		return articles;
	}
	
	private Document getDomElement(){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);
		}catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
		return doc;
	}
	
	@SuppressWarnings("unused")
	private String getValue(Element item, String str)
	{
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}
	
	private String getElementValue(Node node)
	{
		Node child;
		if (node != null) {
			if (node.hasChildNodes()) {
				child = node.getFirstChild();
				while (child != null) {
					if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
					child = child.getNextSibling();
				}
			}
		}
		return "";
	}
	
	
	
}
