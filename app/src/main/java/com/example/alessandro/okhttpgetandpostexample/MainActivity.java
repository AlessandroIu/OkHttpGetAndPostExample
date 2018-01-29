/*
The example is taken from http://square.github.io/okhttp/
 */

package com.example.alessandro.okhttpgetandpostexample;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;

        import okhttp3.MediaType;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static OkHttpClient clientGet = new OkHttpClient();
    static OkHttpClient clientPost = new OkHttpClient();
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String Tag_THIS = String.valueOf(this.getClass().getSimpleName());

    String okHttpReadmeMdUrl = "https://raw.github.com/square/okhttp/master/README.md";
    Button getButton;
    TextView getTextView;
    EditText getEditText;
    Button setOkhttpButton;
    Button postOkhttpButton;
    TextView postTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getButton = findViewById(R.id.get_button);
        getTextView = findViewById(R.id.get_tv);
        getEditText = findViewById(R.id.get_et);
        setOkhttpButton = findViewById(R.id.set_okhttp_button);
        postOkhttpButton = findViewById(R.id.post_okhttp_button);
        postTextView = findViewById(R.id.post_tv);

        getButton.setOnClickListener(this);
        setOkhttpButton.setOnClickListener(this);
        postOkhttpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.get_button): {
                String url = getEditText.getText().toString();
                // Avoiding crashes due to null strings
                if (url != null) {
                    getAttempt(url);
                }
                break;
            }
            case (R.id.set_okhttp_button): {
                getEditText.setText(okHttpReadmeMdUrl);
                break;
            }
            case (R.id.post_okhttp_button): postAttempt();
        }
    }

    // This method manages the URL get request, creates a Runnable object which encapsulate
    // inside a thread (it cannot be run alone) and passes it to another method .
    void getAttempt(final String url) {

        final Runnable runnable = new Runnable() {
            public void run() {
                String getResult = null;
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = clientGet.newCall(request).execute();
                    getResult = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    // From the moment that UI actions must be called from the UI (main) thread, and
                    // not from a generic worker thread, we create a Runnable.
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "Insert a valid URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // As above, tasks that update the UI has to be run on the main thread.
                final String finalGetResult = getResult;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getTextView.setText(finalGetResult);
                        Log.i(Tag_THIS, "Result of the get:" + finalGetResult);
                    }
                });
            }
        };
        NetworkUtils.performOnBackgroundThread(runnable);
    }

    String bowlingJson(String player1, String player2){
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10], 'color':-48060,'total':41}"
                +"]}";
    }

    String post (String url, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = clientPost.newCall(request).execute()){
            return response.body().string();
        }
    }

    void postAttempt() {
        final Runnable runnable = new Runnable() {
            public void run() {
                String json = bowlingJson("Jesse", "Jake");
                String response = null;
                try {
                    response = post("http://www.roundsapp.com/post", json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String finalResponse = response;
                // As above, tasks that update the UI has to be run on the main thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postTextView.setText(finalResponse);
                    }
                });
                Log.i(Tag_THIS, "Result of the post:" + response);
            }
        };
        NetworkUtils.performOnBackgroundThread(runnable);
    }

}