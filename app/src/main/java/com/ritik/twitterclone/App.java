package com.ritik.twitterclone;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NLyIUK56JmhFeRPbaULJq9JdrbWfmaayT2OgCy9k")
                // if defined
                .clientKey("gvkV2EUfXpInvxw58sPomFYrrZRv44c3i3y74Njl")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
