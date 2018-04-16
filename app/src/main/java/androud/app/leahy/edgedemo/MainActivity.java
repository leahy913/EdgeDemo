package androud.app.leahy.edgedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.look.Slook;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Slook slook = new Slook();

        try {
            slook.initialize(this);
        } catch (SsdkUnsupportedException e) {
            return;
        }

        if (slook.isFeatureEnabled(Slook.COCKTAIL_PANEL)) {
            // 该设备支持边缘单模式、边缘单加模式和边缘馈送模式。
            EdgeLog.wtf("11111");
            if (slook.isFeatureEnabled(Slook.COCKTAIL_BAR)) {
                // 该设备支持边缘沉浸式模式特征。
                EdgeLog.wtf("2222");
            }


        }

    }
}
