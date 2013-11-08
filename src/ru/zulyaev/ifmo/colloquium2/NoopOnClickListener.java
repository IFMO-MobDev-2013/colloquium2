package ru.zulyaev.ifmo.colloquium2;

import android.content.DialogInterface;

/**
 * author: zulyaev
 */
public class NoopOnClickListener implements DialogInterface.OnClickListener {
    public static final NoopOnClickListener INSTANCE = new NoopOnClickListener();

    private NoopOnClickListener() {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
}
