package com.thales.github.utilities;

import android.support.test.espresso.IdlingResource;
import android.view.View;

public class ViewVisibilityIdlingResource implements IdlingResource {

    private final View view;
    private final int expectedVisibility;

    private boolean idle;
    private ResourceCallback resourceCallback;

    public ViewVisibilityIdlingResource(final View view, final int expectedVisibility) {
        this.view = view;
        this.expectedVisibility = expectedVisibility;
        this.idle = false;
        this.resourceCallback = null;
    }

    @Override
    public final String getName() {
        return ViewVisibilityIdlingResource.class.getSimpleName();
    }

    @Override
    public final boolean isIdleNow() {
        idle = idle || view.getVisibility() == expectedVisibility;

        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }

        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
