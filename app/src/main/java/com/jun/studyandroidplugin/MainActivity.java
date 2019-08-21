package com.jun.studyandroidplugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private MyClassLoader mMyClassLoader;

    @Bind(R.id.bt_LoadPlugin)
    Button mBtLoadPlugin;
    @Bind(R.id.bt_LoadPluginTodex)
    Button mBtLoadPluginTodex;
    @Bind(R.id.bt_LoadPluginMethod)
    Button mBtLoadPluginMethod;
    @Bind(R.id.bt_LoadPluginAty)
    Button mBtLoadPluginAty;
    @Bind(R.id.bt_LoadPluginService)
    Button mBtLoadPluginService;
    @Bind(R.id.bt_LoadPluginBroad)
    Button mBtLoadPluginBroad;
    @Bind(R.id.bt_CallLocalMethod)
    Button mBtCallLocalMethod;
    @Bind(R.id.bt_PrintclassLoader)
    Button mBtPrintclassLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_PrintclassLoader,R.id.bt_CallLocalMethod,R.id.bt_LoadPlugin, R.id.bt_LoadPluginTodex, R.id.bt_LoadPluginMethod, R.id.bt_LoadPluginAty, R.id.bt_LoadPluginService, R.id.bt_LoadPluginBroad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_PrintclassLoader:
                ClassLoader classLoader = getClassLoader();
                System.out.println("vbvb: " + classLoader);

                Resources lResources = getResources() ;
                CharSequence appName = lResources.getText(R.string.app_name);
                Log.d("vbvb", "app_name: "+appName);
                break;
            case R.id.bt_CallLocalMethod:
                callLocalMethodByClassLoader();
                break;
            case R.id.bt_LoadPlugin:
                copyPluginToPrivateDir();
                break;
            case R.id.bt_LoadPluginTodex:
                generatePluginDex();
                break;
            case R.id.bt_LoadPluginMethod:
                callPluginMethod();
                break;
            case R.id.bt_LoadPluginAty:
                callPluginActivity();
                break;
            case R.id.bt_LoadPluginService:
                break;
            case R.id.bt_LoadPluginBroad:
                break;
            default:
                break;
        }
    }

    /**
     * 通过classLoader调用自己的类
     */
    private void callLocalMethodByClassLoader() {
        ClassLoader llocalClassLoader = this.getClassLoader();
        try {
            Class lClass = llocalClassLoader.loadClass("com.jun.studyandroidplugin.Test");
            Object obj = lClass.newInstance();
            Method printLog = lClass.getMethod("printLog");
            printLog.invoke(obj);
        } catch ( Exception pE) {
            pE.printStackTrace();
        }
    }

    /**
     * 一、复制Asset 文件中的 apk 文件到私有目录
     */
    private void copyPluginToPrivateDir() {
        try {
            doCopy(getDir(Constant.PLUGIN_DIR, Context.MODE_PRIVATE).getAbsolutePath());
            Toast.makeText(this, "保存文件成功", Toast.LENGTH_LONG).show();
        } catch (IOException pE) {
            pE.printStackTrace();
            Toast.makeText(this, "保存文件失败", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 二、将插件apk解析为dex文件
     */
    private void generatePluginDex() {
        try{
            String dexPath = getDir(Constant.PLUGIN_DIR, Context.MODE_PRIVATE) + File.separator + Constant.PLUGIN_APP_NAME;
            File lFile = new File(dexPath);
            if (lFile.exists()) {
                File optimizedDirectory = getDir(Constant.PLUGIN_DIR, 0);
                mMyClassLoader = new MyClassLoader(
                        dexPath,                //data/data/com.jun.studyandroidplugin/app_PluginDex/app-debug.apk
                        optimizedDirectory,     //data/data/com.jun.studyandroidplugin/app_PluginDex/
                        null,
                        this.getClassLoader());
                Toast.makeText(this, "解析dex完成", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "插件apk不存在", Toast.LENGTH_LONG);
            }
        }catch(Exception pE){
            pE.printStackTrace();
        }
    }

    /**
     * 三、调用插件apk中的函数
     */
    private void callPluginMethod() {
        try {
            Class lClass = mMyClassLoader.loadClass("com.jun.pluginapp.MyPlugin");
            Object obj = lClass.newInstance();
            Method say = lClass.getMethod("say");
            String msgFromPlugin = (String) say.invoke(obj);
            Toast.makeText(this, "收到插件消息：" + msgFromPlugin, Toast.LENGTH_LONG).show();
        } catch (Exception pE) {
            pE.printStackTrace();
            Toast.makeText(this, "class生成失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 四、调用插件中的 Activity
     */
    private void callPluginActivity(){
        Intent intent = new Intent(this,ProxyAty.class);
        String className = PluginManager.getInstance(this).getPackageInfo().activities[0].name;
        intent.putExtra("className",className);
        super.startActivity(intent);
    }


    public void doCopy(String filePath) throws IOException {
        InputStream inputStream = this.getAssets().open(Constant.PLUGIN_APP_NAME);
        File file = new File(filePath + File.separator + Constant.PLUGIN_APP_NAME);

        FileOutputStream fos = new FileOutputStream(file);
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
        inputStream.close();
        fos.close();
        Toast.makeText(this, "模型文件复制完毕", Toast.LENGTH_LONG).show();
    }
}
