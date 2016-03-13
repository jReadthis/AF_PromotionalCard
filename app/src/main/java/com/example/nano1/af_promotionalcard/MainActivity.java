package com.example.nano1.af_promotionalcard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by nano1 on 3/11/2016.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<Promotion.PromotionsEntity> mArrayOfPro;
    ListView mListView;
    MyAdapter myAdapter;
    String response ="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        mListView = (ListView) findViewById(R.id.listView);
        mArrayOfPro = new ArrayList<>();
        if (!isConnected()){
            try {
                response = mReadJsonData("response");
            } catch (IOException e) {
                e.printStackTrace();
            }
                if (response.isEmpty()){
                    notConnected();
                    Toast.makeText(this, "Unable to Connect",Toast.LENGTH_SHORT).show();
                }else {
                    fetchData(response);
                }

        }else {
            fetchData(response);
        }
        myAdapter = new MyAdapter(this, mArrayOfPro);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(this);
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public String mReadJsonData(String params) throws IOException {
        File f = new File("/data/data/" + getPackageName() + "/" + params);
        FileInputStream is = new FileInputStream(f);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer);
    }

    private void fetchData(String response) {
        AberTask downloader = new AberTask(this);
        downloader.execute(response);
        try {
            mArrayOfPro = downloader.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void notConnected(){

        String title = "Welcome to A&F";
        mArrayOfPro.add(new Promotion.PromotionsEntity(
                null,
                null,
                null,
                null,
                title
        ));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", mArrayOfPro.get(position));
        intent.putExtra("target", mArrayOfPro.get(position).getButton().get(0).getTarget());
        startActivity(intent);
    }
}
