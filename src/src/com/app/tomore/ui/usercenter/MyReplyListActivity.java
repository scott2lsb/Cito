package com.app.tomore.ui.usercenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.app.tomore.MyCameraActivity;
import com.app.tomore.R;
import com.app.tomore.R.layout;
import com.app.tomore.beans.ThreadCmtModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.ThreadUpdateModel;
import com.app.tomore.beans.UpdateFollowedModel;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.ui.threads.MyThreadActivity;
import com.app.tomore.ui.yellowpage.RestaurantDetailActivity.GridViewAdapter;
import com.app.tomore.ui.yellowpage.RestaurantDetailActivity.GridViewCache;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
//import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyReplyListActivity extends Activity {
	private DialogActivity dialog;
	private DisplayImageOptions otp;
	ListView listview;
	private Activity mContext;
	private ArrayList<ThreadUpdateModel> threadupdatemodel;
	ThreadUpdateModel threadupdateitem;
	private ListView listView2;
	private ArrayList<ThreadCmtModel> threadcmtmodel;
	ThreadCmtModel threadcmtitem;
	private ArrayList<UpdateFollowedModel> followmodel;
	UpdateFollowedModel followmodelitem;
	private View layout;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reply_list);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		Intent intent = getIntent();
		new GetData(MyReplyListActivity.this, 1).execute("");
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		mContext = this;
		layout = findViewById(R.id.Activity_my_replay_list_layout);
		//LinearLayout whole_layout = (LinearLayout)findViewById(R.id.GeneralBLLayout);
		TextView header_Text = (TextView) layout.findViewById(R.id.btMeg);
		header_Text.setText(R.string.MyReply);
		final Button btnBack = (Button) layout.findViewById(R.id.bar_title_bl_go_back);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		

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
			UserCenterRequest request = new UserCenterRequest(MyReplyListActivity.this);
			try {

				String memberID="45";
				Log.d("doInBackground", "start request");
				result = request.getUserUpdate(memberID);
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
					threadupdatemodel =new UserCenterParse().praserMyUpdateModel(result);
					//threadImageList=new ThreadsParse().getThreadimageByMemberIDParse(result);
					//followmodel=new UserCenterParse().getThreadcmtParse(result);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if(threadcmtmodel !=null){
					Intent intent =new Intent(MyReplyListActivity.this,MyCameraActivity.class);
					intent.putExtra("threadupdatelist",(Serializable) threadcmtmodel);
					// startActivity(intent);
				}
				else{
					// show empty alert
				}
			}
		}

	}
      private void BindDataToListView() {
    	  final List<ImageAndText> imageAndTexts = new ArrayList<ImageAndText>();
    	
          int p ;
          int q;
          String information = getString(R.string.Likeinfo);
          String information1 = getString(R.string.Commentinfo);
          Drawable drawable = this.getResources().getDrawable(R.drawable.likednew);
    	  for(ThreadUpdateModel c: threadupdatemodel)
    	  {  
    		     q = c.getLikeList().size();
        	     for (int i =0;i<q;){
        	    	  imageAndTexts.add(new ImageAndText(c.getLikeList().get(i).getImage(), c.getLikeList().get(i).getAccountName(), information,drawable));
    	    		  i++;
        	     }
    		  
    		  p = c.getCommentList().size();
    	     for (int i =0;i<p;){
    		  imageAndTexts.add(new ImageAndText(c.getCommentList().get(i).getMemberImage(), c.getCommentList().get(i).getCommentContent(), information1,null));
    		  i++;
    	
    	     }
    
    	     
    	  }
	        ListView listView = (ListView) findViewById(R.id.myreplaylistactivity_listview);
			listView.setAdapter(new MyReplyAdapter(this, imageAndTexts,
					listView));

    }
  	class ViewHolder {
		private TextView membername;
	    private ImageView memberimage;
	    private TextView comment;
	}
  	private class ImageAndText
	{
	 private String imageUrl;          
	    private String text;   
	    private String text1;
	    private Drawable image1;
	    
	        public ImageAndText(String imageUrl, String text,String text1, Drawable image1) {  
	            this.imageUrl = imageUrl;  
	            this.text = text;  
	            this.text1 = text1;
	            this.image1=image1;
	       }  
	        public String getImageUrl() {  
	            return imageUrl;  
	       }  

	       public String getText() {  
	           return text;  
	        }  
	       public String getText1() {  
	           return text1;  
	        }  
	       public Drawable getImage1(){
	    	   return image1;
	       }
	}
  	public class MyReplyAdapter extends ArrayAdapter<ImageAndText>{
  		   
  		    private ListView listview;  
	        public MyReplyAdapter(Activity activity, List<ImageAndText> imageAndTexts, ListView listview2) {  
	            super(activity, 0, imageAndTexts);  
	            this.listview = listview2;  
	            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));

	        }  
	        public View getView(int position, View convertView, ViewGroup parent) {
	        	Activity activity = (Activity) getContext(); 
	            View rowView = convertView;  
	            ListViewCache viewCache;  
	        
	        if (rowView == null) {  
                LayoutInflater inflater = activity.getLayoutInflater();  
                rowView = inflater.inflate(R.layout.myreplay_activity_usercenter, null);  
                viewCache = new ListViewCache(rowView);  
                rowView.setTag(viewCache);  
            } else {  
                viewCache = (ListViewCache) rowView.getTag();  
            }  
            ImageAndText imageAndText = getItem(position);  
            final String imageUrl = imageAndText.getImageUrl();  
            ImageView imageView = viewCache.getImageView();  
            imageView.setTag(imageUrl);  
            ImageLoader.getInstance().displayImage(imageUrl,
            		imageView);

            final Drawable drawable= imageAndText.getImage1();
            ImageView imageView1 = viewCache.getImageView1();  
            imageView1.setImageDrawable(drawable);

            // Set the text on the TextView  
            TextView textView = viewCache.getTextView();  
            textView.setText(imageAndText.getText());  
            TextView textView1 = viewCache.getTextView1();  
            textView1.setText(imageAndText.getText1());  

            return rowView;  
  	}
  	}
  	public class ListViewCache {

	    private View baseView;
	    private TextView textView;
	    private ImageView imageView;
	    private TextView textView1;
	    private ImageView imageView1;


	    public ListViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public TextView getTextView() {
	        if (textView == null) {
	            textView = (TextView) baseView.findViewById(R.id.Myreplymembercontent);
	        }
	        return textView;
	    }
	    public TextView getTextView1() {
	        if (textView1 == null) {
	            textView1 = (TextView) baseView.findViewById(R.id.Myreplyinfo);
	        }
	        return textView1;
	    }


	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.Myreplymemberimage);
	        }
	        return imageView;
	    }
	    public ImageView getImageView1(){
	    	 if (imageView1 == null) {
		            imageView1 = (ImageView) baseView.findViewById(R.id.heart);
		        }
		        return imageView1;
	    	
	    }

}
}
