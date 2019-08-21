package com.jun.stdface;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lenovo on 2019/7/16.
 * 描述：
 */
public interface AtyInterface {
    void onAttach(Activity pActivity);//宿主Activity通过这个地方传递过来

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
