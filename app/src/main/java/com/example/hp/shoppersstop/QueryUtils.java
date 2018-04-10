package com.example.hp.shoppersstop;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.example.hp.shoppersstop.UserViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.hp.shoppersstop.EnterList.uid;

/**
 * Created by hp on 28-01-2018.
 */

public class QueryUtils extends Application {

    private static final String TAG = "QUERY_UTILS";
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static List<ListItem> demoModel;
    private static SparseArray<ListItem> demoMap;

    private QueryUtils(){}

    public static ArrayList<ListItem> extractList(final String listJSON){

        if(TextUtils.isEmpty(listJSON)){
            Log.e(TAG, "JSON IS EMPTY!");
             return null;
        }
        demoModel = new ArrayList<>();
        demoMap = new SparseArray<>();

        final ArrayList<ListItem> itemArrayList = new ArrayList<>();

         mDatabase.child("shopstore").child("customer").child(EnterList.uid).child("productList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ListItem listItem = dataSnapshot.getValue(ListItem.class);
                itemArrayList.add(new ListItem(listItem.getName(),listItem.getBrand(),
                        listItem.getQuant(), listItem.getPrice() ,listItem.getWeight()));

                demoModel.add(new ListItem(listItem.getName(),listItem.getBrand(),
                        listItem.getQuant(), listItem.getPrice() ,listItem.getWeight()));

                demoMap.put(listItem.id, new ListItem(listItem.getName(),listItem.getBrand(),
                        listItem.getQuant(), listItem.getPrice() ,listItem.getWeight()));

                Log.i(TAG, listItem.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    return itemArrayList;
    }

    public static final void removeItemFromList(int position) {
        try {
            Log.i(TAG, "position:"+position);
            ListItem listItem = demoModel.get(position);
            Log.i(TAG, "Name:" + listItem.getName());
            demoModel.remove(position);
            demoMap.remove(demoModel.get(position).id);
        } catch (IndexOutOfBoundsException e){
            Log.i(TAG, "Index out of bounds:"+e+ "Position :"+position+"total Demo Model size:"+demoModel.size()+" total size DemoMap:"+demoMap.size());
        }
        catch(NullPointerException e){
            Log.i(TAG, "Null Pointer Exception:" + e);

        }
    }

    public static ListItem findById(int id) {
      ListItem earthquake = demoMap.get(id);
        ListItem earthquake1 = demoMap.valueAt(id);
      Log.i(TAG, "earthquake Map:"+earthquake.getName() + " earthquake map" + earthquake1.getBrand());
        return demoMap.get(id);
    }


    public static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
            } catch (MalformedURLException e){
            Log.e("QUERY", "error in url:"+e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";

        if(url == null){
        Log.e("URL ", "NULL");
        return jsonResponse;
       }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e){
            Log.e("QueryUtil", "InputReader error" +e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }

        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
       return output.toString();
    }

}
