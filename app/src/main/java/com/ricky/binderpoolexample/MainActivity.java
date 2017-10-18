package com.ricky.binderpoolexample;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import com.ricky.binderpoolexample.binder.BinderPool;
import com.ricky.binderpoolexample.binder.FoodImpl;
import com.ricky.binderpoolexample.binder.SellingCarsImpl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startBinderPoolService();
            }
        }).start();

    }

    private void startBinderPoolService() {
        BinderPool mBinderPool = BinderPool.getBinderPoolInstance(this);

        IBinder mIBinder = mBinderPool.getIBinderFromService(BinderPool.FOOD_BINDER_NUMBER);
        Food mFood = FoodImpl.Stub.asInterface(mIBinder);
        try {
            mFood.eat();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        IBinder carBinder = mBinderPool.getIBinderFromService(BinderPool.SELL_CAR_BINDER_NUMBER);
        SellingCars sellCar = SellingCarsImpl.Stub.asInterface(carBinder);
        try {
            sellCar.car();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
