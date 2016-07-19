package com.example.hptouchsmart.ui_try_tinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.daprlabs.cardstack.SwipeDeck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    SwipeDeck cardSwipe;
    String url = "";
    ImageView iv;
    RequestQueue que;
    RequestQueue que2;
    SwipeCardAdapter adapter;

    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> gifUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        String addUrl = i.getStringExtra(StartScreen.PUBLIC_KEY);

        String[] thisAdd = addUrl.split(" ");

        que = Volley.newRequestQueue(this);
        que2 = Volley.newRequestQueue(this);
        cardSwipe = (SwipeDeck) findViewById(R.id.swipe_deck);

        String apiKey = "&api_key=dc6zaTOxFJmzC";

        url = "http://api.giphy.com/v1/gifs/search?q=";

        for(int counter = 0 ; counter < thisAdd.length ; counter++){
            url += thisAdd[counter]+ "+";
        }

        url += apiKey;

        Log.d("TAG",url);


        adapter = new SwipeCardAdapter(urls,MainActivity.this);
        cardSwipe.setAdapter(adapter);

        que.add(strReq());

        cardSwipe.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.d("TAG","card swiped left with position "+position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.d("TAG","card swiped right with position "+position);
            }

            @Override
            public void cardsDepleted() {
                Log.d("TAG","No more cards");
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });

    }



    public StringRequest strReq(){

        //String url  param;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //String dataString = jsonObject.getString("data");
                    //JSONArray dataArray  = new JSONArray(dataString);
                    JSONArray dataObject = jsonObject.getJSONArray("data");
                    //JSONObject images = dataObject.getJSONObject(16);
                    for(int i = 0 ; i < dataObject.length() ; i++){
                        JSONObject thisObject = dataObject.getJSONObject(i);
                        JSONObject image = thisObject.getJSONObject("images");
                        JSONObject imageType = image.getJSONObject("original");
                        String imageUrl = imageType.getString("url");
                        Log.d("TAG", imageUrl);
                       // gifUrl.add(imageUrl);
                        URL url = new URL(imageUrl);

                        urls.add(imageUrl);
                        adapter.notifyDataSetChanged();

                     //   que2.add(imageReq(imageUrl));



//                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                        urls.add(bmp);


//                        new AsyncTask<URL,Void,Bitmap>(){
//
//                            @Override
//                            protected Bitmap doInBackground(URL... params) {
//                                Bitmap bmp = null;
//                                try {
//                                     bmp = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                return bmp;
//                            }
//
//                            @Override
//                            protected void onPostExecute(Bitmap bitmap) {
//
//                                super.onPostExecute(bitmap);
//                                urls.add(bitmap);
//                                adapter.notifyDataSetChanged();
//
//
//                            }
//                        }.execute(url);



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("TAG","error occured in try and catch");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.getCause() != null){
                    error.getCause().printStackTrace();
                }
                if(error.networkResponse != null){
                    Toast.makeText(MainActivity.this, "Network Response is " + error.networkResponse , Toast.LENGTH_SHORT).show();
                }

            }
        });


                return stringRequest;
    }



    public ImageRequest imageReq(String url){
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                //urls.add(response);

                adapter.notifyDataSetChanged();



            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.getCause() != null){
                    error.getCause().printStackTrace();
                }
                if(error.networkResponse != null){
                    Toast.makeText(MainActivity.this, "Network Response is " + error.networkResponse , Toast.LENGTH_SHORT).show();
                }

            }
        });
        return imageRequest;
    }






    public class SwipeCardAdapter extends BaseAdapter{
        ArrayList<String> data;
        Context context;

        public SwipeCardAdapter(ArrayList<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li = getLayoutInflater();


            if(convertView == null){
                convertView = li.inflate(R.layout.card_layout,parent,false);

            }

//
            GifImageView gifImageView = (GifImageView) convertView.findViewById(R.id.gifIV);
            //ImageView imageView = (ImageView) convertView.findViewById(R.id.IV);

            String thisGif = getItem(position);
            //imageView.setImageBitmap(thisGif);


            //gifImageView.setImageBitmap(thisGif);

            GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

            Glide
                    .with(context)
                    .load(thisGif)
                    .placeholder(R.drawable.loading_spinner)
                    .into(glideDrawableImageViewTarget);



            return convertView;
        }
    }

}
