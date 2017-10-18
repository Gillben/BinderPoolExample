package com.ricky.binderpoolexample.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ricky.binderpoolexample.BinderPoolService;
import com.ricky.binderpoolexample.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * Binder连接池
 * Created by ricky on 2017/10/18.
 */

public class BinderPool {

    private static final String TAG = "BinderPool";

    public static final int FOOD_BINDER_NUMBER = 1;
    public static final int SELL_CAR_BINDER_NUMBER = 2;

    private Context mContext;
    private static volatile BinderPool binderPoolInstance;
    private IBinderPool mIBinderPool;
    //作用是把bindService异步绑定转换成同步，为耗时
    private CountDownLatch mCountDownLatch;

    private BinderPool(Context context) {
        this.mContext = context.getApplicationContext();
        connectionBinderPoolService();
    }

    //单例模式获取连接池
    public static BinderPool getBinderPoolInstance(Context context) {
        if (binderPoolInstance == null) {
            synchronized (BinderPool.class) {
                if (binderPoolInstance == null) {
                    binderPoolInstance = new BinderPool(context);
                }
            }
        }
        return binderPoolInstance;
    }

    private synchronized void connectionBinderPoolService() {
        mCountDownLatch = new CountDownLatch(1);
        Intent mIntent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //绑定成功，获取IBinderPool的实例
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: 绑定成功");
            mIBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                //设置IBinder的死亡代理
                mIBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //在服务端异常终止时，清除里面的IBinder,进行重新连接
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mIBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            mIBinderPool = null;
            connectionBinderPoolService();
        }
    };


    public IBinder getIBinderFromService(int type) {
        IBinder binder = null;
        try {
            if (mIBinderPool != null) {
                binder = mIBinderPool.getBinder(type);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }


    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder getBinder(int binderType) throws RemoteException {
            Binder mBinder = null;

            switch (binderType) {
                case FOOD_BINDER_NUMBER:
                    mBinder = new FoodImpl();
                    break;

                case SELL_CAR_BINDER_NUMBER:
                    mBinder = new SellingCarsImpl();
                    break;
            }
            return mBinder;
        }
    }

}
