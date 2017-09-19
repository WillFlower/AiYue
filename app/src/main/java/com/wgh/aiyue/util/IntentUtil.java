package com.wgh.aiyue.util;

import android.content.Context;
import android.content.Intent;

import com.wgh.aiyue.ui.AboutActivity;
import com.wgh.aiyue.ui.DetailActivity;
import com.wgh.aiyue.ui.FavoriteActivity;
import com.wgh.aiyue.ui.MainActivity;
import com.wgh.aiyue.ui.SearchActivity;

/**
 * Created by   : WGH.
 */
public class IntentUtil {

    public static void openMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void openDetail(Context context, String urlString) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_Url, urlString);
        context.startActivity(intent);
    }

    public static void openAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    public static void openFavorite(Context context) {
        Intent intent = new Intent(context, FavoriteActivity.class);
        context.startActivity(intent);
    }

    public static void openSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
