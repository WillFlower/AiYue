package com.wgh.aiyue.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wgh.aiyue.R;
import com.wgh.aiyue.util.APPTheme;
import com.wgh.aiyue.util.ConstDefine;

/**
 * Created by   : WGH.
 */
public class BaseActivity extends AppCompatActivity {
    protected void onPreCreate() {
        final APPTheme currentTheme = ConstDefine.getAppTheme();

        switch (currentTheme) {
            case Blue:
                this.setTheme(R.style.BlueTheme);
                break;
            case Green:
                this.setTheme(R.style.GreenTheme);
                break;
            case Red:
                this.setTheme(R.style.RedTheme);
                break;
            case Grey:
                this.setTheme(R.style.BlueGreyTheme);
                break;
            case Black:
                this.setTheme(R.style.BlackTheme);
                break;
            case Orange:
                this.setTheme(R.style.OrangeTheme);
                break;
            case Purple:
                this.setTheme(R.style.PurpleTheme);
                break;
            case Pink:
                this.setTheme(R.style.PinkTheme);
                break;
            case WillFLow:
                this.setTheme(R.style.WillFlowTheme);
            default:
                this.setTheme(R.style.WillFlowTheme);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onPreCreate();
        super.onCreate(savedInstanceState);
    }

    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
