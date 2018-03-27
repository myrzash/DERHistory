package kz.nis.history.adapter;
//package erc.historykz.ru.adapter;
//
//import android.util.Log;
//
//public class Helper {
//	
//	public static int[] random(int arrayLength, int minValue, int maxValue) {
//		int delta = maxValue - minValue + 1;
//		if (arrayLength > delta)
//			return null;
//		int[] array = new int[arrayLength];
//		int i = 0;
//		while (i < arrayLength) {
//			array[i] = (int) (Math.random() * delta) + minValue;
//			if (i != 0) {
//				int j = 0;
//				while (j < i) {
//					if (array[j] == array[i]) {
//						i--;
//						break;
//					}
//					j++;
//				}
//			}
//			i++;
//		}
////		showArray(array);
//		return array;
//	}
//
//	
//
//	public static int[] randomGenerate(int[] array, int quantity) {
//		if(array.length<quantity) return null;
//	int[] arrayFinal = new int[quantity];
//	int index=0, buf;
//	for(int i=0;i<quantity;i++){
//		buf = array[i];
//		index = rand(i, array.length-1);
//		array[i] = array[index];
//		array[index] = buf;
//		arrayFinal[i] = array[i];
//	}
//	for(int i=0;i<arrayFinal.length;i++){
//		Log.d("Helper",":"+arrayFinal[i]);			
//	}
//	return arrayFinal;
//}
//
//public static int rand(int minValue, int maxValue) {
//	if(minValue>maxValue) return 0;
//	maxValue = maxValue -minValue+1;
//	int rand = (int) (Math.random()*maxValue+minValue);		
//	return rand;
//}
//
//
//	
////	private static void showArray(int[] array) {
////		Log.d("LOGS", "ARRAY:");
////		if (array != null) {
////			int i = 0;
////			while (i < array.length) {
////				Log.d("LOGS", ":" + array[i]);
////				i++;
////			}
////		}
////	}
//}
