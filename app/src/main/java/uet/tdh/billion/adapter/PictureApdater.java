package uet.tdh.billion.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;

import uet.tdh.billion.R;
import uet.tdh.billion.common.Common;
import uet.tdh.billion.facebook.samples.zoomable.ZoomableDraweeView;
import uet.tdh.billion.model.Picture;

public class PictureApdater extends RecyclerView.Adapter<PictureApdater.PictureHolder> {

    private ArrayList<Picture> listData;
    private Context mContext;
//    private int height, width;

    public PictureApdater(Context mContext, ArrayList<Picture> listData) {
        this.mContext = mContext;
        this.listData = listData;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

//        height = metrics.heightPixels;
//        width = metrics.widthPixels;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_picture, parent, false);
        return new PictureHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
//        int h = height - 50;
//        int w = 111 * h / 175;
//        if (width < w) {
//            w = width - 10;
//            h = 175 * w / 111;
//        }

        Picture item = listData.get(position);
        Log.d(Common.TAG, item.link);

        Uri uri = Uri.parse(item.link);
        //Uri uri = Uri.parse("res:///" +R.drawable.onload);
        //holder.picture.setImageURI(uri);
        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
                //.setLowResImageRequest(ImageRequest.fromUri(Uri.parse("res:///" +R.drawable.onload)))
                .setUri(uri)
                .setImageRequest(ImageRequest.fromUri(uri))
                .setTapToRetryEnabled(true)
                .setOldController(holder.picture.getController())
                .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();

        holder.picture.setController(ctrl);
        holder.picture.setHierarchy(hierarchy);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class PictureHolder extends RecyclerView.ViewHolder {

        //WrapContentDraweeView picture;
        ZoomableDraweeView picture;

        PictureHolder(View itemView) {
            super(itemView);
            picture = (ZoomableDraweeView) itemView.findViewById(R.id.picture);
        }
    }
}
