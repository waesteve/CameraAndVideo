package com.sparksoft.cameraandvideo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    public MSurfaceView(Context context) {
        super(context);
    }

    public MSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private ICameraViewListener mListener = null;

    protected MSurfaceView init(ICameraViewListener listener) {
        mListener = listener;
        SurfaceHolder mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);
        return this;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        Config.logd("MSurface setVisibility ： " + visibility);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Config.logd("MSurface  created.");
        if (mListener == null) {
            return;
        }
        Camera camera = mListener.onCameraViewAvailable();
        try {
            camera.setPreviewCallback(this);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            mListener.onCameraViewDestory();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Config.logd("MSurface onAttachedToWindow");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Config.logd("MSurface  surfaceChanged ： " + width + " " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Config.logd("MSurface  surfaceDestroyed：");
        if (mListener == null) {
            return;
        }
        mListener.onCameraViewDestory();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Config.logd("MSurface  onPreviewFrame ： ");
        if (mListener == null) {
            return;
        }
        mListener.onPreviewFrame(data);
    }
}
