package com.isoy.ezcontact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class GenerateCodeActivity extends ActionBarActivity {
    EditText txtFname;
    EditText txtLname;
    EditText txtPhone;
    EditText txtEmail;
    String fname,lname,email,phone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);
        txtFname = (EditText) findViewById(R.id.txtFname);
        txtLname = (EditText) findViewById(R.id.txtLname);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        String ret = "";

        //attempts to read in the file config.txt if it exists
        try {
            InputStream inputStream = openFileInput("EzContactConfig.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
            parseContact(ret);
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }
    //parse the information scanned by the QR code
    public void parseContact(String contact){
        String [] info = contact.split(";");
        fname=info[0];
        lname=info[1];
        phone=info[2];
        email=info[3];
        txtFname.setText(fname);
        txtLname.setText(lname);
        txtPhone.setText(phone);
        txtEmail.setText(email);
    }

    public void generateOnlClick(View V) {
        String content = txtFname.getText().toString() + ";" + txtLname.getText().toString() + ";"
                + txtPhone.getText().toString() + ";" + txtEmail.getText().toString();
        //attempts to store the contact information into a text file
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("EzContactConfig.txt"
                    ,Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        //Generate QR code......
        QRCodeWriter writer = new QRCodeWriter();
        ImageView myQRCodeImg = (ImageView) findViewById(R.id.myQRCode);

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
