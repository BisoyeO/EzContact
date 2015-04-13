package com.isoy.ezcontact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class GenerateCodeActivity extends ActionBarActivity {
    EditText txtFname;
    EditText txtLname;
    EditText txtPhone;
    EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);
        txtFname=(EditText)findViewById(R.id.txtFname);
        txtLname=(EditText)findViewById(R.id.txtLname);
        txtPhone=(EditText)findViewById(R.id.txtPhone);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
    }
    public void generateOnlClick(View V){
        String content = txtFname.getText().toString() +";"+ txtLname.getText().toString() +";"+ txtPhone.getText().toString()
                    +";"+ txtEmail.getText().toString();
        //Generate QR code......
        QRCodeWriter writer = new QRCodeWriter();
        ImageView myQRCodeImg = (ImageView)findViewById(R.id.myQRCode);

        try {
            BitMatrix bitMatrix = writer.encode(content,
                    BarcodeFormat.QR_CODE, 128, 128);
            int width = 128;
            int height = 128;
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y))
                        bmp.setPixel(x, y, Color.BLACK);
                    else
                        bmp.setPixel(x, y, Color.WHITE);
                }
            }
            myQRCodeImg.setImageBitmap(bmp);
        } catch (WriterException e) {
            //Log.e("QR ERROR", ""+e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generate_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.switch_layout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
