package com.ricky.binderpoolexample.binder;

import android.os.RemoteException;
import android.util.Log;

import com.ricky.binderpoolexample.Food;

/**
 * 食物具体类
 * Created by ricky on 2017/10/18.
 */

public class FoodImpl extends Food.Stub {

    private static final String TAG = "FoodImpl";
    @Override
    public void eat() throws RemoteException {
        Log.e(TAG, "eat: 我在吃东西" );
    }
}
