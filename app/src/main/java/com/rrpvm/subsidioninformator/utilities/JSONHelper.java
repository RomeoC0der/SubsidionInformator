package com.rrpvm.subsidioninformator.utilities;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper {

    public static <T> boolean exportToJSON(Context context, List<T> data, String fileName) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static <T> ArrayList<T> importFromJSON(Context context, String fileName, Type type) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(fileName);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            ArrayList<T> dataItems = new ArrayList<T>();
            dataItems = gson.fromJson(streamReader, type);
            return dataItems;
            //obj = gson.fromJson(streamReader, obj.getClass());
            // obj=gson.fromJson(streamReader,typeMyType);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static <T> boolean exportToJSON(Context context, String fileName, T data) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static <T> T importFromJSON(Context context, String fileName, TypeToken token, boolean singleObject) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        T result;
        try {
            fileInputStream = context.openFileInput(fileName);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            result = gson.fromJson(streamReader, token.getType());
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
