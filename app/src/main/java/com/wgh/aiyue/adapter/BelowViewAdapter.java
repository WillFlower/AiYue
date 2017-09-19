package com.wgh.aiyue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.model.Category;

import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
public class BelowViewAdapter extends BaseAdapter {

    private Context mContext;
    public ArrayList<Category> categories;
    private TextView item_text;
    boolean isVisible = true;
    public int remove_position = -1;

    public BelowViewAdapter(Context context, ArrayList<Category> categories) {
        this.mContext = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories == null ? 0 : categories.size();
    }

    @Override
    public Category getItem(int position) {
        if (categories != null && categories.size() != 0) {
            return categories.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.channel_view_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        Category channel = getItem(position);
        item_text.setText(channel.getName());
        if (!isVisible && (position == -1 + categories.size())) {
            item_text.setText("");
        }
        if (remove_position == position) {
            item_text.setText("");
        }
        return view;
    }

    public ArrayList<Category> getCategoryLst() {
        return categories;
    }

    public void addItem(Category channel) {
        categories.add(channel);
        notifyDataSetChanged();
    }

    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    public void remove() {
        categories.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }

    public void setListDate(ArrayList<Category> list) {
        categories = list;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
