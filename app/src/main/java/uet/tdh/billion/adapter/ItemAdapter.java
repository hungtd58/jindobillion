package uet.tdh.billion.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uet.tdh.billion.R;
import uet.tdh.billion.activity.CommicActivity;
import uet.tdh.billion.common.Common;
import uet.tdh.billion.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private ArrayList<Item> mListData = new ArrayList<>();
    private Context mContext;
    private int height;

    LayoutInflater inflater;

    public ItemAdapter(Context context, ArrayList<Item> listData) {
        this.mListData = listData;
        this.mContext = context;
        inflater = LayoutInflater.from(context);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comic, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

        int h = height / 4;
        int w = 111 * h / 175;
        final Item item = mListData.get(position);

        Picasso.with(mContext)
                .load(Common.URL_HOME + item.link + "/" + item.link + "%20(1).png")
                .resize(w, h)
                .into(holder.thumbnail);

        holder.nameItem.setText("Jindo - Tập " + item.index);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommicActivity.class);
                intent.putExtra("link", item.link);
                intent.putExtra("page", item.page);
                intent.putExtra("index", item.index);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView nameItem;

        ItemHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            nameItem = (TextView) itemView.findViewById(R.id.nameItem);
        }
    }
}
