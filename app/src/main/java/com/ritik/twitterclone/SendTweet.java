package com.ritik.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweet extends AppCompatActivity {

    private EditText edtTweet;
    private Button btnSendTweet, btnViewTweets;
    private ListView tweetListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtTweet = findViewById(R.id.edtTweet);
        btnSendTweet = findViewById(R.id.btnSendTweet);
        btnViewTweets = findViewById(R.id.btnViewTweets);
        tweetListView = findViewById(R.id.tweetListView);

        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject parseObject = new ParseObject("MyTweet");
                parseObject.put("tweet", edtTweet.getText().toString());
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                final ProgressDialog progressDialog = new ProgressDialog(SendTweet.this);
                progressDialog.setMessage("Sending Tweet...");
                progressDialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            FancyToast.makeText(SendTweet.this, ParseUser.getCurrentUser().getUsername() + ": " + edtTweet.getText().toString(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        }
                        else {
                            FancyToast.makeText(SendTweet.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

        btnViewTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
                final SimpleAdapter adapter = new SimpleAdapter(SendTweet.this, tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUsername", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});
                try {
                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                    parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("followerOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size() > 0 && e == null) {
                                for(ParseObject tweetObject : objects) {
                                    HashMap<String, String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUsername", tweetObject.getString("username"));
                                    userTweet.put("tweetValue", tweetObject.getString("tweet"));
                                    tweetList.add(userTweet);
                                }
                            }
                            tweetListView.setAdapter(adapter);
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
