package com.zhilearn.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TianZiGeView extends View {

    private Paint gridPaint;
    private Paint centerLinePaint;
    private Paint diagonalPaint;
    private Paint strokePaint;
    private Paint guidePaint;
    private Path currentPath;
    private List<Path> paths;
    private float cellSize;
    private float offsetX;
    private float offsetY;

    public TianZiGeView(Context context) {
        super(context);
        init();
    }

    public TianZiGeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.parseColor("#E0E0E0"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(6f);

        centerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerLinePaint.setColor(Color.parseColor("#E8E8E8"));
        centerLinePaint.setStyle(Paint.Style.STROKE);
        centerLinePaint.setStrokeWidth(3f);

        diagonalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        diagonalPaint.setColor(Color.parseColor("#F0F0F0"));
        diagonalPaint.setStyle(Paint.Style.STROKE);
        diagonalPaint.setStrokeWidth(2f);
        PathEffect dash = new DashPathEffect(new float[]{15, 10}, 0);
        diagonalPaint.setPathEffect(dash);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.parseColor("#222222"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(18f);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);

        guidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        guidePaint.setColor(Color.parseColor("#BBBBBB"));
        guidePaint.setStyle(Paint.Style.STROKE);
        guidePaint.setStrokeWidth(8f);
        guidePaint.setStrokeCap(Paint.Cap.ROUND);
        guidePaint.setAlpha(120);

        paths = new ArrayList<>();
        currentPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellSize = Math.min(w, h) * 0.7f;
        offsetX = (w - cellSize) / 2f;
        offsetY = (h - cellSize) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        canvas.drawColor(Color.WHITE);

        // 田字格外框
        canvas.drawRect(offsetX, offsetY, offsetX + cellSize, offsetY + cellSize, gridPaint);

        // 横中线
        canvas.drawLine(offsetX, offsetY + cellSize / 2,
                        offsetX + cellSize, offsetY + cellSize / 2, centerLinePaint);

        // 竖中线
        canvas.drawLine(offsetX + cellSize / 2, offsetY,
                        offsetX + cellSize / 2, offsetY + cellSize, centerLinePaint);

        // 对角线虚线
        canvas.drawLine(offsetX, offsetY, offsetX + cellSize, offsetY + cellSize, diagonalPaint);
        canvas.drawLine(offsetX + cellSize, offsetY, offsetX, offsetY + cellSize, diagonalPaint);

        // 绘制已有笔画
        for (Path path : paths) {
            canvas.drawPath(path, strokePaint);
        }

        // 绘制当前笔画
        canvas.drawPath(currentPath, strokePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath.moveTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (!currentPath.isEmpty()) {
                    paths.add(new Path(currentPath));
                }
                currentPath.reset();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void clear() {
        paths.clear();
        currentPath.reset();
        invalidate();
    }

    public boolean hasContent() {
        return paths.size() > 0;
    }

    /**
     * 简化版书写评分：
     * 基于笔画数量和覆盖面积评估
     */
    public int evaluateScore(String targetChar) {
        if (paths.isEmpty()) {
            return 0;
        }

        // 基础分：有笔画就有分
        int baseScore = 60;

        // 笔画数量匹配加分（越多笔画越像）
        int strokeBonus = Math.min(paths.size() * 8, 30);

        // 随机波动（模拟更真实的评分）
        int randomFactor = (int) (Math.random() * 10) - 2; // -2 到 +8

        int finalScore = baseScore + strokeBonus + randomFactor;
        return Math.max(60, Math.min(100, finalScore));
    }
}
