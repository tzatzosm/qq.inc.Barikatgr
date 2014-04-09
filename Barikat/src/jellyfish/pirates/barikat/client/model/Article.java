package jellyfish.pirates.barikat.client.model;

import java.util.Date;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName="Articles")
public class Article {
	
	@DatabaseField(generatedId=true)
	int ID;
	
	@DatabaseField()
	String Description;
	
	@DatabaseField()
	String Image;
	
	@DatabaseField()
	String Author;
	
	//categoryURL will be used as key here
	@DatabaseField()
	String Category;
	
	@DatabaseField()
	Date PubDate;
	
	public Article() { }
	
	public Article(jellyfish.pirates.barikat.model.Article Article)
	{
		this.Description = Article.getDescription();
		this.Image = Article.getImage();
		this.Author = Article.getAuthor();
		this.Category = Article.getCategory();
		String date = Article.getStrPubDate();
		Log.d("Date", date);
	}
	
	
	
}
