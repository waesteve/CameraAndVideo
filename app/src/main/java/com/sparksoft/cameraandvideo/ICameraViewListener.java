package com.sparksoft.cameraandvideo;

import android.hardware.Camera;

/**
 * Created by jiapeng on 11/07/2017.
 */

public interface ICameraViewListener {


    /**
     * 预览图片处理
     * @param data
     */
    public void onPreviewFrame(byte[] data);

    /**
     * CameraView 可用
     */
    public Camera onCameraViewAvailable();

    public void onCameraViewDestory();
}
