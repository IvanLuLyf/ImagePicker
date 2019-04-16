package cn.twimi.util;

import android.net.Uri;

public interface PickImageCallback {
    void onResult(Uri uri, String filePath);
}