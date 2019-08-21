package com.jun.studyandroidplugin;

import java.io.File;

import dalvik.system.PathClassLoader;

/**
 * Created by lenovo on 2019/7/11.
 * 描述：
 *  Parent Class BaseDexClassLoader will init DexPathList class,
 *  DexPathList have a property Element [] and DexPathList Constractor will init Element []
 *  and  the Element [] saved DexFile who fileName endWith .dex
 *
 *
 *
 *  more infomation :
 *  http://androidxref.com/5.1.0_r1/xref/libcore/dalvik/src/main/java/dalvik/system/
 */
public class MyClassLoader extends PathClassLoader {
    public MyClassLoader(String dexPath, File optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath,   librarySearchPath, parent);
    }
}
