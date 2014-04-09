package jellyfish.pirates.barikat.ui;

import java.util.ArrayList;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.model.Article;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ArticleActivity extends ActionBarActivity {
	
	//region Properties
	
	private ArrayList<Article> _dataSource;
	private int _selectedItemIndex;
	
	private ViewPager _pager;
	private PagerAdapter _pagerAdapter;
	
	//endregion

	//region Overrides
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_article);
		
		this._pager = (ViewPager) findViewById(R.id.activity_article_pager);;
		
		getSupportActionBar();
		
		InitializeDataFromExtras();
		PreparePager();
	}

	//endregion

	//region Methods
	
	@SuppressWarnings("unchecked")
	private void InitializeDataFromExtras()
	{
		Bundle extras = getIntent().getExtras();
		_dataSource = extras != null && extras.containsKey("dataSource") ?
				(ArrayList<Article>) extras.getSerializable("dataSource") : new ArrayList<Article>();
		_selectedItemIndex = extras != null && extras.containsKey("selectedItemIndex") ?
				extras.getInt("selectedItemIndex") : 0;
	}
	
	private void PreparePager()
	{
		_pagerAdapter = new ArticlesPagerAdapter(this.getSupportFragmentManager());
		_pager.setAdapter(_pagerAdapter);
		_pager.setCurrentItem(_selectedItemIndex);
	}
	
	//endregion
	
	//region ArticlesPagerAdapter
	
	private class ArticlesPagerAdapter extends FragmentStatePagerAdapter {
		
		public ArticlesPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public FragmentArticleDetail getItem(int position) {
        	Article article = _dataSource.get(position);
            return FragmentArticleDetail.newInstance(article);
        }

        @Override
        public int getCount() {
            return _dataSource != null ? _dataSource.size() : 0;
        }
		
	}
	
	//endregion
	
	//region DepthPageTransformer
	
	public class DepthPageTransformer implements ViewPager.PageTransformer {
	    private static final float MIN_SCALE = 0.75f;

	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 0) { // [-1,0]
	            // Use the default slide transition when moving to the left page
	            view.setAlpha(1);
	            view.setTranslationX(0);
	            view.setScaleX(1);
	            view.setScaleY(1);

	        } else if (position <= 1) { // (0,1]
	            // Fade the page out.
	            view.setAlpha(1 - position);

	            // Counteract the default slide transition
	            view.setTranslationX(pageWidth * -position);

	            // Scale the page down (between MIN_SCALE and 1)
	            float scaleFactor = MIN_SCALE
	                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}

	//endregion
}
