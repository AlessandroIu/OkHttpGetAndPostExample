package com.example.alessandro.okhttpgetandpostexample;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;

        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static OkHttpClient client = new OkHttpClient();

    String okHttpReadmeMdUrl = "https://raw.github.com/square/okhttp/master/README.md";
    Button getButton;
    TextView getTextView;
    EditText getEditText;
    Button setOkhttpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getButton = findViewById(R.id.get_button);
        getTextView = findViewById(R.id.get_tv);
        getEditText = findViewById(R.id.get_et);
        setOkhttpButton = findViewById(R.id.set_okhttp_button);

        getButton.setOnClickListener(this);
        setOkhttpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.get_button): {
                String url = getEditText.getText().toString();
                if (url != null) {
                    getAttempt(url);
                }
                break;
            }
            case (R.id.set_okhttp_button): {
                getEditText.setText(okHttpReadmeMdUrl);
                break;
            }
        }
    }

    // This method manages a generic runnable method inside a new Thread
    // This method should belong to a "NetworkUtilities" class
    public static Thread performOnBackgroundThread(final Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        thread.start();
        return thread;
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
                    Response response = client.newCall(request).execute();
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

                    }
                });


            }
        };
        performOnBackgroundThread(runnable);
    }

}