package jellyfish.pirates.barikat.ui;

import java.text.SimpleDateFormat;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.model.Article;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

@SuppressLint("SimpleDateFormat")
public class FragmentArticleDetail extends Fragment {

	//region Properties
	
	private Article _article;
	
	private TextView _tvTitle;
	private TextView _tvAuthor;
	private TextView _tvDate;
	private ImageView _imv;
	private WebView _wv;
	
	private static int default_textSize = 14;
	private int current_textSize = default_textSize;
	
	//endregion
	
	//region Static Constructor
	
	public static FragmentArticleDetail newInstance(Article article) {
		FragmentArticleDetail fragment = new FragmentArticleDetail();
		fragment._article = article;
		fragment.setHasOptionsMenu(true);
		return fragment;
	}
	
	//endregion
	
	//region Overrides
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_article_detail_old, null);
		this._tvTitle = (TextView) view.findViewById(R.id.fragment_article_detail_title);
		this._tvAuthor = (TextView) view.findViewById(R.id.fragment_article_detail_author);
		this._tvDate = (TextView) view.findViewById(R.id.fragment_article_detail_date);
		this._imv = (ImageView) view.findViewById(R.id.fragment_article_detail_image);
		this._wv = (WebView) view.findViewById(R.id.fragment_article_detail_webview);
		setHasOptionsMenu(true);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (_article != null) {
			if (_article.getTitle() != null) {
				_tvTitle.setText(_article.getTitle());
			}
			if (_article.getAuthor() != null) {
				_tvAuthor.setText(_article.getAuthor());
			}
			if (_article.getDate() != null) {
				SimpleDateFormat df = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
				String strDate = df.format(_article.getDate());
				_tvDate.setText(strDate);
			}
			if (_article.getImage() != null) {
				Picasso.with(getActivity()).load(_article.getImage()).into(_imv);
			}
			if (_article.getDescription() != null) {
				LoadWebViewContent(_article.getDescription());
			}
		}
	}
	
	private void LoadWebViewContent(String Content)
	{
		String html = "<html><head></head><meta name=\"viewport\" content=\"width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/><body style=\text-align:justify\"> %s </body></Html>";
		this._wv.loadDataWithBaseURL(null, String.format(html, Content), "text/html", "utf-8", null);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_article_detailed, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_font_plus:
			FontPlus(+2);
			break;
		case R.id.action_font_minus:
			FontPlus(-2);
			break;
		case R.id.action_share:
			Toast.makeText(getActivity(), "Share Clicked", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_beam:
			Toast.makeText(getActivity(), "Beam Clicked", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_reverse_colors:
			Toast.makeText(getActivity(), "Reverse Colors Clicked", Toast.LENGTH_LONG).show();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//endregion
	
	//region Methods
	
	private void FontPlus(int plus) {
		current_textSize += plus;
		this._wv.getSettings().setDefaultFontSize(current_textSize);
	}
	
	@SuppressWarnings("unused")
	private void Share() {
		
	}
	
	//endregion

}
