package com.ritik.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        setTitle(ParseUser.getCurrentUser().getUsername().toUpperCase());

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(TwitterUsers.this, android.R.layout.simple_list_item_checked, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(TwitterUsers.this);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser user : objects) {
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);

                        for(String twitterUser : arrayList) {
                            if(ParseUser.getCurrentUser().getList("followerOf") != null) {
                                if (ParseUser.getCurrentUser().getList("followerOf").contains(twitterUser)) {
                                    listView.setItemChecked(arrayList.indexOf(twitterUser), true);
                                }
                            }
                        }
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Intent intent = new Intent(TwitterUsers.this, LoginActivity.class);
                        startActivity(intent);
                        FancyToast.makeText(TwitterUsers.this, "Logged Out", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        finish();
                    }
                    else {
                        FancyToast.makeText(TwitterUsers.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            });
        }
        else if(item.getItemId() == R.id.sendTweet) {
            Intent intent = new Intent(TwitterUsers.this, SendTweet.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()) {
            FancyToast.makeText(TwitterUsers.this, arrayList.get(position) + " is Followed", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            ParseUser.getCurrentUser().add("followerOf", arrayList.get(position));
        }
        else {
            FancyToast.makeText(TwitterUsers.this, arrayList.get(position) + " is Not Followed", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            ParseUser.getCurrentUser().getList("followerOf").remove(arrayList.get(position));

            List currentUserFollowerOfList = ParseUser.getCurrentUser().getList("followerOf");
            ParseUser.getCurrentUser().remove("followerOf");
            ParseUser.getCurrentUser().put("followerOf", currentUserFollowerOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    FancyToast.makeText(TwitterUsers.this, "Data Saved To Server", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                }
            }
        });
    }
}
