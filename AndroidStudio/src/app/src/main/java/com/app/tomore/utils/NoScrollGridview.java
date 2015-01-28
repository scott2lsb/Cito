package com.app.tomore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;


public class NoScrollGridview extends GridView {   
	  private int position = 0;

    public NoScrollGridview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridview (Context context, AttributeSet attrs) {   
        super(context, attrs);   
    }   

  
    public NoScrollGridview (Context context, AttributeSet attrs, int defStyle) {   
        super(context, attrs, defStyle);   
    }   
    public void setCurrentPosition(int pos) {
        this.position = pos;
      }
    @SuppressLint("NewApi")
    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
      // TODO Auto-generated method stub
      super.setChildrenDrawingOrderEnabled(enabled);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
      if (i == childCount - 1) {
        return position;
      }
      if (i == position) {
        return childCount - 1;
      }
      return i;
    }

    @Override   
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
  
        int expandSpec = MeasureSpec.makeMeasureSpec(   
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
        super.onMeasure(widthMeasureSpec, expandSpec);   
    }   
    
}   