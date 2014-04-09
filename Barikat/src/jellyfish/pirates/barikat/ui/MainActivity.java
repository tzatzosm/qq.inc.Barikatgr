package jellyfish.pirates.barikat.ui;

import java.util.ArrayList;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.model.Article;
import jellyfish.pirates.barikat.model.NavDrawerItem;
import jellyfish.pirates.barikat.ui.FragmentArticleList.IFragmentArticleListListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.bugsense.trace.BugSenseHandler;

public class MainActivity extends ActionBarActivity implements IFragmentArticleListListener, TabListener, OnPageChangeListener{

	//region Properties
	
	private Context 								_con;
	
	private String[]								_titles;
	private String[]								_urls;
	private ArrayList<NavDrawerItem> 				_navDrawerItems = new ArrayList<NavDrawerItem>();
	
	private ActionBar 								_actionBar;
	private ViewPager 								_pager;
	private TabsPagerAdapter 						_pagerAdapter;

	//endregion
	
	//region Overrides
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initializing Bugsense
		BugSenseHandler.initAndStartSession(this, "9795be94");
		BugSenseHandler.startSession(this);
		
		//This is needed for the setSupportProgressBarIndeterminateVisibility
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_main);

		
		
		_con = this;
		
		_pager = (ViewPager) findViewById(R.id.activity_main_category_list_pager);
		
		Log.d("Lifecycle", "onCreate");
		
		_actionBar = getSupportActionBar();
		
		InitNavigationItems();
		PrepareActionBar();
		PrepareViewPager();
	}
	
	//endregion
	
	//region Methods
	
	private void InitNavigationItems()
	{
		_titles = getResources().getStringArray(R.array.nav_drawer_titles);
		_urls = getResources().getStringArray(R.array.nav_drawer_urls);
		
		for (int i=0;i<_titles.length;i++) {
			_navDrawerItems.add(new NavDrawerItem(_titles[i], _urls[i]));
		}
	}
	
	private void PrepareViewPager()
	{
		_pager.setPageTransformer(true, new ZoomOutPageTransformer());
		_pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		_pager.setAdapter(_pagerAdapter);
		_pager.setOnPageChangeListener(this);
	}
	
	private void PrepareActionBar()
	{
		_actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		for (NavDrawerItem item : _navDrawerItems) {
			_actionBar.addTab(_actionBar.newTab()
					.setText(item.getTitle())
					.setTabListener((MainActivity)_con)
					.setTag(item));
		}
	}
	
	private ArrayList<Article> SubList(ArrayList<Article> Articles, int Start, int End ){
		ArrayList<Article> res = new ArrayList<Article>();
		for (int i=Start;i<=End;i++) {
			res.add(Articles.get(i));
		}
		return res;
	}
	
	
	//endregion
	
	//region TabsPagerAdapter
	
	private class TabsPagerAdapter extends FragmentStatePagerAdapter{

		public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		@Override
		public FragmentArticleList getItem(int position) {
			NavDrawerItem item = _navDrawerItems.get(position);
			return FragmentArticleList.newInstance(item.getRssURL(), (MainActivity)_con);
		}

		@Override
		public int getCount() {
			return _navDrawerItems != null ? _navDrawerItems.size() : 0;
		}
	}

	//endregion
	
	//region ZoomOutPageTransformer
	
	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	    private static final float MIN_SCALE = 0.85f;
	    private static final float MIN_ALPHA = 0.5f;

	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 1) { // [-1,1]
	            // Modify the default slide transition to shrink the page as well
	            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX(horzMargin - vertMargin / 2);
	            } else {
	                view.setTranslationX(-horzMargin + vertMargin / 2);
	            }

	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	            // Fade the page relative to its size.
	            view.setAlpha(MIN_ALPHA +
	                    (scaleFactor - MIN_SCALE) /
	                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
	
	//endregion
	
	//region IFragmentArticleListListener
	
	@Override
	public void OnArticleSelected( ArrayList<jellyfish.pirates.barikat.model.Article> articles, jellyfish.pirates.barikat.model.Article article) {
		Intent i = new Intent( this, ArticleActivity.class);
		Bundle extras = new Bundle();
		int selectedArticleIndex = articles.indexOf(article);
		int articleIndexStart = selectedArticleIndex - 5 >= 0 ? selectedArticleIndex - 5 : 0;
		int articleIndexEnd = selectedArticleIndex + 5 <= articles.size() - 1 ? selectedArticleIndex + 5 : articles.size() - 1;
		int selectedArticleRelativeIndex = selectedArticleIndex - articleIndexStart ;
		ArrayList<Article> dtDataSource = SubList( articles, articleIndexStart, articleIndexEnd);
		extras.putSerializable("dataSource", dtDataSource);
		extras.putInt("selectedItemIndex", selectedArticleRelativeIndex);
		i.putExtras(extras);
		this.startActivity(i);
	}

	//endregion
	
	//region TabChangedListener

	@Override
	public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
		NavDrawerItem item = (NavDrawerItem) tab.getTag();
		int position = _navDrawerItems.indexOf(item);
		this._pager.setCurrentItem(position);
	}

	@Override
	public void onTabUnselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
		
	}

	//endregion
	
	//region OnPageChangedListener
	
	@Override
	public void onPageScrollStateChanged(int position) {
		
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		this._actionBar.setSelectedNavigationItem(position);
	}

	//endregion
	
}
