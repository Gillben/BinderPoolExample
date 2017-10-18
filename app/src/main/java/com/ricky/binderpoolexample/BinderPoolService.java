package com.ricky.binderpoolexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ricky.binderpoolexample.binder.BinderPool;

/**
 * 连接池Service，返回IBinderPool的具体实现类
 * Created by ricky on 2017/10/18.
 */

public class BinderPoolService extends Service {


    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
