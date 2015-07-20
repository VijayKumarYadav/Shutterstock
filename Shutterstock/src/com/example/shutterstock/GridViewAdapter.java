/**
 * 
 */
package com.example.shutterstock;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.shutterstock.ResponseModel.Data;
import com.squareup.picasso.Picasso;

/**
 * GridViewAdapter - Adapter for grid view
 * 
 * @author VijayK
 * 
 */
public class GridViewAdapter extends BaseAdapter {

    private final Context mContext;

    private final List<Data> mItems;

    public GridViewAdapter(final Context context, final List<Data> data) {
        mContext = context;
        mItems = data;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.image_holder);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Data item = mItems.get(position);
        Picasso.with(mContext).load(item.getAssets().getLargeThumb().getUrl()).tag(mContext).into(viewHolder.icon);

        return convertView;
    }

    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     * 
     * @see http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView icon;
    }
}