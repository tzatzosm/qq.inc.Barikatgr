package jellyfish.pirates.barikat.model;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ArticleParser extends AsyncTask<String, Void, ArrayList<Article>>{

	public interface IArticleParseListener
	{
		public void OnResponse(ArrayList<Article> Articles);

		public void OnError (Exception ex);
	}
	
	private String _response;
	
	private Exception _exception;
	
	private IArticleParseListener _listener;
	
	public ArticleParser(String Response, IArticleParseListener Listener) {
		this._response = Response;
		this._listener = Listener;
	}

	@Override
	protected ArrayList<Article> doInBackground(String... params) {
		ArrayList<Article> articles = new ArrayList<Article>();
		try {
			JSONArray jarray = new JSONArray(this._response);
			for (int i=0;i<jarray.length();i++) {
				JSONObject jobj = jarray.getJSONObject(i);
				Article article = new Article(jobj);
				articles.add(article);
			}
		}catch (Exception ex) {
			_exception = ex;
		}
		
		return articles;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Article> result) {
		super.onPostExecute(result);
		if (this._exception != null) {
			if (this._listener != null) {
				this._listener.OnError(this._exception);
				return;
			}
		}
		if (this._listener != null) {
			this._listener.OnResponse(result);
		}
	}
}
