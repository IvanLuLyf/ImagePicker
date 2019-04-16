package cn.twimi.util;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class PickImageUtil {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_ALBUM = 2;
    private static final int REQUEST_CROP = 3;

    private static final String IMAGE_CONTENT = "image/*";

    private AppCompatActivity activity;
    private File fileCapture;
    private File fileCrop;
    private PickImageCallback callback;
    private File basePath;
    private boolean isCrop;
    private boolean circleCrop;
    private int width, height;
    private String providerName;

    public PickImageUtil(AppCompatActivity activity) {
        this.activity = activity;
        width = 300;
        height = 300;
        circleCrop = false;
    }

    public PickImageUtil(AppCompatActivity activity, String fileProviderName, String imagePath) {
        this.activity = activity;
        basePath = activity.getExternalFilesDir(imagePath);
        width = 300;
        height = 300;
        circleCrop = false;
        providerName = fileProviderName;
    }

    public PickImageUtil setCallback(PickImageCallback callback) {
        this.callback = callback;
        return this;
    }

    public PickImageUtil setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public PickImageUtil setCircleCrop(boolean circleCrop) {
        this.circleCrop = circleCrop;
        return this;
    }

    public void selectCamera(boolean crop) {
        isCrop = crop;
        if (!createFile()) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, providerName, fileCapture));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void selectAlbum(boolean crop) {
        isCrop = crop;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_ALBUM);
    }

    private void cropImage(Uri uri) {
        if (!createCropFile()) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, IMAGE_CONTENT);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("circleCrop", circleCrop);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileCrop));
        activity.startActivityForResult(intent, REQUEST_CROP);
    }

    private boolean createFile() {
        fileCapture = new File(basePath, "image_" + System.currentTimeMillis() + ".jpg");
        try {
            return fileCapture.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createCropFile() {
        fileCrop = new File(basePath, "crop_" + System.currentTimeMillis() + ".jpg");
        try {
            return fileCrop.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AppCompatActivity.RESULT_OK) {
            if (fileCapture != null && fileCapture.exists()) {
                fileCapture.delete();
            }
            if (fileCrop != null && fileCrop.exists()) {
                fileCrop.delete();
            }
            return false;
        }
        if (requestCode == REQUEST_ALBUM && null != data) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                if (isCrop) {
                    cropImage(selectedImage);
                } else {
                    if (callback != null) {
                        callback.onResult(selectedImage, UriToPathUtil.uriToPath(activity, selectedImage));
                    }
                }
                return true;
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (isCrop) {
                cropImage(FileProvider.getUriForFile(activity, providerName, fileCapture));
            } else {
                if (callback != null) {
                    callback.onResult(FileProvider.getUriForFile(activity, providerName, fileCapture), fileCapture.getPath());
                }
            }
            return true;
        } else if (requestCode == REQUEST_CROP) {
            if (callback != null) {
                callback.onResult(FileProvider.getUriForFile(activity, providerName, fileCrop), fileCrop.getPath());
            }
            return true;
        }
        return false;
    }
}
