package webservice;

import com.connection.ziparama.CustomHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

	Context context;
	ImageView imageView;
	Bitmap bitmap;
	ProgressBar progress;
	
	public ImageLoader(Context context, ImageView imageView, ProgressBar progress2) {
		// TODO Auto-generated constructor stub
		this.imageView = imageView;
		this.context = context;
		this.progress = progress2;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			bitmap = CustomHttpClient.executeHttpImageDownload(params[0], context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		imageView.setImageBitmap(result);
		progress.setVisibility(View.GONE);
	}

}
