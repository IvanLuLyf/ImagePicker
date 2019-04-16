# ImagePicker

[![](https://jitpack.io/v/IvanLuLyf/ImagePicker.svg)](https://jitpack.io/#IvanLuLyf/ImagePicker)
[![API](https://img.shields.io/badge/API-23%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![Build Status](https://travis-ci.org/IvanLuLyf/ImagePicker.svg?branch=master)](https://travis-ci.org/IvanLuLyf/ImagePicker)
[![GitHub](https://img.shields.io/github/license/IvanLuLyf/ImagePicker.svg?color=blue)](https://github.com/IvanLuLyf/ImagePicker/blob/master/LICENSE)

使用系统图片选择器选择图片.

## 项目配置

添加以下内容到项目的build.gradle文件里面:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

添加项目依赖

```gradle
dependencies {
    implementation 'com.github.IvanLuLyf:ImagePicker:1.0'
}
```

在```res/xml```目录下创建文件```file_paths.xml``` .

> 内容样例

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path name="[pathname]" path="[path]" />
</paths>
```

你可以把```[pathname]```和```[path]```换成你要的内容.

例如: 

```xml
<external-files-path name="image" path="image" />
```

在AndroidManifest.xml的```<application></application>```标记之间添加如下内容.

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="[ProviderName]"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

你可以更换```[ProviderName]```为你想要的内容.例如```com.test.FileProvider```.

```com.test```可以更换为你自己的包名.

## 使用

> 样例

```java
public class MainActivity extends AppCompatActivity {

    private PickImageUtil pickImageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickImageUtil = new PickImageUtil(this, "[ProviderName]", "[pathname]"); //换成你上面定义的内容

        final ImageView imgShow = findViewById(R.id.imgShow);
        Button btnAlbum = findViewById(R.id.btnAlbum);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnAlbumCrop = findViewById(R.id.btnAlbumCrop);
        Button btnCameraCrop = findViewById(R.id.btnCameraCrop);

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.selectAlbum(false);   // 从相册选取图片（不裁剪）
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.selectCamera(false);  // 从相机拍摄图片（不裁剪）
            }
        });

        btnAlbumCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.setSize(300, 300).selectAlbum(true); // 从相册选取图片并裁剪,图片大小为300x300
            }
        });

        btnCameraCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.setSize(300, 300).selectCamera(true);// 从相机拍摄图片并裁剪,图片大小为300x300
            }
        });

        pickImageUtil.setCallback(new PickImageCallback() { // 处理返回
            @Override
            public void onResult(Uri uri, String filePath) {    // 你可以直接使用uri或者用文件路径
                imgShow.setImageURI(uri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //处理onActivityResult
        if (!pickImageUtil.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}
```
