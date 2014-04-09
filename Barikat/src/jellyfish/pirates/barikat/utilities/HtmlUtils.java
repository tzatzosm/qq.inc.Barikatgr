package jellyfish.pirates.barikat.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtils {
	
	public static String getImage(String Html) {
		Document doc = Jsoup.parse(Html);
		Element link = doc.select("img").first();
		String image = link.attr("src");
		return image;
	}
	
	public static String getDescriptionText(String Html) {
		Document doc = Jsoup.parse(Html);
		String text = doc.body().text();
		return text;
	}
	
	public static String FixHtml(String Html) {
		Document doc = Jsoup.parse(Html);
		Element link = doc.select("div").first();
		link.attr("width", "100%");
		Elements imageElements = doc.select("img");
		for (Element element : imageElements) {
			element.attr("width", "100%");
			element.attr("style", "font-size:small");
		}
		return doc.toString();
	}
	
	
	
}
