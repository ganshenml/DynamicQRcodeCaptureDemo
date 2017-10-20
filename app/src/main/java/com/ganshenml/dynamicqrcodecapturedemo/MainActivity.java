package com.ganshenml.dynamicqrcodecapturedemo;


import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainLayoutLl;
    private EditText contentEt;
    private Button qrcodeBtn;
    private ImageView showIv;
    private View hideView;
    private ImageView qrcodeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        mainLayoutLl = (LinearLayout) findViewById(R.id.activity_main);
        contentEt = (EditText) findViewById(R.id.contentEt);
        qrcodeBtn = (Button) findViewById(R.id.qrcodeBtn);
        showIv = (ImageView) findViewById(R.id.showIv);

    }

    private void initListeners() {
        qrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateAndShowCaptureView();
            }
        });

    }

    private void inflateAndShowCaptureView() {
        if (hideView == null) {
            hideView = LayoutInflater.from(this).inflate(R.layout.layout_quick_capture, null);
            qrcodeIv = (ImageView) hideView.findViewById(R.id.qrcodeIv);
            hideView.setDrawingCacheEnabled(true);//设置控件允许绘制缓存
            hideView.measure(View.MeasureSpec.makeMeasureSpec(mainLayoutLl.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            hideView.layout(0, 0, hideView.getMeasuredWidth(), hideView.getMeasuredHeight());
        } else {
            hideView.destroyDrawingCache();//要得到新的视图，就得销毁之前的缓存
        }

        showCaptureView();
    }

    private void showCaptureView() {
        String content = contentEt.getText().toString().trim();
        if (content == null || content.length() == 0) {
            return;
        }
        if (qrcodeIv.getWidth() == 0) {
            return;
        }
        Bitmap qrcodeBitmap = ZXingUtils.createQRImage(content, qrcodeIv.getWidth(), qrcodeIv.getHeight());
        qrcodeIv.setImageBitmap(qrcodeBitmap);//先将生成的二维码显示在加载的视图上

        Bitmap bitmap = hideView.getDrawingCache();  // 获取视图的绘制缓存（快照）
        if (bitmap != null) {
            showIv.setImageBitmap(bitmap);
        }

    }

    @Override
    protected void onDestroy() {
        if (hideView != null) {
            hideView = null;
        }
        super.onDestroy();
    }
}
