package cn.twimi.imagepicker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.twimi.util.PickImageCallback;
import cn.twimi.util.PickImageUtil;

public class MainActivity extends AppCompatActivity {

    private PickImageUtil pickImageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickImageUtil = new PickImageUtil(this, "cn.twimi.imagepicker", "image");

        final ImageView imgShow = findViewById(R.id.imgShow);
        Button btnAlbum = findViewById(R.id.btnAlbum);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnAlbumCrop = findViewById(R.id.btnAlbumCrop);
        Button btnCameraCrop = findViewById(R.id.btnCameraCrop);

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.selectAlbum(false);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.selectCamera(false);
            }
        });

        btnAlbumCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.setSize(300, 300).selectAlbum(true);
            }
        });

        btnCameraCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.setSize(300, 300).selectCamera(true);
            }
        });

        pickImageUtil.setCallback(new PickImageCallback() {
            @Override
            public void onResult(Uri uri, String filePath) {
                imgShow.setImageURI(uri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!pickImageUtil.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}
