package com.sparksoft.cameraandvideo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.VideoView;

import java.io.IOException;

/**
 * 双SurfaceView
 */
public class LiveCameraActivity2 extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceView mBottomView;

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
        Config.logd("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_camera2);

        mBottomView = (SurfaceView) findViewById(R.id.textureView);
//        mBottomView.setZOrderOnTop(false);
        mBottomView.getHolder().setFormat(PixelFormat.OPAQUE);
        mBottomView.getHolder().addCallback(this);

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(Config.videoUrl1));
        videoView.setVisibility(View.GONE);
        videoView.start();
        VideoView bigvideoView = (VideoView) findViewById(R.id.bigvideoView);
        bigvideoView.setVideoURI(Uri.parse(Config.videoUrl2));
        bigvideoView.setVisibility(View.GONE);
        bigvideoView.start();


        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
      surfaceView.setZOrderOnTop(true);
//        surfaceView.setZOrderMediaOverlay(true);
//      surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT); //可以在onAttach后设置

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
            boolean isTrue = true;

            @Override
            public void onClick(View v) {
                if (isTrue) {
                    surfaceView.setZOrderOnTop(true); //在onAttachedToWindow 后无效
                    surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//                    surfaceView.setBackgroundColor(0x3300FF00);
//                    surfaceView.invalidate();
                } else {
                    surfaceView.setZOrderOnTop(false);
                    surfaceView.getHolder().setFormat(PixelFormat.OPAQUE);
//                  surfaceView.setBackgroundColor(0x33FF0000);
                }

                isTrue = !isTrue;

            }
        });
        findViewById(R.id.invalidateBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                surfaceView.invalidate();
            }
        });

        findViewById(R.id.bottomBtn).setOnClickListener(new View.OnClickListener() {
            boolean isTrue = false;

            @Override
            public void onClick(View v) {
                if (isTrue) {
                    mBottomView.setZOrderOnTop(true);
                } else {
                    mBottomView.setZOrderOnTop(false);
                }
                isTrue = !isTrue;

            }
        });
        findViewById(R.id.bottomRightBtn).setOnClickListener(new View.OnClickListener() {
            boolean isTrue = false;

            @Override
            public void onClick(View v) {
                if (isTrue) {
                    mBottomView.bringToFront();
//                    v.getParent().
                } else {
                    mBottomView.setZOrderOnTop(false);
                }
                isTrue = !isTrue;

            }
        });

        Config.logd("onCreated");
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            IBinder window = mBottomView.getWindowToken();
//            if (window != null) {
//                Config.logd( window.toString());
//            }
//            WindowId windowID = mBottomView.getWindowId();
//            if (windowID != null) {
//                Config.logd( "TextureView: " + windowID.describeContents() + " " + windowID.toString());
//            }
//            windowID = surfaceView.getWindowId();
//            if (windowID != null) {
//                Config.logd( "SurfaceView: " + windowID.describeContents() + " " + windowID.toString());
//            }
//        }
    }

    public void drawCanvas(Canvas canvas) {
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
//        canvas.drawRect(5, 5, 100, 260, paint);
        canvas.drawRect(5, 800, 260, 260, paint);
    }

    /**
     * 界面绘制
     */
    public void drawUI(SurfaceHolder holder) {
        Config.logd("Draw UI ...");
        Canvas canvas = holder.lockCanvas();
        try {
            drawCanvas(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }


    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHandle.removeMessages(1);
        mCamera.stopPreview();
        mCamera.release();
    }
}