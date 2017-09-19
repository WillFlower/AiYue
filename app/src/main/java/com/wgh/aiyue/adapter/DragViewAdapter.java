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
public class DragViewAdapter extends BaseAdapter {

    private boolean isItemShow = false;
    private Context context;
    private int holdPosition;
    private boolean isChanged = false;
    boolean isVisible = true;
    public ArrayList<Category> categories;
    private TextView item_text;
    public int remove_position = -1;

    public DragViewAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.channel_view_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        Category category = getItem(position);
        item_text.setText(category.getName());
        if ((position == 0) || (position == 1)){
            item_text.setEnabled(false);
        }
        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
        if (!isVisible && (position == -1 + categories.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if(remove_position == position){
            item_text.setText("");
        }
        return view;
    }

    public void addItem(Category category) {
        categories.add(category);
        notifyDataSetChanged();
    }

    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        Category dragItem = getItem(dragPostion);
        if (dragPostion < dropPostion) {
            categories.add(dropPostion + 1, dragItem);
            categories.remove(dragPostion);
        } else {
            categories.add(dropPostion, dragItem);
            categories.remove(dragPostion + 1);
        }
        isChanged = true;
        notifyDataSetChanged();
    }

    public ArrayList<Category> getCategoryLst() {
        return categories;
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

    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }
}
