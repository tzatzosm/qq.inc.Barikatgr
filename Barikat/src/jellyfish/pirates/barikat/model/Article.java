package jellyfish.pirates.barikat.model;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class Article implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6197976608011886218L;
	
	private String title;
	private String description;
	private String image;
	private String author;
	private String category;
	private String strPubDate;
	private Date date;
	
	@SuppressLint("SimpleDateFormat")
	public Article (JSONObject jArticle) throws Exception
	{
		this.title = jArticle.getString("title");
		this.description = jArticle.getString("Body");
		this.image = jArticle.getString("Slide");
		this.author = jArticle.getString("Authors");
		this.author = ReplaceQuotationFromHtml(this.author);
		this.category = jArticle.getString("category");
		this.strPubDate = jArticle.getString("Post date");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy - hh:mm");
        this.date = formatter.parse(this.strPubDate);
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStrPubDate() {
		return strPubDate;
	}

	public void setStrPubDate(String pubDate) {
		this.strPubDate = pubDate;
	}

	public boolean HasImage() {
		return this.image != null && this.image.length() > 0;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	private static String ReplaceQuotationFromHtml(String Input) {
		return Input.replace("&quot", "\"");
	}
	
}
