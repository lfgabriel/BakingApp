package com.example.android.bakingapp.utilities;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;


public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;

    private AtomicBoolean isIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return(SimpleIdlingResource.class.getSimpleName());
    }

    @Override
    public boolean isIdleNow() {
        return isIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdle(boolean b) {
        isIdle.set(b);
        if (isIdleNow() && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
