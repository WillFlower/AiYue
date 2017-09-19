package com.wgh.aiyue.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaouan.revealator.Revealator;
import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.helper.DialogHelper;
import com.wgh.aiyue.helper.EmailHelper;
import com.wgh.aiyue.util.DateUtil;
import com.wgh.aiyue.util.NetWorkUtil;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgh.aiyue.R.id.about_toolbar;

public class AboutActivity extends AppCompatActivity {
    public static final String STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE = "the_awesome_view_is_visible";

    @BindView(R.id.collapsing_img)
    ImageView collapsingImg;
    @BindView(R.id.about_toolbar)
    Toolbar aboutToolbar;
    @BindView(R.id.collapsing_layout)
    CollapsingToolbarLayout collapsingLayout;
    @BindView(R.id.send_button)
    TextView buttonSend;
    @BindView(R.id.floating_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.feedback_view)
    FrameLayout feedBackView;
    @BindView(R.id.opinion_editext)
    EditText opinionEditext;
    @BindView(R.id.link_editext)
    EditText linkEditext;
    @BindView(R.id.contact_editext)
    EditText contactEditext;
    @BindView(R.id.other_editext)
    EditText otherEditext;
    @BindView(R.id.name_editext)
    EditText nameEditext;

    private boolean isRevealatorOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        aboutToolbar = (Toolbar) findViewById(about_toolbar);
        aboutToolbar.setTitle(R.string.navigation_left_about);
        setSupportActionBar(aboutToolbar);
        collapsingImg.setImageResource(DataCacheHelper.getInstance().getThemeIconId());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DateUtil.isMailInterval()) {
                    Snackbar.make(floatingActionButton, String.format(getResources().getString(R.string.hint_mail_interval),
                            ConstDefine.getMailInterval() - (DateUtil.getCurrentTimeInLong() - ConstDefine.getLastMailTime()) / (1000 * 60)),
                            Snackbar.LENGTH_LONG).setAction(R.string.snake_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastUtil.ToastShort(R.string.snake_hint_mail_interval);
                        }
                    }).show();
                } else {
                    revealatorOpen();
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetWorkUtil.isAvailable(AboutActivity.this)) {
                    ToastUtil.ToastShort(R.string.attention_network_connection);
                    return;
                }
                if (isEditAllEmpty()) {
                    ToastUtil.ToastShort(R.string.attention_empty_data_feedback);
                } else {
                    if (!isEditAllComplete()) {
                        DialogHelper.getInstance(AboutActivity.this, new DialogHelper.AlertListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                sendMail(getMailContent());
                            }

                            @Override
                            public void onNegativeButtonClick() {
                            }
                        }).showAlertDialog(getResources().getString(R.string.send_alert_message));
                    } else {
                        revealatorClose();
                        sendMail(getMailContent());
                        ToastUtil.ToastShort(getResources().getString(R.string.hint_feedback));
                    }
                }
            }
        });

        if (savedInstanceState != null && savedInstanceState.getBoolean(STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE)) {
            feedBackView.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
    }

    private void revealatorOpen() {
        isRevealatorOpen = true;
        floatingActionButton.setVisibility(View.INVISIBLE);
        Revealator.reveal(feedBackView)
                .from(floatingActionButton)
                .withCurvedTranslation()
                .withChildsAnimation()
                //.withDelayBetweenChildAnimation(...)
                //.withChildAnimationDuration(...)
                //.withTranslateDuration(...)
                //.withRevealDuration(...)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        collapsingLayout.setTitle(getString(R.string.navigation_left_feedback));
                    }
                }).start();
    }

    private void revealatorClose() {
        isRevealatorOpen = false;
        Revealator.unreveal(feedBackView)
                .to(floatingActionButton)
                .withCurvedTranslation()
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // floatingActionButton.show();
                        collapsingLayout.setTitle(getString(R.string.navigation_left_about));
                    }
                }).start();
    }

    private boolean isEditAllEmpty() {
        return TextUtils.isEmpty(opinionEditext.getText()) &&
                TextUtils.isEmpty(linkEditext.getText()) &&
                TextUtils.isEmpty(contactEditext.getText()) &&
                TextUtils.isEmpty(otherEditext.getText()) &&
                TextUtils.isEmpty(nameEditext.getText());
    }

    private boolean isEditAllComplete() {
        return !TextUtils.isEmpty(opinionEditext.getText()) &&
                !TextUtils.isEmpty(linkEditext.getText()) &&
                !TextUtils.isEmpty(contactEditext.getText()) &&
                !TextUtils.isEmpty(otherEditext.getText()) &&
                !TextUtils.isEmpty(nameEditext.getText());
    }

    private void sendMail(final String content) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] toAddress = {ConstDefine.getMailNumTo()};
                EmailHelper.send(toAddress, getResources().getString(R.string.mail_title_feedback), content);
                ConstDefine.setLastMailTime(DateUtil.getCurrentTimeInLong());
            }
        }.start();
    }

    private String getMailContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.mail_content_option));
        stringBuilder.append(opinionEditext.getText() + "\n");
        stringBuilder.append(getResources().getString(R.string.mail_content_link));
        stringBuilder.append(linkEditext.getText() + "\n");
        stringBuilder.append(getResources().getString(R.string.mail_content_contact));
        stringBuilder.append(contactEditext.getText() + "\n");
        stringBuilder.append(getResources().getString(R.string.mail_content_name));
        stringBuilder.append(nameEditext.getText() + "\n");
        stringBuilder.append(getResources().getString(R.string.mail_content_other));
        stringBuilder.append(otherEditext.getText() + "\n");
        return stringBuilder.toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE, feedBackView.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isRevealatorOpen) {
            revealatorClose();
        } else {
            super.onBackPressed();
        }
    }
}
