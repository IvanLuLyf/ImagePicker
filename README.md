# ImagePicker

This repo made for using system image picker to select image.

## Configure

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```gradle
dependencies {
    implementation 'com.github.IvanLuLyf:ImagePicker:1.0'
}
```

Create a file like```file_paths.xml``` in ```res/xml``` dir.

> Content Sample

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path name="[pathname]" path="[path]" />
</paths>
```

You can replace ```[pathname]``` and ```[path]``` to what you like.

For example: 

```xml
<external-files-path name="image" path="image" />
```

Add the following content in AndroidManifest.xml between tag ```<application></application>```.

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

You can replace ```[ProviderName]``` to what you like.For example ```com.test.FileProvider```.

```com.test``` can replace with your package name.

## Usage

> Sample

```java
public class MainActivity extends AppCompatActivity {

    private PickImageUtil pickImageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickImageUtil = new PickImageUtil(this, "[ProviderName]", "[pathname]");

        final ImageView imgShow = findViewById(R.id.imgShow);
        Button btnAlbum = findViewById(R.id.btnAlbum);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnAlbumCrop = findViewById(R.id.btnAlbumCrop);
        Button btnCameraCrop = findViewById(R.id.btnCameraCrop);

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.selectAlbum(false);   //Pick image from album without crop
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.selectCamera(false);  //Pick image from camera without crop
            }
        });

        btnAlbumCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.setSize(300, 300).selectAlbum(true); // Pick image from album with crop and size is 300x300
            }
        });

        btnCameraCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageUtil.setSize(300, 300).selectCamera(true);// Pick image from camera with crop and size is 300x300
            }
        });

        pickImageUtil.setCallback(new PickImageCallback() { // handle callback
            @Override
            public void onResult(Uri uri, String filePath) {    // You can use uri directly,or use file path
                imgShow.setImageURI(uri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //handle onActivityResult
        if (!pickImageUtil.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}
```
