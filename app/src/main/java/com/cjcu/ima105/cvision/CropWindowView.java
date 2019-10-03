package com.cjcu.ima105.cvision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CropWindowView extends View {
    class ColorBall {
        Bitmap bitmap;
        Context mContext;
        Point point;
        public ColorBall(Context context, int resourceId, Point point) {
            Drawable drawable = context.getResources().getDrawable(resourceId, context.getTheme());
            Canvas canvas = new Canvas();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            this.bitmap=bitmap;
            mContext = context;
            this.point = point;
        }

        public int getWidthOfBall() {
            return bitmap.getWidth();
        }

        public int getHeightOfBall() {
            return bitmap.getHeight();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public int getX() {
            return point.x;
        }

        public int getY() {
            return point.y;
        }

        public void setX(int x) {
            point.x = x;
        }

        public void setY(int y) {
            point.y = y;
        }
    }


    public Point point1, point3;
    public Point point2, point4;

    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    ArrayList< ColorBall > colorballs = new ArrayList<>();
    // array that holds the balls
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;
    Context context;

    public CropWindowView(Context context) {
        super(context);
        this.context=context;
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        Display display = ((AppCompatActivity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_width = size.x;
        int screen_height = (int)((4*1.f)*((screen_width*1.f)/(3*1.f)));
        int offset=screen_width/3;
        point1 = new Point();
        point1.x = offset;
        point1.y = offset;

        point2 = new Point();
        point2.x = screen_width-offset;
        point2.y = offset;

        point3 = new Point();
        point3.x = screen_width-offset;
        point3.y = screen_height-offset;

        point4 = new Point();
        point4.x = offset;
        point4.y = screen_height-offset;

        colorballs = new ArrayList<>();
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point1));
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point2));
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point3));
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point4));

    }

    public CropWindowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CropWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        Display display = ((AppCompatActivity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_width = size.x;
        int screen_height = (int)((4*1.f)*((screen_width*1.f)/(3*1.f)));
        int offset=500;
        point1 = new Point();
        point1.x = offset;
        point1.y = offset;

        point2 = new Point();
        point2.x = screen_width-offset;
        point2.y = offset;

        point3 = new Point();
        point3.x = screen_width-offset;
        point3.y = screen_height-offset;

        point4 = new Point();
        point4.x = offset;
        point4.y = screen_height-offset;

        colorballs = new ArrayList<>();
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point1));
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point2));
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point3));
        colorballs.add(new ColorBall(context, R.drawable.crop_window_corner, point4));

    }

    private Rect crop_rect;
    @Override
    protected void onDraw(Canvas canvas) {
        //fill
//        canvas.drawColor(Color.parseColor("#00FFFFFF"));
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.parseColor("#00FFFFFF"));
//        canvas.drawRect(point1.x + colorballs.get(0).getWidthOfBall() / 2,
//                point1.y + colorballs.get(0).getHeightOfBall() / 2,
//                point3.x + colorballs.get(2).getWidthOfBall() / 2,
//                point3.y + colorballs.get(2).getHeightOfBall() / 2,
//                paint);

        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.parseColor("#55FFFFFF"));
        crop_rect = new Rect(point1.x + colorballs.get(0).getWidthOfBall() / 2, point1.y + colorballs.get(0).getHeightOfBall() / 2, point3.x + colorballs.get(2).getWidthOfBall() / 2, point3.y + colorballs.get(2).getHeightOfBall() / 2);
        canvas.drawRect(crop_rect, paint);

        // draw the balls on the canvas
        for (ColorBall ball: colorballs) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(), new Paint());
        }
    }
    public Rectangle getCropLocationWithScaleToBitmapLocation(BigDecimal scale){
        Rectangle rectangle = new Rectangle();
        rectangle.x=new BigDecimal(crop_rect.left+"").multiply(scale).intValue();
        rectangle.y=new BigDecimal(crop_rect.top+"").multiply(scale).intValue();
        int br_x = new BigDecimal(crop_rect.right+"").multiply(scale).intValue();
        int br_y = new BigDecimal(crop_rect.bottom+"").multiply(scale).intValue();
        rectangle.width = br_x-rectangle.x+1;
        rectangle.height = br_y-rectangle.y+1;
        return rectangle;
    }



    private int on_touch_down_x;
    private int on_touch_down_y;
    int balID;
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                on_touch_down_x=X;
                on_touch_down_y=Y;
                for (ColorBall ball: colorballs) {
                    paint.setColor(Color.CYAN);
                    Rect rect = new Rect(ball.getX(), ball.getY(), ball.getX()+ball.getWidthOfBall()-1, ball.getY()+ball.getHeightOfBall()-1);
                    if (X>=rect.left && X<=rect.right && Y>=rect.top && Y<=rect.bottom) {
                        balID = colorballs.indexOf(ball);
                        break;
                    }
                    else {
                        balID=-1;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (balID > -1) {
                    paint.setColor(Color.CYAN);
                    int target_x = X-colorballs.get(balID).getWidthOfBall()/2;
                    int target_y = Y-colorballs.get(balID).getHeightOfBall()/2;
                    if(balID==0){
                        if(target_x<colorballs.get(1).getX()-(2*colorballs.get(1).getWidthOfBall()) && target_y<colorballs.get(3).getY()-(2*colorballs.get(3).getHeightOfBall()) &&
                                target_x>0 && target_y>0) {
                            colorballs.get(balID).setX(target_x);
                            colorballs.get(balID).setY(target_y);
                            colorballs.get(3).setX(target_x);
                            colorballs.get(1).setY(target_y);
                        }
                    }
                    else if(balID==1){
                        if(target_x>colorballs.get(0).getX()+(2*colorballs.get(0).getWidthOfBall()) && target_y<colorballs.get(2).getY()-(2*colorballs.get(2).getHeightOfBall()) &&
                                target_x+colorballs.get(balID).getWidthOfBall()<this.getWidth()-1 && target_y>0) {
                            colorballs.get(balID).setX(target_x);
                            colorballs.get(balID).setY(target_y);
                            colorballs.get(2).setX(target_x);
                            colorballs.get(0).setY(target_y);
                        }
                    }
                    else if(balID==2){
                        if(target_x>colorballs.get(3).getX()+(2*colorballs.get(3).getWidthOfBall()) && target_y>colorballs.get(1).getY()+(2*colorballs.get(1).getHeightOfBall()) &&
                                target_x+colorballs.get(balID).getWidthOfBall()<this.getWidth()-1 && target_y+colorballs.get(balID).getHeightOfBall()<this.getHeight()-1) {
                            colorballs.get(balID).setX(target_x);
                            colorballs.get(balID).setY(target_y);
                            colorballs.get(1).setX(target_x);
                            colorballs.get(3).setY(target_y);
                        }
                    }
                    else if(balID==3){
                        if(target_x<colorballs.get(2).getX()-(2*colorballs.get(2).getWidthOfBall()) && target_y>colorballs.get(0).getY()+(2*colorballs.get(0).getHeightOfBall()) &&
                                target_x>0 && target_y+colorballs.get(balID).getHeightOfBall()<this.getHeight()-1) {
                            colorballs.get(balID).setX(target_x);
                            colorballs.get(balID).setY(target_y);
                            colorballs.get(0).setX(target_x);
                            colorballs.get(2).setY(target_y);
                        }
                    }
                    invalidate();
                }
                else{
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_MOVE:
                            if(X>=colorballs.get(0).getX() && X<=colorballs.get(1).getX() && Y>=colorballs.get(0).getY() && Y<=colorballs.get(3).getY()) {
                                int x = (int) event.getX()-on_touch_down_x;
                                int y = (int) event.getY()-on_touch_down_y;
                                if((colorballs.get(0).getX()+x)>0 && (colorballs.get(1).getX()+x+colorballs.get(1).getWidthOfBall())<this.getWidth()-1 &&
                                        (colorballs.get(0).getY()+y)>0 && (colorballs.get(3).getY()+y+colorballs.get(3).getHeightOfBall())<this.getHeight()-1) {
                                    colorballs.get(0).setX(colorballs.get(0).getX() + x);
                                    colorballs.get(0).setY(colorballs.get(0).getY() + y);
                                    colorballs.get(1).setX(colorballs.get(1).getX() + x);
                                    colorballs.get(1).setY(colorballs.get(1).getY() + y);
                                    colorballs.get(2).setX(colorballs.get(2).getX() + x);
                                    colorballs.get(2).setY(colorballs.get(2).getY() + y);
                                    colorballs.get(3).setX(colorballs.get(3).getX() + x);
                                    colorballs.get(3).setY(colorballs.get(3).getY() + y);
                                    canvas.drawRect(point2.x, point4.y, point4.x, point2.y, paint);
                                    on_touch_down_x=(int) event.getX();
                                    on_touch_down_y=(int) event.getY();
                                }
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            break;

                        case MotionEvent.ACTION_DOWN:
                            break;
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }
}
