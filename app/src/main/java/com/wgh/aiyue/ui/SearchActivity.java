package com.wgh.aiyue.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wgh.aiyue.R;
import com.wgh.aiyue.adapter.BaseListAdapter;
import com.wgh.aiyue.adapter.SearchListAdapter;
import com.wgh.aiyue.ui.view.AutoLoadRecyclerView;
import com.wgh.aiyue.util.IntentUtil;
import com.wgh.aiyue.util.KeyBoardUtil;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements BaseListAdapter.OnItemClickListener, BaseListAdapter.OnLoadPageFinishListener, SearchListAdapter.OnSearchFinishListener{

    @BindView(R.id.img_clean_input)
    ImageView imgCleanInput;
    @BindView(R.id.img_serach)
    ImageView imgSerach;
    @BindView(R.id.edit_search_input)
    EditText editSearchInput;
    @BindView(R.id.recycler_search)
    AutoLoadRecyclerView recyclerSearch;

    private SearchListAdapter mSearchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        KeyBoardUtil.openKeybord(editSearchInput, this);

        mSearchListAdapter = new SearchListAdapter(this, this);
        mSearchListAdapter.setOnItemClickListener(this);
        mSearchListAdapter.setOnSearchFinishListener(this);
        recyclerSearch.setLayoutManager(new GridLayoutManager(this, ConstDefine.getViewPagerColumnNum()));
        recyclerSearch.setAdapter(mSearchListAdapter);
        recyclerSearch.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                mSearchListAdapter.loadNextPage();
            }
        });


        editSearchInput.addTextChangedListener(mTextWatcher);
        editSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    readyToSearch();
                }
                return false;
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                imgCleanInput.setVisibility(View.GONE);
            } else {
                imgCleanInput.setVisibility(View.VISIBLE);
            }
        }
    };

    @OnClick({R.id.img_clean_input, R.id.img_serach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_clean_input:
                editSearchInput.setText("");
                break;
            case R.id.img_serach:
                readyToSearch();
                break;
        }
    }

    private void readyToSearch() {
        String searchString = editSearchInput.getText().toString();
        if (TextUtils.isEmpty(searchString)) {
            ToastUtil.ToastShort(R.string.search_empty_hint);
        } else {
            mSearchListAdapter.updateData(searchString);
        }
    }

    @Override
    public void onLoadPageFinish() {
        recyclerSearch.loadFinish();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (!TextUtils.isEmpty(mSearchListAdapter.getClickUrl(position))) {
            IntentUtil.openDetail(this, mSearchListAdapter.getClickUrl(position));
        }
    }

    @Override
    public void onSearchSuccess() {
        KeyBoardUtil.closeKeybord(editSearchInput, this);
    }

    @Override
    public void onSearchFail() {

    }
}
