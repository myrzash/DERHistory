package kz.nis.history;

import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;

public class Generator extends AsyncTask<int[], Void, int[]> {

//	private ProgressDialog progressDialog;
//	private Context context;
	private int size;

	public Generator(Context context, int sizeArray) {
//		this.context = context;
		this.size = sizeArray;
	}

	@Override
	protected int[] doInBackground(int[]... params) {
//		int[] all = shuffle(params[0]);
//		int[] need = new int[size];
//		for (int i = 0; i < size; i++)
//			need[i] = all[i];
		// try {
		// TimeUnit.SECONDS.sleep(3);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return shuffle(params[0],size);
	}
//	private int[] shuffle(int[] ar) {
//		Random rnd = new Random();
//		for (int i = ar.length - 1; i > 0; i--) {
//			int index = rnd.nextInt(i + 1);
//			int a = ar[index];
//			ar[index] = ar[i];
//			ar[i] = a;
//		}
//		return ar;
//	}
	public static int[] shuffle(int[] array, int N) {
	    int[] result = new int[N];
	    int length = array.length;

	    Random gen = new Random();

	    for (int i = 0; i < N; i++) {
	        int index = gen.nextInt(length);
	        result[i] = array[index];
	        array[index] = array[length-1];
	        length--;
	    }

	    return result;
	}
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// progressDialog = new ProgressDialog(context);
	// progressDialog.setMessage(context.getString(R.string.wait));
	// progressDialog.setIndeterminate(true);
	// progressDialog.show();
	// }
	// @Override
	// protected void onPostExecute(int[] result) {
	// if (progressDialog != null && progressDialog.isShowing())
	// progressDialog.dismiss();
	// super.onPostExecute(result);
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	//
	// // try {
	// rand = generateVariants(dbAdapter
	// .getActivRecords(categoryNumber));
	// // TimeUnit.MILLISECONDS.sleep(1000);
	// // } catch (InterruptedException e) {
	// // e.printStackTrace();
	// // }
	// return null;
	// }
	//
	//
	// private int[] generateVariants(Cursor cursor) {
	// int[] activIds = getActivIds(cursor);
	// int[] exerciseIds = Helper.randomGenerate(activIds,
	// ActivityPlay.QUANTITY_ANSWERS);
	// for (int i = 0; i < exerciseIds.length; i++)
	// Log.d("EXERCISE IDS:", "-" + exerciseIds[i]);
	// return exerciseIds;
	// }
	//
	// private int[] getActivIds(Cursor cursor) {
	// int length = cursor.getCount();
	// int[] array = new int[length];
	// Log.d("Loader", "length-" + length);
	// int i = 0;
	// do {
	// array[i] = cursor.getInt(0);
	// i++;
	// } while (cursor.moveToNext());
	// return array;
	// }
}
