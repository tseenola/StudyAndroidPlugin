package com.jun.studyandroidplugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by lenovo on 2019/7/16.
 * 描述：
 */
public class PluginManager {
    private Resources mResources;
    private static PluginManager ourInstance = new PluginManager();
    private DexClassLoader mDexClassLoader;
    private static Context mContext;
    private PackageInfo mPackageInfo;
    private static String mPluginApkPath;

    public static PluginManager getInstance(Context pContext){
        mContext = pContext;
        return ourInstance;
    }

    private PluginManager() {
    }


    public void loadApk() {
        mPluginApkPath = mContext.getDir(Constant.PLUGIN_DIR, Context.MODE_PRIVATE) + File.separator + Constant.PLUGIN_APP_NAME;
        File dexOutFile = mContext.getDir(Constant.PLUGIN_DIR, Context.MODE_PRIVATE);
        mDexClassLoader = new DexClassLoader(mPluginApkPath, dexOutFile.getAbsolutePath(), null, mContext.getClassLoader());
        try {
            AssetManager lAssetManager = AssetManager.class.newInstance();
            //调用addAssetPath函数传入资源文件目录。
            Method lMethod = AssetManager.class.getMethod("addAssetPath", String.class);
            lMethod.invoke(lAssetManager, mPluginApkPath);
            mResources = new Resources(lAssetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());
            mPackageInfo = mContext.getPackageManager().getPackageArchiveInfo(mPluginApkPath, PackageManager.GET_ACTIVITIES);
        } catch (Exception pE) {
            pE.printStackTrace();
        }
    }

    public ClassLoader getClassLoader() {
        if (mDexClassLoader == null) {
            loadApk();
        }
        return mDexClassLoader;
    }

    public Resources getResources() {
        if (mResources == null) {
            loadApk();
        }
        return mResources;
    }

    public PackageInfo getPackageInfo() {
        if (mPackageInfo == null) {
            loadApk();
        }
        return mPackageInfo;
    }
}
