package com.sparksoft.cameraandvideo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.VideoView;

import java.io.IOException;

/**
 * TextureView 和 SurfaceView
 */
public class LiveCameraActivity extends Activity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;

    private SurfaceView surfaceView = null;


    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    drawUI(surfaceView.getHolder());
                    mHandle.sendEmptyMessageDelayed(1, 100);
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_camera);


        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);


        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(Config.videoUrl1));
//        videoView.setVisibility(View.GONE);
        videoView.start();
//
        VideoView bigvideoView = (VideoView) findViewById(R.id.bigvideoView);
        bigvideoView.setVideoURI(Uri.parse(Config.videoUrl2));
        bigvideoView.setVisibility(View.GONE);
        bigvideoView.start();


        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
//        surfaceView.setZOrderOnTop(true);
        surfaceView.setZOrderMediaOverlay(true);
//        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                mHandle.sendEmptyMessageDelayed(1, 100);
                surfaceView.bringToFront();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            boolean isTrue = false;
            @Override
            public void onClick(View v) {
                if (isTrue) {
//                    surfaceView.setZOrderOnTop(true);
                    surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//                    surfaceView.setBackgroundColor(0x3300FF00);
                } else {
//                    surfaceView.setZOrderOnTop(false);
                    surfaceView.getHolder().setFormat(PixelFormat.OPAQUE);
//                    surfaceView.setBackgroundColor(0x33FF0000);
                }


                isTrue = !isTrue;

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IBinder window = mTextureView.getWindowToken();
            if (window != null) {
            }
            WindowId windowID = mTextureView.getWindowId();
            if (windowID != null) {
                Config.logd( "TextureView: " + windowID.describeContents() + " " + windowID.toString());
            }
            windowID = surfaceView.getWindowId();
            if (windowID != null) {
                Config.logd( "SurfaceView: " + windowID.describeContents() + " " + windowID.toString());
            }
        }
    }

    public void drawCanvas(Canvas canvas) {
//      canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
//        canvas.drawRect(5, 5, 100, 260, paint);
        canvas.drawRect(5, 800, 260, 260, paint);
    }

    /**
     * 界面绘制
     */
    public void drawUI(SurfaceHolder holder) {
        Config.logd( "Draw UI ...");
        Canvas canvas = holder.lockCanvas();
        try {
            drawCanvas(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }


    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mHandle.removeMessages(1);
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }
}