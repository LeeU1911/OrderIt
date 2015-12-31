package orderit.mainapp.application;

/**
 * Created by Blackcool on 12/31/15.
 */

import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.app.Application;

public class AppProcess extends Application {
    private ImageView img; // the image
    private RelativeLayout bgimg; // layout of the activity
    private Bitmap nav; // the image in the Bitmap format
    private Bitmap background; // background in the Bitmap format
    private BitmapDrawable bg; // background in the Drawable format

    public void loadBackground(int id) {
        background = BitmapFactory.decodeStream(getResources().openRawResource(id));
        bg = new BitmapDrawable(getResources(), background);
        bgimg.setBackground(bg);
    }

    public void unloadBackground() {
        if (bgimg != null)
            bgimg.setBackground(null);
        if (bg!= null) {
            background.recycle();
        }
        bg = null;
    }

    public void loadBitmap(int id) {
        nav = BitmapFactory.decodeStream(getResources().openRawResource(id));
        img.setImageBitmap(nav);
    }
    public void unloadBitmap() {
        if (img != null)
            img.setImageBitmap(null);
        if (nav!= null) {
            nav.recycle();
        }
        nav = null;
    }

    public void setBackground(RelativeLayout i, int sourceid) {
        unloadBackground();
        bgimg = i;
        loadBackground(sourceid);
    }

    public void setImage(ImageView i, int sourceid) {
        unloadBitmap();
        img = i;
        loadBitmap(sourceid);
    }
}
