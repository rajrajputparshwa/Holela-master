package com.holela.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class RecordVideo extends SurfaceView implements SurfaceHolder.Callback {
	private Activity act;

	public RecordVideo(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		m_recorder = new MediaRecorder();
		holder = getHolder();
		act = (Activity) context;
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public MediaRecorder m_recorder;
	public Camera camera;
	private SurfaceHolder holder;

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int height,
							   int width) {
		Camera.Size previewSize = null;
		try {
			Camera.Parameters parameters = camera.getParameters();
			List<Size> supportSize = parameters
					.getSupportedPreviewSizes();
			if (supportSize != null) {
				previewSize = getOptimalPreviewSize(supportSize, width, height);
			}
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			parameters.set("orientation", "portrait");
			camera.setPreviewDisplay(arg0);
			camera.setParameters(parameters);
			camera.startPreview();
			camera.unlock();
			m_recorder.setPreviewDisplay(arg0.getSurface());
			m_recorder.setCamera(camera);
			m_recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			m_recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			m_recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			m_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			m_recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
			m_recorder.setMaxDuration(90000);
			m_recorder.setOnInfoListener(oninfoLis);
			m_recorder.setVideoSize(previewSize.width, previewSize.height);
			m_recorder.setVideoFrameRate(30);

			m_recorder.setVideoEncodingBitRate(500);
			m_recorder.setAudioEncodingBitRate(128);
			m_recorder.setOutputFile("/mnt/sdcard/myfile"
					+ SystemClock.elapsedRealtime() + ".mp4");
			m_recorder.prepare();
			m_recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.getMessage();
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {

		camera = openFrontFacingCamera();
		if (camera != null) {
			try {
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				camera.release();
				camera = null;
			}
		} else {
			Toast.makeText(act, "Problem in opening Camera.", Toast.LENGTH_LONG)
					.show();
			act.finish();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		try {
			camera.stopPreview();
			camera.release();
			camera = null;
			m_recorder.reset();
			m_recorder.release();
			m_recorder = null;
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	MediaRecorder.OnInfoListener oninfoLis = new OnInfoListener() {

		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Log.i("Error =" + what, "Comes");
		}
	};

	private Camera openFrontFacingCamera() {
		try {
			return Camera.open();
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
}
