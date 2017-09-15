package com.holela.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.holela.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 5/13/2017.
 */

public class Camera extends Fragment implements SurfaceHolder.Callback {


    android.hardware.Camera camera;
    SurfaceView surfaceView;
    Bitmap bitmap;
    SurfaceHolder surfaceHolder;
    ImageView imageView;

    android.hardware.Camera.PictureCallback rawCallback;
    android.hardware.Camera.ShutterCallback shutterCallback;
    android.hardware.Camera.PictureCallback jpegCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.camera, container, false);

/*

        requestCameraPermission();
        requestWritePermission();
*/

        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView);
        imageView= (ImageView) v.findViewById(R.id.clickmee);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureImagee();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new android.hardware.Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
                FileOutputStream outStream = null;
                try {


                    ExifInterface exif = new ExifInterface(String.valueOf(outStream));
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    int rotate = 0;

                    switch (exifOrientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                    }

                    if (rotate != 0) {

                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();

// Setting pre rotate
                        Matrix mtx = new Matrix();
                        mtx.preRotate(rotate);

                        // Rotating Bitmap & convert to ARGB_8888, required by tess
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    }

                    outStream = new FileOutputStream(String.format("/sdcard/DCIM/Camera/%d.jpg", System.currentTimeMillis()));

                    outStream.write(data);
                    outStream.close();







                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }



                Toast.makeText(getActivity(), "Picture Saved", Toast.LENGTH_SHORT).show();
                refreshCamera();
            }
        };


        return v;
    }


    public void captureImagee() throws IOException
    {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            android.hardware.Camera.Parameters params = camera.getParameters();
            if (params.getSupportedFocusModes().contains(
                    android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            camera.setParameters(params);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }



    //Requesting permission


    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = android.hardware.Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        android.hardware.Camera.Parameters param;
        param = camera.getParameters();

        List<android.hardware.Camera.Size> sizes = param.getSupportedPictureSizes();
        int w = 0, h = 0;
        for (android.hardware.Camera.Size size : sizes) {
            if (size.width > w || size.height > h) {
                w = size.width;
                h = size.height;
            }

        }
        param.setPictureSize(w, h);

        // modify parameter
        param.getSupportedPreviewSizes();
        param.getSupportedPictureSizes();
        param.getSupportedPictureFormats();
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);

            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }



}
