package jellyfish.pirates.barikat.ui;

import java.util.ArrayList;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.adapter.ArticlesListAdapter;
import jellyfish.pirates.barikat.model.Article;
import jellyfish.pirates.barikat.model.ArticleParser;
import jellyfish.pirates.barikat.model.ArticleParser.IArticleParseListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class FragmentArticleList extends Fragment implements
		AdapterView.OnItemClickListener {

	//region Listeners
	
	public interface IFragmentArticleListListener {
		public void OnArticleSelected(ArrayList<Article> articles, Article article);
	}
	
	//endregion

	// region Properties

	private static final String s_volley_tag = "VolleyPatterns";
	
	private String _rssUrl;
	private IFragmentArticleListListener _listener;

	private Context _con;
	private RequestQueue _queue = null;
	private ListView _lv = null;
	private boolean _firstTime = true;
	
	private ArrayList<Article> _articles = new ArrayList<Article>();
	private ArticlesListAdapter _la = null;

	private int _currentPage = 0;
	
	// endregion

	// region Static Constructor

	public static FragmentArticleList newInstance(String RssUrl,
			IFragmentArticleListListener Listener) {
		FragmentArticleList fragment = new FragmentArticleList();
		fragment._rssUrl = RssUrl;
		fragment._listener = Listener;
		return fragment;
	}

	// endregion

	// region Overrides

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_article_list, null);
		//Initializing View And List Adapter Here
		_lv = (ListView) v.findViewById(R.id.fragment_article_list_lv);
		_la = new ArticlesListAdapter(getActivity());
		_lv.setAdapter(_la);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_con = getActivity();
		_queue = Volley.newRequestQueue(getActivity());		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("Lc", "OnStart");
		_lv.setOnItemClickListener(this);
	}
	
	@Override
	public void onResume() {
		Log.d("Lc", "OnResume");
		super.onResume();
		if (_firstTime) {
			GetArticles(_currentPage);
			_firstTime = false;
		}else {
			_la = new ArticlesListAdapter(_con);
			_la.AddArticles(_articles);
			_la.notifyDataSetChanged();
			_lv.setAdapter(_la);
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (_queue != null) {
			_queue.cancelAll(s_volley_tag);
		}
		this._la.setLoadingMore(false);
		this._la.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("Lc", "onActivityCreated");
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("Articles")) {
			_articles = (ArrayList<Article>) savedInstanceState
					.getSerializable("Articles");
			_firstTime = savedInstanceState.getBoolean("FirstTime");
			_currentPage = savedInstanceState.getInt("CurrentPage");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d("Lc", "SaveInstanceState");
		if (_articles != null && _articles.size() > 0) {
			outState.putSerializable("Articles", _articles);
			outState.putBoolean("FirstTime", _firstTime);
			outState.putInt("CurrentPage", _currentPage);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d("Lc", "OnStop");
	}
	

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		if (this._listener != null) {
			if (id == Long.MAX_VALUE) {
				GetArticles(_currentPage);
				return;
			}
			if (this._la != null) {
				Article article = (Article) this._la.getItem(position);
				this._listener.OnArticleSelected(_articles, article);
			}
		}
	}

//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		if (firstVisibleItem > _prevFirstItem) {
//			_flagGoingDown = true;
//		} else {
//			_flagGoingDown = false;
//		}
//		boolean shouldLoadMore = totalItemCount != 0
//				&& totalItemCount - firstVisibleItem - visibleItemCount < 1;
//		if (_flagGoingDown && shouldLoadMore) {
//			if (_la != null && !_la.isLoadingMore()) {
//				_la.setLoadingMore(true);
//				_la.notifyDataSetChanged();
//				GetArticles(_currentPage);
//			}
//		}
//	}

//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//	}

	// endregion

	// region Methods

	private void GetArticles(int page) {
		this._la.setLoadingMore(true);
		String url = PrepareURL(page);
		StringRequest request = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						ArticleParser parser = new ArticleParser(response,
								new IArticleParseListener() {
									@Override
									public void OnResponse(
											ArrayList<Article> Articles) {
										_articles.addAll(Articles);
										
										//Set The Loading More View To Load No More
										AddArticles(Articles);
										_currentPage = _currentPage + 1;
									}

									@Override
									public void OnError(Exception ex) {
										//Set The Loading More View To Load No More
										Toast.makeText(_con, ex.getMessage(),
												Toast.LENGTH_LONG).show();
									}
								});
						parser.execute("");
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				});
		request.setTag(s_volley_tag);
		_queue.add(request);
		_queue.start();
	}

	// Prepares The URL Based on
	// the _currentPage variable
	private String PrepareURL(int page) {
		String url = _rssUrl.concat(page + "");
		return url;
	}

	private void AddArticles(ArrayList<Article> Articles) {
		_la.AddArticles(Articles);
	}

	// endregion

}
