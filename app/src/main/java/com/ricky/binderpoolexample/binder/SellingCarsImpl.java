package com.ricky.binderpoolexample.binder;

import android.os.RemoteException;
import android.util.Log;

import com.ricky.binderpoolexample.SellingCars;

/**
 * 销售汽车具体类
 * Created by ricky on 2017/10/18.
 */

public class SellingCarsImpl extends SellingCars.Stub {
    private static final String TAG = "SellingCarsImpl";
    @Override
    public void car() throws RemoteException {
        Log.e(TAG, "car: 正在销售汽车" );
    }
}
