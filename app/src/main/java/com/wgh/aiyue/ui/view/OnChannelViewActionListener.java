package com.wgh.aiyue.ui.view;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by   : WGH.
 */
interface OnChannelViewActionListener {
    void onEditMode(boolean isEditMode);
    void onItemLongClick(AdapterView<?> parent, View view, int position, long id);
    void onItemDragPosition(int startPosition, int endPosition);
    void onItemDragFinish();
    void onDragItemClick(int position);
    void onBelowItemClick(int position);
    void onDragItemEditClick(int position);
    void onBelowItemEditClick(int position);
}
