package com.app.tomore;  
import java.io.FileNotFoundException;  

import com.app.tomore.R;

import android.app.Activity;  
import android.content.ContentResolver;  
import android.content.Intent;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.net.Uri;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
import android.widget.Button;  
import android.widget.ImageButton;
import android.widget.ImageView;  
public class ViewAlbums extends Activity {  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.mycamera_layout);  
          
        ImageButton button = (ImageButton)findViewById(R.id.albums);  
        button.setOnClickListener(new ImageButton.OnClickListener(){
            @Override  
            public void onClick(View v) {  
                Intent intent = new Intent();  
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }  
              
        });  
    }  
      
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {  
            Uri uri = data.getData();  
            Log.e("uri", uri.toString());  
            ContentResolver cr = this.getContentResolver();  
            try {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                ImageView imageView = (ImageView) findViewById(R.id.iv01);  
                /* ��Bitmap�趨��ImageView */  
                imageView.setImageBitmap(bitmap);  
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  
}  