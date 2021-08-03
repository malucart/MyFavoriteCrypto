/*
 Class focused on Type Writer Effect
 */

package com.luiza.luizacryptotracker.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.os.Handler;

// custom implementation of the TextView component
public class TypeWriter extends androidx.appcompat.widget.AppCompatTextView {

    private int mIndex;
    private long mDelay = 150; // in ms
    private CharSequence mText;

    public TypeWriter(Context context) {
        super(context);
    }

    // AttributeSet is to create a custom view
    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // when you create a new Handler, it is bound to the thread/message queue of the thread that is creating it
    private Handler mHandler = new Handler();

    // used to type each letter of the text to display at some defined delay by calling the postDelayed method of the Handler object
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder); // removeCallbacks simply removes those runnables who have not yet begun processing from the queue
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long m) {
        mDelay = m;
    }
}