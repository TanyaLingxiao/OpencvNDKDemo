package com.chenly.opencvndkdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button opencvButton;
    private Button ndkButton;
    private ImageView myImageView;

    private Bitmap srcBitmap;
    private Bitmap grayBitmap;

    // 初始化OpenCV
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        opencvButton = (Button) findViewById(R.id.opencvButton);
        ndkButton = (Button) findViewById(R.id.ndkButton);
        myImageView = (ImageView) findViewById(R.id.myImageView);

        opencvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procSrc2Gray();
                myImageView.setImageBitmap(grayBitmap);
                Toast.makeText(MainActivity.this, "The picture has turned into gray picture !", Toast.LENGTH_SHORT).show();
            }
        });

        ndkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, stringFromJNI(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //load OpenCV engine and init OpenCV library
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, getApplicationContext(), mLoaderCallback);
        Log.i(TAG, "onResume sucess load OpenCV...");
    }

    /*
    彩色图转灰度图
    原图像从drawable.pic读取到srcBitmap中
    转换后存到grayBitmap中
     */
    public void procSrc2Gray() {
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);                      //convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap);                   //convert mat to bitmap
        Log.i(TAG, "procSrc2Gray sucess...");
    }

    /*
    初始化JNI
     */
    static {
        System.loadLibrary("demo-jni");
    }
    public native String stringFromJNI();
}
