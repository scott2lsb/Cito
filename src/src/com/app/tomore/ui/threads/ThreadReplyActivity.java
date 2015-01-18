package com.app.tomore.ui.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.ToMoreParse;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.app.tomore.R;
import com.app.tomore.utils.AppUtil;
import com.app.tomore.utils.SpUtils;
import com.app.tomore.utils.ToastUtils;
import com.app.tomore.utils.PullToRefreshListView;
import com.app.tomore.utils.PullToRefreshBase;
import com.app.tomore.utils.PullToRefreshBase.OnLastItemVisibleListener;
import com.app.tomore.utils.PullToRefreshBase.OnRefreshListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.beans.ThreadModel;
import android.widget.AdapterView.OnItemClickListener;
import com.app.tomore.beans.ThreadCmtModel;

public class ThreadReplyActivity extends Activity {
	private DialogActivity dialog;
	private ArrayList<ThreadCmtModel> commentList;
	private ThreadCmtModel commentItem;
	private DisplayImageOptions otp;
	private PullToRefreshListView mListView;
	private Activity mContext;
	private TextView noneData;
	private View no_net_lay;
	ThreadDetailsActivity threadAdapter;
	private boolean headerRefresh = false;
	private int page = 1;
	private int num = 20;
	private ThreadModel threadModel;
	private Button submit;
	private EditText content;
	private String userId;
	private String finalResult = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread_reply);
		mContext = this;
		commentList = new ArrayList<ThreadCmtModel>();
		threadModel = (ThreadModel) getIntent().getSerializableExtra(
				"threadModel");
		commentList = threadModel.getThreadCmtList();
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));

		mListView = (PullToRefreshListView) findViewById(R.id.mag_listviews);
		mListView.setOnRefreshListener(onRefreshListener);
		mListView.setOnLastItemVisibleListener(onLastItemVisibleListener);
		mListView.setOnItemClickListener(itemClickListener);
		noneData = (TextView) findViewById(R.id.noneData);
		no_net_lay = findViewById(R.id.no_net_lay);

		no_net_lay = findViewById(R.id.no_net_lay);
		submit = (Button) findViewById(R.id.commentSubmit);
		content = (EditText) findViewById(R.id.commentContent);
		content.setText("");

		BindDataToListView();

		RelativeLayout rl = (RelativeLayout) getWindow().getDecorView()
				.findViewById(R.id.bar_title_mythread);
		final Button btnBack = (Button) rl
				.findViewById(R.id.bar_title_blocked_go_back);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View viewIn) {
				userId = SpUtils.getUserId(ThreadReplyActivity.this);
				if (content.getText().toString() == null
						|| content.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入内容",
							Toast.LENGTH_SHORT).show();
				} else {
					if (userId != null) {
						new GetData1(ThreadReplyActivity.this, 1).execute("");
					} else {
						AppUtil.startLoginPage(mContext);

					}
				}
			}
		});
	}

	private void BindDataToListView() {

		if (threadAdapter == null) {
			threadAdapter = new ThreadDetailsActivity();
			mListView.setAdapter(threadAdapter);
		} else {
			threadAdapter.notifyDataSetChanged();
		}
		if (commentList != null && commentList.size() > 0) {
			showDataUi();
		} else {
			showNoDataUi();
		}
	}

	void showDataUi() {
		mListView.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		no_net_lay.setVisibility(View.GONE);
	}

	void showNoDataUi() {
		mListView.setVisibility(View.GONE);
		noneData.setVisibility(View.VISIBLE);
		no_net_lay.setVisibility(View.GONE);
	}

	protected void showNoNetUi() {
		no_net_lay.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
	}

	private class GetData extends AsyncTask<String, String, String> {
		private int mType;

		private GetData(Context context, int type) {
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
			String result = null;
			ThreadsRequest request = new ThreadsRequest(
					ThreadReplyActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.getCommentsByThreadID(num, page,
						Integer.parseInt(threadModel.getThreadID()));
				Log.d("doInBackground", "returned");
			} catch (IOException e) {
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
			mListView.onRefreshComplete();
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				ToastUtils.showToast(mContext, "列表为空");
			} else {
				if (commentList != null && commentList.size() > 0) {
					if (headerRefresh)
						commentList = new ArrayList<ThreadCmtModel>();
				} else {
					commentList = new ArrayList<ThreadCmtModel>();
				}

				try {
					ArrayList<ThreadCmtModel> tempCommentList = new ThreadsParse()
							.getCommentsByThreadIDParse(result);
					commentList.addAll(tempCommentList);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!AppUtil.networkAvailable(mContext)) {
				ToastUtils.showToast(mContext, "列表为空");
				return;
			}
			if (commentList == null) {
				return;
			}
			Object obj = (Object) commentList.get(position - 1);
			if (obj instanceof String) {
				return;
			}

			// Intent intent = new Intent(MainMagActivity.this,
			// MagDetailActivity.class);
			// intent.putExtra("articleList", (Serializable) obj);
			// startActivity(intent);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			mListView.onRefreshComplete();
		}
	};

	public OnRefreshListener<ListView> onRefreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			if (AppUtil.networkAvailable(mContext)) {
				headerRefresh = true;
				page = 1;
				new GetData(ThreadReplyActivity.this, 1).execute("");
			} else {
				ToastUtils.showToast(mContext, "到头了");
				mListView.onRefreshComplete();
			}
		}
	};

	private OnLastItemVisibleListener onLastItemVisibleListener = new OnLastItemVisibleListener() {
		@Override
		public void onLastItemVisible() {
			if (AppUtil.networkAvailable(mContext)) {
				headerRefresh = false;
				page++;
				new GetData(ThreadReplyActivity.this, 1).execute("");

			} else {
				ToastUtils.showToast(mContext, "到头了");
			}
		}
	};

	class ViewHolder {
		TextView textViewTitle;
		TextView textViewComment;
		TextView TimeDiff;
		ImageView imageView;
	}

	private class ThreadDetailsActivity extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			commentItem = (ThreadCmtModel) getItem(position);
			final String speakerName = commentItem.getAccountName();
			final String content = commentItem.getCommentContent();
			final String time = commentItem.getCommentPostDate();
			final String imageUrl = commentItem.getMemberImage();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_list_item, null);
			viewHolder.textViewTitle = (TextView) convertView
					.findViewById(R.id.speakerName);
			viewHolder.textViewTitle.setText(speakerName);

			viewHolder.textViewComment = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.textViewComment.setText(content);

			viewHolder.TimeDiff = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.TimeDiff.setText(time);

			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.memberImage);

			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageView, otp);
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return commentList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	private class GetData1 extends AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;

		private GetData1(Context context, int type) {
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
			String result = null;
			ThreadsRequest request = new ThreadsRequest(
					ThreadReplyActivity.this);
			try {
				Log.d("doInBackground", "start request");
				if (!content.getText().toString().equals(""))
					result = request.postThreadComment(
							Integer.parseInt(userId),
							Integer.parseInt(threadModel.getThreadID()),
							content.getText().toString());
				content.setText("");
				Log.d("doInBackground", "returned");
			} catch (IOException e) {
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

				CommonModel returnResult = new CommonModel();
				try {
					ToMoreParse getrequest = new ToMoreParse();
					returnResult = getrequest.CommonPares(result);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				finalResult = returnResult.getResult();
				if (finalResult.equals("succ")) {
					Toast.makeText(getApplicationContext(), "发送成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "请重新发送",
							Toast.LENGTH_SHORT).show();
				}

				headerRefresh = true;
				page = 1;
				new GetData(ThreadReplyActivity.this, 1).execute("");
				// get data again
			}
		}

	}
}