package com.example.nano1.af_promotionalcard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import org.chromium.customtabsclient.CustomTabsActivityHelper;
import org.chromium.customtabsclient.CustomTabsHelper;

/**
 * Created by nano1 on 3/11/2016.
 */

public class DetailActivity extends AppCompatActivity {

    private String url;
    private CustomTabsClient mClient;
    private CustomTabsActivityHelper mCustomTabActivityHelper;
    private CustomTabsSession mCustomTabsSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) findViewById(R.id.text_detail_title);
        TextView mDescription = (TextView) findViewById(R.id.text_description);
        SimpleDraweeView mImage = (SimpleDraweeView) findViewById(R.id.imageView);
        TextView mFooter = (TextView) findViewById(R.id.text_footer);
        Promotion.PromotionsEntity selectedPromotion = getIntent().getParcelableExtra("detail");
        mTitle.setText(selectedPromotion.getTitle());
        mDescription.setText(selectedPromotion.getDescription());
        Uri uri = Uri.parse(selectedPromotion.getImage());
        mImage.setImageURI(uri);
        url = getIntent().getStringExtra("target");
        String footer = selectedPromotion.getFooter();

        if (footer.length()== 0) {
            mFooter.setVisibility(View.GONE);
        }else{
            mFooter.setText(Html.fromHtml(selectedPromotion.getFooter()), TextView.BufferType.SPANNABLE);
            mFooter.setMovementMethod(LinkMovementMethod.getInstance());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mCustomTabActivityHelper = new CustomTabsActivityHelper();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void prepareMenuItems(CustomTabsIntent.Builder builder,Context context) {
        Intent menuIntent = new Intent();
        menuIntent.setClass(context, MainActivity.class);
        // Optional animation configuration when the user clicks menu items.
        Bundle menuBundle = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).toBundle();
        PendingIntent pi = PendingIntent.getActivity(context, 0, menuIntent, 0,
                menuBundle);
        builder.addMenuItem("Menu entry 1", pi);
    }

    private static void prepareActionButton(CustomTabsIntent.Builder builder, Context context) {
        // An example intent that sends an email.
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("*/*");
        actionIntent.putExtra(Intent.EXTRA_EMAIL, "example@example.com");
        actionIntent.putExtra(Intent.EXTRA_SUBJECT, "example");
        PendingIntent pi = PendingIntent.getActivity(context, 0, actionIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        builder.setActionButton(icon, "send email", pi);
    }


    private CustomTabsSession getSession(){
        if (mClient == null) {
            mCustomTabsSession = null;
        }else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new NavigationCallback());
        }
        return mCustomTabsSession;
    }

    public void customTab(String URL, Context context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(Color.BLUE).setShowTitle(true);
        prepareMenuItems(builder, context);
        prepareActionButton(builder, context);
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_arrow_back));
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);
        customTabsIntent.launchUrl((Activity) context, Uri.parse(URL));
    }

        private static class NavigationCallback extends CustomTabsCallback {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        }


    public void openLink(View view) {
        customTab(url,this);
    }
}

