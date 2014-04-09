package jellyfish.pirates.barikat.client.model;

import com.j256.ormlite.field.DatabaseField;

public class Category {

	@DatabaseField(unique=true)
	String ID;
	
	@DatabaseField()
	String Description;
	
	
	
}
