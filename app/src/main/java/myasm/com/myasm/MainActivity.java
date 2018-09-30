package myasm.com.myasm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button1){
            testAsm();
        }else if(v.getId() == R.id.button2) {
            try {
                HelloWorld.main(new String[]{""});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void testAsm(){
        try {
            AsmTest.test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
