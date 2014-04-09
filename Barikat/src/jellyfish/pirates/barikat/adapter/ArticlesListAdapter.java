package jellyfish.pirates.barikat.adapter;

import java.util.ArrayList;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.model.Article;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ArticlesListAdapter extends BaseAdapter {

	//region Properties
	
	private Context _con = null;
	private ArrayList<Article> _articles = new ArrayList<Article>();
	private View _viewLoadingMore = null;
	private boolean _loadingMore = false;
	
	
	//endregion
	
	//region Constructor
	
	public ArticlesListAdapter(Context Con){
		this._con = Con;
		this._articles = new ArrayList<Article>();
	}
	
	//endregion
	
	//region Getters & Setters
	
	public void setLoadingMore(boolean LoadingMore) {
		this._loadingMore = LoadingMore;
		ChangeLoadingMore(this._loadingMore);
	}
	
	private void ChangeLoadingMore(boolean LoadingMore) {
		if (_viewLoadingMore != null) {
			ViewLoadingMoreHolder holder = (ViewLoadingMoreHolder) _viewLoadingMore.getTag();
			if (holder != null) {
				int loadingVisibility = LoadingMore ? View.VISIBLE : View.GONE;
				holder.pbLoading.setVisibility(loadingVisibility);
//				holder.pbLoading
				holder.tvLoading.setVisibility(loadingVisibility);
				int loadMoreVisibility = LoadingMore ? View.GONE : View.VISIBLE;
				holder.tvLoadMore.setVisibility(loadMoreVisibility);
			}
		}
	}
	
	public boolean isLoadingMore() {
		return this._loadingMore;
	}
	
	//endregion
	
	//region Overrides
	
	@Override
	public int getCount() {
		return _articles.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return position < _articles.size() ? _articles.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position < _articles.size() ? position : Long.MAX_VALUE;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		Article article = (Article) getItem(position);
		CellArticleHolder holder = null;
		View cell = view;
		if (getItemViewType(position) == 1) {
			cell = PrepareLoadMoreCell(cell);
			ChangeLoadingMore(_loadingMore);
		}else {
			cell = PrepareArticleCell(cell, holder, article);
		}
		return cell;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position < _articles.size() ? 0 : 1;
	}
	
	//endregion
	
	//region Methods
	
//	private View PrepareArticleCell(View cell, CellArticleHolder holder, Article article) {
//		if (cell == null) {
//			cell = LayoutInflater.from(_con).inflate(R.layout.list_item_article, null);
//			holder = new CellArticleHolder();
//			holder.imv = (ImageView) cell.findViewById(R.id.li_article_image);
//			holder.tvTitle = (TextView) cell.findViewById(R.id.li_article_title);
//			holder.tvDescription = (TextView) cell.findViewById(R.id.li_article_description);
//			cell.setTag(holder);
//		}else {
//			holder = (CellArticleHolder) cell.getTag();
//		}
//		if (article.HasImage()) {
//			Picasso.with(_con).load(article.getImage()).into(holder.imv);
//		}
//		holder.tvTitle.setText(article.getTitle());
//		holder.tvDescription.setText(article.getDescription());
//		return cell;
//	}
	
	private View PrepareArticleCell(View cell, CellArticleHolder holder, Article article) {
		if (cell == null) {
			Typeface tf = Typeface.createFromAsset( _con.getAssets(), "fonts/Roboto-Regular.ttf");
			cell = LayoutInflater.from(_con).inflate(R.layout.list_item_article, null);
			holder = new CellArticleHolder();
			holder.imv = (ImageView) cell.findViewById(R.id.cell_article_imageView);
			holder.tvTitle = (TextView) cell.findViewById(R.id.cell_article_tv_title);
			holder.tvTitle.setTypeface(tf);
			cell.setTag(holder);
		}else {
			holder = (CellArticleHolder) cell.getTag();
		}
		if (article.HasImage()) {
			Picasso.with(_con).load(article.getImage()).into(holder.imv);
		}
		holder.tvTitle.setText(article.getTitle());
		return cell;
	}
	

	private View PrepareLoadMoreCell(View cell)
	{
		
		if (_viewLoadingMore == null) {
			Typeface tf = Typeface.createFromAsset( _con.getAssets(), "fonts/Roboto-Regular.ttf");
			_viewLoadingMore = LayoutInflater.from(_con).inflate(R.layout.button_load_more, null);
			ViewLoadingMoreHolder holder = new ViewLoadingMoreHolder();
			holder.pbLoading = (ProgressBar) _viewLoadingMore.findViewById(R.id.view_load_more_progress);
			holder.tvLoading = (TextView) _viewLoadingMore.findViewById(R.id.view_load_more_tv_loading);
			holder.tvLoading.setTypeface(tf);
			holder.tvLoadMore = (TextView) _viewLoadingMore.findViewById(R.id.view_load_more_tv_load_more);
			holder.tvLoadMore.setTypeface(tf);
			_viewLoadingMore.setTag(holder);
		}
		cell = _viewLoadingMore;
		return cell;
	}
	
	public void AddArticles(ArrayList<Article> Articles){
		this._articles.addAll(Articles);
		this.setLoadingMore(false);
		this.notifyDataSetChanged();
	}
	
	//endregion
	
	//region CellArticleHolder
	
	private class CellArticleHolder {
		public ImageView imv;
		public TextView tvTitle;
	}
	
	//endregion
	
	//region ViewLoadingMoreHolder
	
	public class ViewLoadingMoreHolder {
		public ProgressBar pbLoading;
		public TextView tvLoading;
		public TextView tvLoadMore;
	}
	
	//endregion
	
}
