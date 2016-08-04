package com.yzd.mylibrary.tabbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzd.mylibrary.R;

public class IndicatorLayout extends LinearLayout implements View.OnClickListener {
    public IndicatorLayout(Context context) {
        super(context);
    }

    public IndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttr(attrs);
    }

    public IndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttr(attrs);
    }

    public void setTabs(int[] texts, int[] icons) {
        this.texts = texts;
        this.icons = icons;

//        updateView();
    }

    private int[] texts;

    private int[] icons;

    private int indicatorColor;

    private int location;

    private int textRes;

    private int width;

    private int indicatorWidth;

    private int curSelectIndex;

    private View topIndicator;
    private View bottomIndicator;
    private LinearLayout contentLl;

    private View indicator;

    private static final int TOP = 1;
    private static final int BOTTOM = 2;

    private void updateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.indicator_layout, null);

        topIndicator = view.findViewById(R.id.view_indicator_top);
        bottomIndicator = view.findViewById(R.id.view_indicator_bottom);
        contentLl = (LinearLayout) view.findViewById(R.id.ll_indicator_content);

        if (location == TOP) {
            topIndicator.setVisibility(View.VISIBLE);
            bottomIndicator.setVisibility(View.GONE);
            indicator = topIndicator;
        } else if (location == BOTTOM) {
            topIndicator.setVisibility(View.GONE);
            bottomIndicator.setVisibility(View.VISIBLE);
            indicator = bottomIndicator;
        } else {
            topIndicator.setVisibility(View.GONE);
            bottomIndicator.setVisibility(View.GONE);
        }

        indicatorWidth = width / texts.length;

        if (null != indicator) {
            indicator.setBackgroundColor(getResources().getColor(indicatorColor));

            LayoutParams params = (LayoutParams) indicator.getLayoutParams();
            params.width = indicatorWidth;
        }

        LayoutParams contentParams = (LayoutParams) contentLl.getLayoutParams();
        contentParams.width = width;

        for (int i = 0; i < texts.length; i++) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(textRes, null);
            textView.setText(texts[i]);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, icons[i], 0, 0);
            textView.setTag(i);

            ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(indicatorWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(tvParams);
            if (i == 0) {
                textView.setSelected(true);
                curSelectIndex = 0;
            }

            textView.setOnClickListener(this);

            contentLl.addView(textView);
        }
        addView(view);
    }

    private void parseAttr(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Indicator);

        indicatorColor = a.getResourceId(R.styleable.Indicator_bgcolor, Color.parseColor("#FF0000"));
        location = a.getInt(R.styleable.Indicator_location, TOP);
        textRes = a.getResourceId(R.styleable.Indicator_textRes, -1);

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;

        updateView();
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();

        if (null != indicator) {
            TranslateAnimation animation = new TranslateAnimation(indicatorWidth * curSelectIndex, indicatorWidth * tag, 0, 0);
            animation.setDuration(500);
            animation.setFillAfter(true);
            indicator.startAnimation(animation);
        }

        for (int i = 0; i < contentLl.getChildCount(); i++) {
            contentLl.getChildAt(i).setSelected(tag == i);
        }

        curSelectIndex = tag;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
