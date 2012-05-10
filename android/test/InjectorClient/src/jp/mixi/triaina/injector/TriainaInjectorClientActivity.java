package jp.mixi.triaina.injector;

import jp.mixi.triaina.injector.R;
import android.app.Activity;
import android.os.Bundle;

public class TriainaInjectorClientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}