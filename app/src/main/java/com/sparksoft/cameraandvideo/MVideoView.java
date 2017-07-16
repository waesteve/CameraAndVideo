package com.sparksoft.cameraandvideo;

import android.content.Context;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by jiapeng on 16/07/2017.
 */

public class MVideoView extends VideoView {

    public MVideoView(Context context) {
        super(context);
    }

    public MVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Config.logd( "MVideoView onAttachedToWindow");
    }
}
