package jellyfish.pirates.barikat.adapter;

import java.util.ArrayList;

import jellyfish.pirates.barikat.R;
import jellyfish.pirates.barikat.model.NavDrawerItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter{

	private Context _con;
	private ArrayList<NavDrawerItem> _items;
	
	public NavDrawerListAdapter(Context Con, ArrayList<NavDrawerItem> NavDrawerItems) {
		this._con = Con;
		this._items = NavDrawerItems;
	}
	
	
	@Override
	public View getView(int position, View view, ViewGroup group) {
		View cell = view;
		Holder holder = null;
		if (cell == null) {
			cell = LayoutInflater.from(_con).inflate(R.layout.list_item_drawer, null);
			holder = new Holder();
			holder.tvTitle = (TextView) cell.findViewById(R.id.li_drawer_title);
			cell.setTag(holder);
		}else {
			holder = (Holder) cell.getTag();
		}
		NavDrawerItem item = (NavDrawerItem) getItem(position); 
		holder.tvTitle.setText(item.getTitle());
		return cell;
	}
	
	private class Holder {
		TextView tvTitle;
	}

	@Override
	public int getCount() {
		if (_items != null){
			return _items.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (_items != null) {
			return _items.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
