package jellyfish.pirates.barikat.ui;

import java.util.ArrayList;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.model.Article;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPagerArticlesDetails extends Fragment {
	
	//region Properties
	
	private ArrayList<Article> _dataSource;
	private int _selectedItemIndex;
	
	private ViewPager _pager;
	private PagerAdapter _pagerAdapter;
	
	//endregion
	
	//region Static Cunstructor
	
	public static FragmentPagerArticlesDetails newInstance(ArrayList<Article> DataSource, int SelectedItemIndex)
	{
		FragmentPagerArticlesDetails fragment = new FragmentPagerArticlesDetails();
		fragment._dataSource = DataSource;
		fragment._selectedItemIndex = SelectedItemIndex;
		return fragment;
	}
	
	//endregion
	
	//region Overrides
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_pager_articles_details, container, false);
		((MainActivity)getActivity()).getSupportActionBar();
		_pager = (ViewPager) rootView.findViewById(R.id.fragment_pager_articles_details_pager);
		
//		PrepareActionBar();
		LoadAnimator();
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LoadAdapters();		
		SetCurrentArticle();
	}
	
	//endregion
	
	//region Methods
	
	//Loads The animator
	private void LoadAnimator() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//				_pager.setPageTransformer(true, new ZoomOutPageTransformer());
			_pager.setPageTransformer(true, new DepthPageTransformer());
		}
	}
	
	//Called after LoadDataSource
	//Set the view Adapter
	private void LoadAdapters() {
		_pagerAdapter = new ArticlesPagerAdapter(getActivity().getSupportFragmentManager());
		_pager.setAdapter(_pagerAdapter);
	}
	
	//Called after LoadAdapters
	//Set the selected Article as the currently Visible
	private void SetCurrentArticle() {
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
