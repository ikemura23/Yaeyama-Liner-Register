
package com.ikmr.banbara23.yaeyama_liner_register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * メイン画面
 */
public class MainActivity extends AppCompatActivity {

    private YaeyamaLinerRegisterService mYaeyamaLinerRegisterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mYaeyamaLinerRegisterService = new YaeyamaLinerRegisterService();
        mYaeyamaLinerRegisterService.startResident(getApplicationContext());

        ((TextView) findViewById(R.id.intervalText)).setText("間隔：" + getResources().getInteger(R.integer.service_interval) + "分");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mYaeyamaLinerRegisterService.stopResidentIfActive(getApplicationContext());
    }
}
