package com.wgh.aiyue.ui.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wgh.aiyue.R;
import com.wgh.aiyue.ui.MainActivity;
import com.wgh.aiyue.util.APPTheme;
import com.wgh.aiyue.util.ConstDefine;


/**
 * Created by   : WGH.
 */
public class ThemeDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final View layout = inflater.inflate(R.layout.dialog_theme, container, false);
        layout.findViewById(R.id.blue_theme).setOnClickListener(this);
        layout.findViewById(R.id.willflow_theme).setOnClickListener(this);
        layout.findViewById(R.id.green_theme).setOnClickListener(this);
        layout.findViewById(R.id.red_theme).setOnClickListener(this);
        layout.findViewById(R.id.grey_theme).setOnClickListener(this);
        layout.findViewById(R.id.black_theme).setOnClickListener(this);
        layout.findViewById(R.id.purple_theme).setOnClickListener(this);
        layout.findViewById(R.id.orange_theme).setOnClickListener(this);
        layout.findViewById(R.id.pink_theme).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View v) {
        APPTheme theme;
        switch (v.getId()) {
            case R.id.blue_theme:
                theme = APPTheme.Blue;
                break;
            case R.id.willflow_theme:
                theme = APPTheme.WillFLow;
                break;
            case R.id.green_theme:
                theme = APPTheme.Green;
                break;
            case R.id.red_theme:
                theme = APPTheme.Red;
                break;
            case R.id.grey_theme:
                theme = APPTheme.Grey;
                break;
            case R.id.black_theme:
                theme = APPTheme.Black;
                break;
            case R.id.orange_theme:
                theme = APPTheme.Orange;
                break;
            case R.id.purple_theme:
                theme = APPTheme.Purple;
                break;
            case R.id.pink_theme:
                theme = APPTheme.Pink;
                break;
            default:
                theme = APPTheme.WillFLow;
                break;
        }
        ConstDefine.setAppTheme(theme);
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}
