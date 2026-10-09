package com.zhilearn.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class RadarView extends View {

    private Paint axisPaint;
    private Paint gridPaint;
    private Paint dataPaint;
    private Paint fillPaint;
    private Paint labelPaint;

    private float[] values = {0.8f, 0.6f, 0.7f, 0.5f, 0.65f};
    private String[] labels = {"字形", "读音", "释义", "书写", "运用"};
    private int centerX;
    private int centerY;
    private float radius;
    private int numAxes = 5;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.parseColor("#CCCCCC"));
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setStrokeWidth(2f);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.parseColor("#E8E8E8"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1f);

        dataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dataPaint.setColor(Color.parseColor("#FF6B35"));
        dataPaint.setStyle(Paint.Style.STROKE);
        dataPaint.setStrokeWidth(4f);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.parseColor("#FF6B35"));
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAlpha(80);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.parseColor("#333333"));
        labelPaint.setTextSize(36f);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w, h) / 2f * 0.75f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制网格圈
        for (int ring = 1; ring <= 4; ring++) {
            float ringRadius = radius * ring / 4f;
            Path ringPath = new Path();
            for (int i = 0; i < numAxes; i++) {
                double angle = Math.toRadians(270 + i * 360.0 / numAxes);
                float x = centerX + (float) (ringRadius * Math.cos(angle));
                float y = centerY + (float) (ringRadius * Math.sin(angle));
                if (i == 0) ringPath.moveTo(x, y);
                else ringPath.lineTo(x, y);
            }
            ringPath.close();
            canvas.drawPath(ringPath, gridPaint);
        }

        // 绘制轴线
        for (int i = 0; i < numAxes; i++) {
            double angle = Math.toRadians(270 + i * 360.0 / numAxes);
            float endX = centerX + (float) (radius * Math.cos(angle));
            float endY = centerY + (float) (radius * Math.sin(angle));
            canvas.drawLine(centerX, centerY, endX, endY, axisPaint);

            // 标签
            float labelX = centerX + (float) ((radius + 50) * Math.cos(angle));
            float labelY = centerY + (float) ((radius + 50) * Math.sin(angle)) + 12;
            canvas.drawText(labels[i], labelX, labelY, labelPaint);
        }

        // 绘制数据区域
        Path dataPath = new Path();
        for (int i = 0; i < numAxes; i++) {
            double angle = Math.toRadians(270 + i * 360.0 / numAxes);
            float val = values[i];
            float px = centerX + (float) (radius * val * Math.cos(angle));
            float py = centerY + (float) (radius * val * Math.sin(angle));
            if (i == 0) dataPath.moveTo(px, py);
            else dataPath.lineTo(px, py);
        }
        dataPath.close();

        canvas.drawPath(dataPath, fillPaint);
        canvas.drawPath(dataPath, dataPaint);
    }

    public void setValues(float[] values) {
        if (values != null && values.length == numAxes) {
            this.values = values;
            invalidate();
        }
    }
}
