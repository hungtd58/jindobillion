package uet.tdh.billion.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.Arrays;

import uet.tdh.billion.R;
import uet.tdh.billion.adapter.ItemAdapter;
import uet.tdh.billion.model.Item;

public class MainActivity extends AppCompatActivity {

    RecyclerView listItem;
    ArrayList<Item> listData;
    ItemAdapter adapter;
    RequestQueue mQueue;
    Item [] listDataArray = {
            new Item("chap001", "49", "1"),
            new Item("chap002", "35", "2"),
            new Item("chap003", "43", "3"),
            new Item("chap004", "58", "4"),
            new Item("chap005", "54", "5"),
            new Item("chap006", "43", "6"),
            new Item("chap007", "55", "7"),
            new Item("chap008", "43", "8"),
            new Item("chap009", "44", "9"),
            new Item("chap010", "45", "10")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listItem = (RecyclerView) findViewById(R.id.listItem);
        listItem.setHasFixedSize(true);
        listItem.setItemViewCacheSize(20);
        listItem.setDrawingCacheEnabled(true);
        listItem.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        listItem.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        listData = new ArrayList<>(Arrays.asList(listDataArray));
        adapter = new ItemAdapter(MainActivity.this, listData);
        listItem.setAdapter(adapter);
    }
}
