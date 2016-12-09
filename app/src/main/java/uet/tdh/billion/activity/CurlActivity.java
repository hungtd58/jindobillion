package uet.tdh.billion.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uet.tdh.billion.R;
import uet.tdh.billion.common.Common;
import uet.tdh.billion.model.CurlPage;
import uet.tdh.billion.model.CurlView;
import uet.tdh.billion.model.Picture;

public class CurlActivity extends Activity {

    private CurlView mCurlView;
    ArrayList<Picture> listData;
    String link, index;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_curl);


        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        page = Integer.parseInt(intent.getStringExtra("page"));
        index = intent.getStringExtra("index");

        listData = new ArrayList<>();
        for (int i = 1; i <= page; i++) {
            Picture picture = new Picture(Common.URL_HOME + link + "/" + link + "%20(" + i + ").png");
            listData.add(picture);
        }

        mCurlView = (CurlView) findViewById(R.id.curlview);
        mCurlView.setPageProvider(new PageProvider());
        mCurlView.setSizeChangedObserver(new SizeChangedObserver());
        mCurlView.setCurrentIndex(0);
        mCurlView.setBackgroundColor(0xFF202830);
        //mCurlView.setMargins(-.5f, -.5f, -.5f, -.5f);

    }


    @Override
    public void onPause() {
        super.onPause();
        mCurlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurlView.onResume();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mCurlView.getCurrentIndex();
    }

    /**
     * Bitmap provider.
     */
    private class PageProvider implements CurlView.PageProvider {

        @Override
        public int getPageCount() {
            return page;
        }


        public Drawable drawableFromUrl(String url) throws IOException {
            Bitmap x;

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            x = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(getResources(), x);
        }

        private Bitmap loadBitmap(final int width, final int height, final int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFFFFFF);
            final Canvas c = new Canvas(b);
            final Drawable[] d = {getResources().getDrawable(R.drawable.placeholder)};
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        d[0] = drawableFromUrl(listData.get(index).link);
                        int margin = 7;
                        int border = 3;
                        Rect r = new Rect(margin, margin, width - margin, height - margin);

                        int imageWidth = r.width() - (border * 2);
                        int imageHeight = imageWidth * d[0].getIntrinsicHeight()
                                / d[0].getIntrinsicWidth();
                        if (imageHeight > r.height() - (border * 2)) {
                            imageHeight = r.height() - (border * 2);
                            imageWidth = imageHeight * d[0].getIntrinsicWidth()
                                    / d[0].getIntrinsicHeight();
                        }

                        r.left += ((r.width() - imageWidth) / 2) - border;
                        r.right = r.left + imageWidth + border + border;
                        r.top += ((r.height() - imageHeight) / 2) - border;
                        r.bottom = r.top + imageHeight + border + border;

                        Paint p = new Paint();
                        p.setColor(0xFFC0C0C0);
                        c.drawRect(r, p);
                        r.left += border;
                        r.right -= border;
                        r.top += border;
                        r.bottom -= border;

                        d[0].setBounds(r);
                        d[0].draw(c);

                    } catch (IOException e) {
                    }
                }
            });
            thread.start();
            int margin = 7;
            int border = 3;
            Rect r = new Rect(margin, margin, width - margin, height - margin);

            int imageWidth = r.width() - (border * 2);
            int imageHeight = imageWidth * d[0].getIntrinsicHeight()
                    / d[0].getIntrinsicWidth();
            if (imageHeight > r.height() - (border * 2)) {
                imageHeight = r.height() - (border * 2);
                imageWidth = imageHeight * d[0].getIntrinsicWidth()
                        / d[0].getIntrinsicHeight();
            }

            r.left += ((r.width() - imageWidth) / 2) - border;
            r.right = r.left + imageWidth + border + border;
            r.top += ((r.height() - imageHeight) / 2) - border;
            r.bottom = r.top + imageHeight + border + border;

            Paint p = new Paint();
            p.setColor(0xFFC0C0C0);
            c.drawRect(r, p);
            r.left += border;
            r.right -= border;
            r.top += border;
            r.bottom -= border;

            d[0].setBounds(r);
            d[0].draw(c);

            return b;
        }

        @Override
        public void updatePage(final CurlPage page, final int width, final int height, final int index) {
            final Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFFFFFF);
            final Canvas c = new Canvas(b);

            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(listData.get(index).link))
                    .setAutoRotateEnabled(true)
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            final DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (dataSource.isFinished() && bitmap != null){
                        Log.d("Bitmap","has come");
                        Bitmap bmp = Bitmap.createBitmap(bitmap);
                        dataSource.close();
                        Drawable[] d = {new BitmapDrawable(getResources(), bmp)};
                        int margin = 7;
                        int border = 3;
                        Rect r = new Rect(margin, margin, width - margin, height - margin);

                        int imageWidth = r.width() - (border * 2);
                        int imageHeight = imageWidth * d[0].getIntrinsicHeight()
                                / d[0].getIntrinsicWidth();
                        if (imageHeight > r.height() - (border * 2)) {
                            imageHeight = r.height() - (border * 2);
                            imageWidth = imageHeight * d[0].getIntrinsicWidth()
                                    / d[0].getIntrinsicHeight();
                        }

                        r.left += ((r.width() - imageWidth) / 2) - border;
                        r.right = r.left + imageWidth + border + border;
                        r.top += ((r.height() - imageHeight) / 2) - border;
                        r.bottom = r.top + imageHeight + border + border;

                        Paint p = new Paint();
                        p.setColor(0xFFC0C0C0);
                        c.drawRect(r, p);
                        r.left += border;
                        r.right -= border;
                        r.top += border;
                        r.bottom -= border;

                        d[0].setBounds(r);
                        d[0].draw(c);
                        page.setTexture(b, CurlPage.SIDE_BOTH);
                        page.setColor(Color.argb(127, 255, 255, 255),
                                CurlPage.SIDE_BACK);
                    }
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    if (dataSource != null) {
                        dataSource.close();
                    }
                }
            }, CallerThreadExecutor.getInstance());

