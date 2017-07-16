package com.sparksoft.cameraandvideo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import java.io.IOException;

public class MTextureView extends TextureView implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {
    public MTextureView(Context context) {
        super(context);
    }

    public MTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ICameraViewListener mCameraViewListener = null;

    protected MTextureView init(ICameraViewListener listener) {
        mCameraViewListener = listener;
        //getSurfaceTexture().
        setSurfaceTextureListener(this);
        this.setBackgroundColor(Color.GREEN);
        return this;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mCameraViewListener != null) {
            mCameraViewListener.onPreviewFrame(data);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Config.logd( "MTextureView onSurfaceTextureAvailable");
        if (mCameraViewListener != null) {
            Camera camera = mCameraViewListener.onCameraViewAvailable();
            try {
                camera.setPreviewCallback(this);
                camera.setPreviewTexture(surface);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                mCameraViewListener.onCameraViewDestory();
            }

        }


    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Config.logd( "MTextureView onSurfaceTextureSizeChanged " + width + " " + height);

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Config.logd( "MTextureView onSurfaceTextureDestroyed");

        if (mCameraViewListener != null) {
            mCameraViewListener.onCameraViewDestory();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Config.logd( "MTextureView onSurfaceTextureUpdated");
    }

    @Override
    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
        super.setVisibility(View.VISIBLE);
        Config.logd( "MTextureView setVisibility ： " + visibility);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Config.logd( "MTextureView onAttachedToWindow");
    }
}
