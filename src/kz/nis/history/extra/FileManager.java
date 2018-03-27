package kz.nis.history.extra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;

public class FileManager {
//	IN MAIN
//	
//	 String str = "";
//	
//	 for(int i=1;i<6;i++){
//	
//	 str = str+"\n"+i+"-РАЗДЕЛ:\n";
//	 Cursor cursor = dbAdapter.getAllRecords(i);
//	 do{
//	 str = str+cursor.getString(1).trim()+"/";
//	 str = str+cursor.getString(2).trim()+"/";
//	 str = str+cursor.getString(3).trim()+"/";
//	 str = str+cursor.getString(4).trim()+"/";
//	 str = str+cursor.getString(5).trim()+"\n";
//	
//	 }while(cursor.moveToNext());
//	 }
//	
//	 String filename = "history_datas.txt";
////	 writeToExternalstorage(filename, str);
//	FileManager.writeToInternalStorage(this,filename,str);
	// writeToExternalstorage(filename, str);
	// writeToInternalStorage(filename,str);

	public static void writeToInternalStorage(Context context, String fileName,
			String data) {
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static  void writeToExternalstorage(String filename, String data) {
		// get the path to sdcard
		File sdcard = Environment.getExternalStorageDirectory();
		// to this path add a new directory path
		File dir = new File(sdcard.getAbsolutePath());
		// create this directory if not already created
		dir.mkdir();
		// create the file in which we will write the contents
		File file = new File(dir, filename);
		FileOutputStream os;
		try {
			os = new FileOutputStream(file);
			os.write(data.getBytes());
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
