package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel;

import android.graphics.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FADrawer {

    public static final float RADIUS = 40;
    private Map<String, Paint> paints;

    private Canvas canvas = null;


    public void setCanvas(Canvas canvas) {
        //if (this.canvas == null)
            this.canvas = canvas;
    }


    public FADrawer() {

        this.paints = new HashMap<>();



        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);

        paints.put("state", paint);

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setTextSize(32.0f);
        paint1.setTextAlign(Paint.Align.CENTER);
        paint1.setAntiAlias(true);

        paints.put("stateText", paint1);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(2);
        paint2.setStyle(Paint.Style.STROKE);

        paints.put("arrow", paint2);

        Paint paint3 = new Paint();
        paint3.setColor(Color.argb(0.4f, 246, 255, 212));
        paint3.setAntiAlias(true);

        paints.put("triangle", paint3);

    }

    public void drawArrow(float startX, float startY, float endX, float endY) {

        double degree1 = calculateDegree(startX, endX, startY, endY);

        float _x = endX;
        _x = _x - (float)Math.sin(Math.toRadians(degree1)) * RADIUS;

        float _y = endY;
        _y = _y + (float)Math.cos(Math.toRadians(degree1)) * RADIUS;

        float endX1 = (float) (_x + ((10) * Math.cos(Math.toRadians((degree1-30)+90))));
        float endY1 = (float) (_y + ((10) * Math.sin(Math.toRadians(((degree1-30)+90)))));

        float endX2 = (float) (_x + ((10) * Math.cos(Math.toRadians((degree1-60)+180))));
        float endY2 = (float) (_y + ((10) * Math.sin(Math.toRadians(((degree1-60)+180)))));

        canvas.drawLine(_x,_y,endX1,endY1,paints.get("arrow"));
        canvas.drawLine(_x,_y,endX2,endY2,paints.get("arrow"));
    }

    public void drawTriangle(float x, float y) {

        Path triangle = new Path();
        triangle.moveTo(x, y);
        triangle.lineTo(x - 30, y - 40);
        triangle.lineTo(x - 30, y + 40);
        canvas.drawPath(triangle, paints.get("triangle"));
    }

    static public double calculateDegree(float x1, float x2, float y1, float y2) {
        float startRadians = (float) Math.atan((y2 - y1) / (x2 - x1));
        startRadians += ((x2 >= x1) ? 90 : -90) * Math.PI / 180;
        //System.out.println("radian=====" + Math.toDegrees(startRadians));
        return Math.toDegrees(startRadians);
    }

    public void drawTransitions(Map<String,State> states, ArrayList<Transition> transitions) {

        float x1, x2, y1, y2, middleX, middleY;
        for ( Transition value : transitions ) {
            x1 = states.get(String.valueOf(value.getStartStateId())).getX();
            x2 = states.get(String.valueOf(value.getEndStateId())).getX();
            y1 = states.get(String.valueOf(value.getStartStateId())).getY();
            y2 = states.get(String.valueOf(value.getEndStateId())).getY();

            if (value.getEndStateId() == value.getStartStateId()) {
                RectF oval = new RectF();
                oval.set(x1 - RADIUS + RADIUS*5 /9,
                        y1 - RADIUS*2,
                        x1 + RADIUS/3,
                        y1 + RADIUS / 2);

                canvas.drawArc(oval, 135, 270, false, paints.get("arrow"));
                this.drawArrow(x1 - RADIUS*10/9, y1 - RADIUS*5/2, x1, y1);
                canvas.drawText(value.getReadStr(), x1 - RADIUS/3, y1 - RADIUS*5/2, paints.get("stateText"));
            } else {
                middleX = (x2 + x1) / 2;
                middleY = (y1 + y2) / 2;

                canvas.drawLine(x1, y1, x2, y2, value.getPaint());
                drawArrow(x1, y1, x2, y2);
                canvas.drawText(value.getReadStr(), middleX, middleY, paints.get("stateText"));
            }

        }
    }

    public void drawStates(Map<String,State> states) {
        for ( State value : states.values() ) {
            canvas.drawCircle(value.getX(), value.getY(), RADIUS, paints.get("state"));
            canvas.drawCircle(value.getX(), value.getY(), RADIUS, paints.get("arrow"));
            canvas.drawText(value.getName(), value.getX(), value.getY() + 10, paints.get("stateText"));
            if (value.is_initial()) {
                drawTriangle(value.getX() - RADIUS, value.getY());
            }
            if (value.is_final()) {
                canvas.drawCircle(value.getX(), value.getY(), RADIUS - 10, paints.get("arrow"));
            }
        }
    }

    public void drawTransition(float x1, float y1, float x2, float y2) {
        canvas.drawLine(x1, y1, x2, y2, paints.get("arrow"));
    }


}
