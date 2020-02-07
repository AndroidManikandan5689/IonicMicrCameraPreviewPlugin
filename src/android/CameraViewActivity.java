package cordova.plugin.raqmiyat.micrcameraview;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.cameraview.CameraView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * This demo app saves the taken picture to a constant file.
 * $ adb pull /sdcard/Android/data/com.google.android.cameraview.demo/files/Pictures/picture.jpg
 */
public class CameraViewActivity extends Activity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "CameraViewActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

    private CameraView mCameraView;
    private OverlayView mOverlayView;

    private Handler mBackgroundHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String package_name = getApplication().getPackageName();
        Resources resources = getApplication().getResources();

        int layoutID = resources.getIdentifier("camera_preview", "layout", package_name);
        int camera = resources.getIdentifier("camera", "id", package_name);
        int overlayView = resources.getIdentifier("overlayView", "id", package_name);
        int OkbuttonID = resources.getIdentifier("okbtn", "id", package_name);
        int closeButtonID = resources.getIdentifier("cancelbtn", "id", package_name);

        setContentView(layoutID);

        mCameraView = findViewById(camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }

        mOverlayView = (OverlayView) findViewById(overlayView);

        ImageView btnCapture = (ImageView) findViewById(OkbuttonID);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
                showOverLay();
            }
        });

        ImageView btnCancel = (ImageView) findViewById(closeButtonID);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ActivityResult", "Cancelled by the user.");
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }


    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");


            showOverLay();
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);

            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {

                    Bitmap cameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    int   wid = cameraBitmap.getWidth();
                    int  hgt = cameraBitmap.getHeight();
                    Bitmap newImage = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(newImage);
                    canvas.drawBitmap(cameraBitmap, 0f, 0f, null);


                    File storagePath = new File(Environment.getExternalStorageDirectory() + "/Android/data/cordova.raqmiyat.camerapreview/cache/");
                    storagePath.mkdirs();

                    File myImage = new File(storagePath, Long.toString(System.currentTimeMillis()) + ".jpg");

                    try
                    {
                        FileOutputStream out = new FileOutputStream(myImage);
                        newImage.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        out.flush();
                        out.close();
                    }
                    catch(FileNotFoundException e)
                    {
                        Log.d("In Saving File", e + "");
                    }
                    catch(IOException e)
                    {
                        Log.d("In Saving File", e + "");
                    }

//                    mCameraView.startPreview();

                    newImage.recycle();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ActivityResult", "file://"+myImage.getAbsolutePath());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }
            });
        }


    };


    private void showOverLay() {
        LeadSize leadSize = new LeadSize(mCameraView.getWidth(), mCameraView.getHeight());
        mOverlayView.updateArea(leadSize, leadSize, leadSize);
        // show overlay view
        mOverlayView.setVisibility(View.VISIBLE);
        mOverlayView.invalidate();

    }


}
