package com.shephertz.app42.buddy.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.shephertz.app42.buddy.app.AppContext;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.util.Utils;


public class ImageLoader {
	private int requiredSize=0;
    private boolean loadNormal=true;
	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private static HashMap<String, Bitmap> cacheAlbums;
	 private static LruCache<String, Bitmap> cache;
	private File cacheDir;
	public boolean fromGallery=false;
    
	public ImageLoader(Context context,int imageSize) {
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
		requiredSize=imageSize;
		if(imageSize>0){
			loadNormal=false;
		}
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),AppContext.AppName);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		memoryInstance();
		
	}
	
	/*
	 * instance of class
	 */
	private void memoryInstance() {
		if (cache == null) {
			cache =new LruCache<String, Bitmap>(1024*10);
			cacheAlbums=new HashMap<String, Bitmap>();;
		}
	}
	public void checkAndClearCache(){
		if(cacheAlbums.size()>20){
			cacheAlbums.clear();
		}
	}

	final int stub_id = R.drawable.default_pic;

	public void DisplayImage(String url,ImageView imageView) {
		try{
			Bitmap bmp=null;
			if(fromGallery){
				bmp=cacheAlbums.get(url);
			}
			else{
				bmp=cache.get(url);
			}
	
		if (bmp!=null){
			imageView.setImageBitmap(bmp);
		}
		else {
			imageView.setTag(url);
			queuePhoto(url, imageView);
		}
		}
		catch (Throwable ex) {
			 ex.printStackTrace();
	           if(ex instanceof OutOfMemoryError)
	              clearCache();
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	public  Bitmap getBitmap(String urlImage) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
	
		String spaceUrlEncode=Utils.urlSpaceEncode(urlImage);
		String filename = String.valueOf(spaceUrlEncode.hashCode());
		File f = new File(cacheDir, filename);

		// from SD cache
		Bitmap b;
		if(loadNormal){
			b = decodeWithoutSampling(f);
		}
		else{
			b = decodewithSampleing(f);
		}
	
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			InputStream is = new URL(spaceUrlEncode).openStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			if(loadNormal){
				bitmap = decodeWithoutSampling(f);
			}
			else{
				bitmap = decodewithSampleing(f);
			}
			return bitmap;
		} catch (Throwable ex) {
			 ex.printStackTrace();
	           if(ex instanceof OutOfMemoryError)
	              clearCache();
	           return null;
		}
	}

	private Bitmap decodeWithoutSampling(File f){
		try {
			
			return BitmapFactory.decodeStream(new FileInputStream(f));
			
		} catch (Exception e) {
		}
		return null;
	}
	
	// decodes image and scales it to reduce memory consumption
	private Bitmap decodewithSampleing(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < requiredSize
						|| height_tmp / 2 < requiredSize)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPreferredConfig=Bitmap.Config.RGB_565;
			o2.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						Bitmap bmp = getBitmap(photoToLoad.url);
						if(fromGallery){
							cacheAlbums.put(photoToLoad.url, bmp);
						}
						else{
							cache.put(photoToLoad.url, bmp);
						}
					
						Object tag = photoToLoad.imageView.getTag();
						if (tag != null
								&& ((String) tag).equals(photoToLoad.url)) {
							BitmapDisplayer bd = new BitmapDisplayer(bmp,
									photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView
									.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (Exception e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null){
				imageView.setImageBitmap(bitmap);
			}
			else
				imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		// clear memory cache
		cache.evictAll();
		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

}