//            final Drawable[] d = {getResources().getDrawable(R.drawable.placeholder)};

//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        d[0] = drawableFromUrl(listData.get(index).link);
//                        int margin = 7;
//                        int border = 3;
//                        Rect r = new Rect(margin, margin, width - margin, height - margin);
//
//                        int imageWidth = r.width() - (border * 2);
//                        int imageHeight = imageWidth * d[0].getIntrinsicHeight()
//                                / d[0].getIntrinsicWidth();
//                        if (imageHeight > r.height() - (border * 2)) {
//                            imageHeight = r.height() - (border * 2);
//                            imageWidth = imageHeight * d[0].getIntrinsicWidth()
//                                    / d[0].getIntrinsicHeight();
//                        }
//
//                        r.left += ((r.width() - imageWidth) / 2) - border;
//                        r.right = r.left + imageWidth + border + border;
//                        r.top += ((r.height() - imageHeight) / 2) - border;
//                        r.bottom = r.top + imageHeight + border + border;
//
//                        Paint p = new Paint();
//                        p.setColor(0xFFC0C0C0);
//                        c.drawRect(r, p);
//                        r.left += border;
//                        r.right -= border;
//                        r.top += border;
//                        r.bottom -= border;
//
//                        d[0].setBounds(r);
//                        d[0].draw(c);
//                        page.setTexture(b, CurlPage.SIDE_BOTH);
//                        page.setColor(Color.argb(127, 255, 255, 255),
//                                CurlPage.SIDE_BACK);
//                    } catch (IOException e) {
//                    }
//                }
//            });
//            thread.start();
        }

    }

    /**
     * CurlView size changed observer.
     */
    private class SizeChangedObserver implements CurlView.SizeChangedObserver {
        @Override
        public void onSizeChanged(int w, int h) {
            if (w > h) {
                mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
            } else {
                mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
            }
        }
    }
}
