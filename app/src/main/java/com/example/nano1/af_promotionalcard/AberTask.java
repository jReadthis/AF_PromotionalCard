package com.example.nano1.af_promotionalcard;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by nano1 on 3/11/2016.
 */
public class AberTask extends AsyncTask<String, Void, List<Promotion.PromotionsEntity>> {

    private final String LOG_TAG = AberTask.class.getSimpleName();
    private final Context mContext;
    private static final String FILE_NAME = "response";

    public AberTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Promotion.PromotionsEntity> doInBackground(String... params) {

        List<Promotion.PromotionsEntity> mPromotions = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String mAberJSONString = params[0];

        final String BASE_URL = "http://www.abercrombie.com";
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("anf")
                .appendPath("nativeapp")
                .appendPath("Feeds")
                .appendPath("promotions.json")
                .build();

        if (mAberJSONString.isEmpty()) {
            try {
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.v(LOG_TAG, "Empty input stream");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    Log.v(LOG_TAG, "Stream was empty");
                    return null;
                }
                mAberJSONString = buffer.toString();
                mCreateAndSaveFile(FILE_NAME, mAberJSONString);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
                JSONObject jsonObject = new JSONObject(mAberJSONString);
                JSONArray promotionsArray = jsonObject.getJSONArray("promotions");
                for (int i = 0; i < promotionsArray.length(); i++) {
                    JSONObject promoEntityObject = promotionsArray.getJSONObject(i);
                    String description = promoEntityObject.getString("description");
                    String image = promoEntityObject.getString("image");
                    String title = promoEntityObject.getString("title");
                    String footer = "";
                    try {
                        footer = promoEntityObject.getString("footer");
                    } catch (JSONException noFooter) {
                        noFooter.printStackTrace();
                    }
                    List<ButtonEntity> buttonList = new ArrayList<>();
                    try {
                        JSONArray buttonArray = promoEntityObject.getJSONArray("button");
                        for (int j = 0; j < buttonArray.length(); j++) {
                            buttonList.add(
                                    new ButtonEntity(
                                            buttonArray.getJSONObject(j).getString("target"),
                                            buttonArray.getJSONObject(j).getString("title")
                                    )
                            );
                        }
                    } catch (JSONException notAnArray) {
                        notAnArray.printStackTrace();
                        try {
                            JSONObject buttonObject = promoEntityObject.getJSONObject("button");
                            buttonList.add(
                                    new ButtonEntity(
                                            buttonObject.getString("target"),
                                            buttonObject.getString("title")
                                    )
                            );
                        } catch (JSONException jsonE) {
                            jsonE.printStackTrace();
                        }
                    }

                    mPromotions.add(new Promotion.PromotionsEntity(
                            buttonList,
                            description,
                            footer,
                            image,
                            title
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return mPromotions;
    }

    public void mCreateAndSaveFile(String params, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter("/data/data/" + mContext.getPackageName() + "/" + params);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
