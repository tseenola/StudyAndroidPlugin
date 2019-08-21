package com.jun.studyandroidplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jun.stdface.AtyInterface;

import java.lang.reflect.Constructor;

/**
 * Created by lenovo on 2019/7/16.
 * 描述：
 */
public class ProxyAty extends Activity {
    private AtyInterface lAtyInterface;
    private static String msClassPackageAndName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            msClassPackageAndName = getIntent().getStringExtra("className");
            Class activityClass = getClassLoader().loadClass(msClassPackageAndName);
            Constructor lConstructor = activityClass.getConstructor(new Class[]{});
            Object lO = lConstructor.newInstance(new Object[]{});
            lAtyInterface = (AtyInterface) lO;
            lAtyInterface.onAttach(this);
            Bundle lBundle = new Bundle();
            lAtyInterface.onCreate(savedInstanceState);
        }catch(Exception pE){
            pE.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        lAtyInterface.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lAtyInterface.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lAtyInterface.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lAtyInterface.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lAtyInterface.onDestroy();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent proxyIntent = new Intent(this,ProxyAty.class);
        proxyIntent.putExtra("className",className);
        super.startActivity(proxyIntent);
    }
}
