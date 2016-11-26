package uet.tdh.billion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import uet.tdh.billion.R;
import uet.tdh.billion.adapter.PictureApdater;
import uet.tdh.billion.common.Common;
import uet.tdh.billion.model.Picture;
import uet.tdh.billion.ui.SnappyRecyclerView;

public class CommicActivity extends AppCompatActivity {

    SnappyRecyclerView listPicture;
    ArrayList<Picture> listData;
    PictureApdater adapter;
    String link, index;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_commic);

        listPicture = (SnappyRecyclerView) findViewById(R.id.listPicture);
        listPicture.setHasFixedSize(true);
        listPicture.setItemViewCacheSize(20);
        listPicture.setDrawingCacheEnabled(true);
        listPicture.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        listPicture.setLayoutManager(new LinearLayoutManager(CommicActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        listData = new ArrayList<>();

        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        page = Integer.parseInt(intent.getStringExtra("page"));
        index = intent.getStringExtra("index");

        for (int i = 1; i <= page; i++) {
            Picture picture = new Picture(Common.URL_HOME + link + "/" + link + "%20(" + i + ").png");
            listData.add(picture);
        }
        adapter = new PictureApdater(CommicActivity.this, listData);
        listPicture.setAdapter(adapter);
    }
}
