package com.example.carveyourchoice.Utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utilities {

    public static Uri getImageUri(Context context, File file) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap image = BitmapFactory.decodeFile(file.getPath());
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        return Uri.fromFile(file);
    }

    public static int randomId(int min, int max){
        return (int)(Math.random()*(max-min+1));
    }

    public static String dateCompare(String date1, String date2){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d1 = dateFormat.parse(date1);
            Date d2 = dateFormat.parse(date2);
            if (d1.compareTo(d2)>0){
                return "after";
            }else if(d1.compareTo(d2) < 0){
                return "before";
            }else if(d1.compareTo(d2) == 0){
                return "same";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String dateAdder(String dateStr, int days){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(dateStr);
            calendar.setTime(date);
            calendar.add(Calendar.DATE,days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(calendar.getTime());
    }

    public static String getTodayDateStr(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateFormat.format(date);
        return today;
    }
    public static int getDateSplit(String date, String pattern, int index){
        return Integer.parseInt(date.split(pattern)[index]);
    }
    public static String getMonth(int month){
        String Month = "";
        switch (month){
            case 1:
                Month = "Jan";
                break;
            case 2:
                Month = "Feb";
                break;
            case 3:
                Month = "Mar";
                break;
            case 4:
                Month = "Apr";
                break;
            case 5:
                Month = "May";
                break;
            case 6:
                Month = "Jun";
                break;
            case 7:
                Month = "Jul";
                break;
            case 8:
                Month = "Aug";
                break;
            case 9:
                Month = "Sep";
                break;
            case 10:
                Month = "Oct";
                break;
            case 11:
                Month = "Nov";
                break;
            case 12:
                Month = "Dec";
                break;

        }
        return Month;
    }

    public static void deleteFile(Context context, String directory, String filename){
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File dir = contextWrapper.getDir(directory, Context.MODE_PRIVATE);
        File file = new File(dir,filename);
        if (file.exists()){
            file.delete();
        }
    }
    public static List<Integer> convertStringToIntList(String data){
        List<Integer> integers = new ArrayList<>();
        data = data.replace("[","").replace("]","");
        String[] splitData = data.split(",");
        for (String split: splitData) {
            split = split.trim();
            if (!split.isEmpty()){
                integers.add(Integer.parseInt(split));
            }
        }
        return integers;
    }

    public static String readFile(Context context,String directory, String filename){
        StringBuilder data = new StringBuilder();
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File dir = contextWrapper.getDir(directory, Context.MODE_PRIVATE);
        File file = new File(dir,filename);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null){
                data.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public static List<JSONObject> readJsonFile(Context context, String directory, String filename){
        String data = readFile(context,directory,filename);
        List<JSONObject> objects = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                objects.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public static List<JSONObject> readJsonFile(Context context,int resource){
        String data = readFile(context,resource);
        List<JSONObject> objects = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                objects.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public static String readFile(Context context, int res){
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            String jsonStr = null;
            inputStream = context.getResources().openRawResource(res);
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while((jsonStr = bufferedReader.readLine()) != null){
                stringBuilder.append(jsonStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(stringBuilder);
    }

    public static void saveFile(Context context,String directory, String filename, String data){
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File dir = contextWrapper.getDir(directory, Context.MODE_PRIVATE);
        File file = new File(dir,filename);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File loadImage(String imagePath, Context context){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imagePath.split("/")[0], Context.MODE_PRIVATE);
        File file = new File(directory, imagePath.split("/")[1]);
        return file;
    }

    public static String saveImage(Context context, Uri uri){
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File dir, file;
        String[] path = uri.getPath().split("/");
        String filePath = path[path.length-1].replaceAll("-","_");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            dir = contextWrapper.getDir("appUserImages",Context.MODE_PRIVATE);
            file = new File(dir,filePath);
            if (!file.exists()){
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "appUserImages/"+filePath;
    }
}
