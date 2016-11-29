package playfire.top.scalerule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liuhuijie on 2016-06-08 09:44.
 */
public class HorizontalScaleScrollView extends BaseScaleView {

    private  int color= Color.WHITE;
    private  boolean islide=true;

    public HorizontalScaleScrollView(Context context) {
        super(context);
    }

    public HorizontalScaleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getColor() {
        return color;
    }

    public  void setColor(int color){
        this.color= color;
    }

    public  void setSlide(boolean islide){
        this.islide=islide;
    }
    @Override
    protected void initVar() {
        mRectWidth = (mMax - mMin) * mScaleMargin;
        mRectHeight = mScaleHeight * 8;
        mScaleMaxHeight = mScaleHeight * 2;

        // 设置layoutParams
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(mRectWidth, mRectHeight);
        this.setLayoutParams(lp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = View.MeasureSpec.makeMeasureSpec(mRectHeight, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
        mScaleScrollViewRange = getMeasuredWidth();
        mTempScale = mScaleScrollViewRange / mScaleMargin / 2 + mMin;
        mMidCountScale = mScaleScrollViewRange / mScaleMargin / 2 + mMin;
    }

    @Override
    protected void onDrawLine(Canvas canvas, Paint paint) {
        //去掉底色白线
       // canvas.drawLine(0, mRectHeight, mRectWidth, mRectHeight, paint);
    }

    @Override
    protected void onDrawScale(Canvas canvas, Paint paint) {

        paint.setTextSize(mRectHeight / 4);

        for (int i = 0, k = mMin; i <= mMax - mMin; i++) {
            if (i % 10 == 0) { //整值
                canvas.drawLine(i * mScaleMargin, 0, i * mScaleMargin, mRectHeight / 2, paint);
                //整值文字
//                canvas.drawText(String.valueOf(k), i * mScaleMargin, mRectHeight - mScaleMaxHeight - 20, paint);
                if (tag==0) {
                    canvas.drawText(String.valueOf(k), i * mScaleMargin, mRectHeight - mScaleMaxHeight, paint);
                }else if (tag==1){
                    canvas.drawText(String.valueOf(k / 2), i * mScaleMargin, mRectHeight - mScaleMaxHeight, paint);
                }
                k += 10;
            } else if (i % 5 == 0) {
                canvas.drawLine(i * mScaleMargin, 0, i * mScaleMargin, mRectHeight / 3, paint);
            } else {
//                canvas.drawLine(i * mScaleMargin, mRectHeight - 2 * mScaleHeight, i * mScaleMargin, mRectHeight - mScaleHeight, paint);
                canvas.drawLine(i * mScaleMargin, 0, i * mScaleMargin, mRectHeight / 4, paint);
            }
        }

    }

    @Override
    protected void onDrawPointer(Canvas canvas, Paint paint) {
        //去掉中间红色刻度标记线
        //paint.setColor(Color.RED);

        //每一屏幕刻度的个数/2
        int countScale = mScaleScrollViewRange / mScaleMargin / 2;
        //根据滑动的距离，计算指针的位置【指针始终位于屏幕中间】
        int finalX = mScroller.getFinalX();
        //滑动的刻度
        int tmpCountScale = (int) Math.rint((double) finalX / (double) mScaleMargin); //四舍五入取整
        //总刻度
        mCountScale = tmpCountScale + countScale + mMin;
        if (mScrollListener != null) { //回调方法
            mScrollListener.onScaleScroll(mCountScale);
        }
        //去掉中间的刻度线
        //  canvas.drawLine(countScale * mScaleMargin + finalX, mRectHeight,
        //        countScale * mScaleMargin + finalX, mRectHeight - mScaleMaxHeight - mScaleHeight, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!islide)return true;
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mScrollLastX = x;
                return true;
            case MotionEvent.ACTION_MOVE:
                int dataX = mScrollLastX - x;
                if (mCountScale - mTempScale < 0) { //向右边滑动
                    if (mCountScale <= mMin && dataX <= 0) //禁止继续向右滑动
                        return super.onTouchEvent(event);
                } else if (mCountScale - mTempScale > 0) { //向左边滑动
                    if (mCountScale >= mMax && dataX >= 0) //禁止继续向左滑动
                        return super.onTouchEvent(event);
                }
                smoothScrollBy(dataX, 0);
                mScrollLastX = x;
                postInvalidate();
                mTempScale = mCountScale;
                return true;
            case MotionEvent.ACTION_UP:
                if (mCountScale < mMin) mCountScale = mMin;
                if (mCountScale > mMax) mCountScale = mMax;
                int finalX = (mCountScale - mMidCountScale) * mScaleMargin;
                mScroller.setFinalX(finalX); //纠正指针位置
                postInvalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }
    //根据滑动的距离，计算指针的位置【指针始终位于屏幕中间】
  //  int finalX = mScroller.getFinalX();
    //滑动的刻度
   // int tmpCountScale = (int) Math.rint((double) finalX / (double) mScaleMargin); //四舍五入取整
    //总刻度
   // mCountScale = tmpCountScale + countScale + mMin;
   // slogger.e("aaaaaaaaaaa"+mCountScale+"aaaaaaaa"+tmpCountScale+"aaaaaaaa"+countScale+"aaaaaaa"+mMin);

    /**
     * 设置默认刻度
     * @param numer
     * @param screenWidth
     */
    public void setDefaultNumber(int numer,int screenWidth){

        int countScale = screenWidth / mScaleMargin / 2;
        int finalX = (numer-countScale-mMin) * mScaleMargin;
        smoothScrollBy(finalX,0);
        postInvalidate();

    }
}

