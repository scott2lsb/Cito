package com.app.tomore.ui.threads;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import android.util.DisplayMetrics; 

import com.app.tomore.MyCameraActivity;
import com.app.tomore.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.tomore.beans.ThreadImageModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;  

public class MyThreadActivity extends Activity {
	private DialogActivity dialog;
	private DisplayImageOptions otp;
	ListView listview;
	private Activity mContext;
	private ArrayList<ThreadModel> threadmodel;
	private ArrayList<ThreadImageModel> threadImageList;
	ThreadModel ThreadItem;
	ThreadImageModel ThreadImageitem;
	private ListView listView;
	private DisplayMetrics dm = new DisplayMetrics();  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_thread);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		Intent intent = getIntent();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		listView  = (ListView) findViewById(R.id.mythreadactivity_listview);
		mContext = this;
		new GetData(MyThreadActivity.this, 1).execute("");

		
	}
	
	private class GetData extends AsyncTask<String, String, String>{
		
		private int mType;
		private GetData(Context context, int type) {
			// this.mContext = context;
			this.mType = type;
			dialog = new DialogActivity(context, type);
		}
		@Override
		protected void onPreExecute() {
			if (mType == 1) {
				if (null != dialog && !dialog.isShowing()) {
					dialog.show();
				}
			}
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			String result1 =null;
			ThreadsRequest request = new ThreadsRequest(MyThreadActivity.this);
			try {
				int page =1;
				int limit=1000;
				int memberID=25;
				Log.d("doInBackground", "start request");
				result = request.getThreadListByMemberID(limit, page, memberID);
				Log.d("doInBackground", "returned");
			}catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				// show empty alert
			} else {
				
				try {
					threadmodel =new ThreadsParse().getThreadListByMemberIDParse(result);
					//threadImageList=new ThreadsParse().getThreadimageByMemberIDParse(result);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if(threadmodel !=null){
					Intent intent =new Intent(MyThreadActivity.this,MyCameraActivity.class);
					intent.putExtra("threadslist",(Serializable) threadmodel);
					// startActivity(intent);
				}
				else{
					// show empty alert
				}
			}
		}

	}
	private void BindDataToListView() {
		
		MyThreadAdapter newsListAdapter = new MyThreadAdapter(mContext, dm.widthPixels);
		//dm.widthPixels
		listView.setAdapter(newsListAdapter);
	
	}
	
	class ViewHolder {
		private TextView ThreadContent;
	    private ImageView ThreadImage;
	    private TextView TimeDiff;
	    private View actionLayout;    
	    private Button btOne; 
	    private View normalItemContentLayout; 
	    private HorizontalScrollView itemHorizontalScrollView;

	}
	
	 class MyThreadAdapter extends BaseAdapter implements View.OnClickListener{

		    private List<Integer> colors;  
		    /** 
		     * context上下文,用来获得convertView 
		     */  
		    private Context nContext;  
		    /** 
		     * 屏幕宽度,由于我们用的是HorizontalScrollView,所以按钮选项应该在屏幕外 
		     */  
		    private int mScreentWidth;  
		  
		    /** 
		     * 构造方法 
		     * @param context 
		     * @param screenWidth 
		     */  
		    public MyThreadAdapter(Context context, int screenWidth)  
		    {  
		        //初始化  
		    	nContext = context;  
		        mScreentWidth = screenWidth;  
		         
		    }  
		  
		  
		    @Override  
		    public void onClick(View v)  
		    {  
		        int position = (Integer) v.getTag();  
		        switch (v.getId())  
		        {  
		            case R.id.button1:  
		  
		                //colors.add(R.color.blue);  
		                break;  
		  
		  
		            default:  
		                break;  
		        }  
		        //刷新ListView内容  
		        notifyDataSetChanged();  
		    }  
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return threadmodel.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return threadmodel.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
				viewHolder = new ViewHolder();  
				ThreadItem =(ThreadModel) getItem(position);
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.thread_myactivity, parent, false);
				viewHolder.itemHorizontalScrollView = (HorizontalScrollView) convertView.findViewById(R.id.hsv);  
				viewHolder.ThreadImage=(ImageView) convertView.findViewById(R.id.Mythreadimage);
				viewHolder.ThreadContent = (TextView) convertView.findViewById(R.id.Mythreadcontent);
				viewHolder.TimeDiff=(TextView) convertView.findViewById(R.id.MythreadTimeDiff);

				viewHolder.actionLayout = convertView.findViewById(R.id.ll_action);  
				viewHolder.btOne = (Button) convertView.findViewById(R.id.button1);   
	  
				viewHolder.btOne.setTag(position);  

	  	  
	            //设置内容view的大小为屏幕宽度,这样按钮就正好被挤出屏幕外  
				viewHolder.normalItemContentLayout = convertView.findViewById(R.id.ll_content);  
	            LayoutParams lp = viewHolder.normalItemContentLayout.getLayoutParams();  
	            lp.width = mScreentWidth; 
	  
	            convertView.setTag(viewHolder);  
	  
	        	//viewHolder = (ViewHolder) convertView.getTag();  
	  
	        //设置监听事件  
	        convertView.setOnTouchListener(new View.OnTouchListener()  
	        {  
	            @Override  
	            public boolean onTouch(View v, MotionEvent event)  
	            {  
	                switch (event.getAction())  
	                {  
	                    case MotionEvent.ACTION_UP:  
	                          
	                        //获得ViewHolder  
	                        ViewHolder Holder = (ViewHolder) v.getTag();  
	                          
	                        //获得HorizontalScrollView滑动的水平方向值.  
	                        int scrollX = Holder.itemHorizontalScrollView.getScrollX();  
	                          
	                        //获得操作区域的长度  
	                        int actionW = Holder.actionLayout.getWidth();  
	                          
	                        //注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬  
	                        //如果水平方向的移动值<操作区域的长度的一半,就复原  
	                        if (scrollX < actionW / 2)  
	                        {  
	                        	Holder.itemHorizontalScrollView.smoothScrollTo(0, 0);  
	                        }  
	                        else//否则的话显示操作区域  
	                        {  
	                        	Holder.itemHorizontalScrollView.smoothScrollTo(actionW, 0);  
	                        }  
	                        return true;  
	                }  
	                return false;  
	            }  
	        });  
	  
	        //这里防止删除一条item后,ListView处于操作状态,直接还原  
	        if (viewHolder.itemHorizontalScrollView.getScrollX() != 0)  
	        {  
	        	viewHolder.itemHorizontalScrollView.scrollTo(0, 0);  
	        } 
	          
	        //设置背景颜色,设置填充内容.  
	  
	        //设置监听事件  
	        viewHolder.btOne.setOnClickListener(this);  
	  
			// TODO Auto-generated method stub

			ImageLoader.getInstance().displayImage(ThreadItem.getThreadImageList().get(0).getImageUrl(),
					viewHolder.ThreadImage,otp);
			viewHolder.ThreadContent.setText(ThreadItem.getThreadContent());
			viewHolder.TimeDiff.setText(ThreadItem.getTimeDiff());
			return convertView;
		}
	}
}
