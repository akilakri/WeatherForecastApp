package com.forecase.akila.weatherforecastapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                submit_form();
            }
        });
    }
    public void submit_form() {
        Spinner dropdown = (Spinner) findViewById(R.id.state);
        String[] items = new String[]{"Select..", "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", " WV", "WI", "WY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        final String state = dropdown.getSelectedItem().toString();
        final EditText streetEditText = (EditText) findViewById(R.id.street);
        final EditText cityEditText = (EditText) findViewById(R.id.city);
        final TextView ErrorText = (TextView) findViewById(R.id.error);
        final String street = streetEditText.getText().toString().trim();
        final String city = cityEditText.getText().toString().trim();
        final RadioGroup degreegroup = (RadioGroup) findViewById(R.id.degree);
        final int selected_degreeid = degreegroup.getCheckedRadioButtonId();
        final View radiodegree = degreegroup.findViewById(selected_degreeid);
        final int radioId = degreegroup.indexOfChild(radiodegree);
        final RadioButton sel_degree = (RadioButton) degreegroup.getChildAt(radioId);
        String degree = (String) sel_degree.getText();
       AWSCall callaws = new AWSCall();
        String aws_url = "http://cs-server.usc.edu:36513/server.php?street=2363%20Portland%20Street&city=Los%20Angeles&state=CA&degree=fahrenheit";
    //    aws_url+="?street="+encode(street)+"&city="+encode(city)+"&state="+state+"&degree="+degree;
       callaws.execute(new String[] {aws_url});
        if (TextUtils.isEmpty(street)) {
            ErrorText.setText("Please Enter a Street");
            ErrorText.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(city)) {
            ErrorText.setText("Please Enter a City");
            ErrorText.setVisibility(View.VISIBLE);
        }
        streetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {

                if (street.length() > 0) {
                    ErrorText.setText(null);
                    ErrorText.setVisibility(View.INVISIBLE);
                }
            }
        });
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {

                if (city.length() > 0) {
                    ErrorText.setText(null);
                    ErrorText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    String response="";

    private class AWSCall extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... urls) {

            String response;
            for (String url : urls) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(url);
                    //HttpPost httppost = new HttpPost(params[0]);//you can also pass it and get the Url here.

                    HttpResponse responce = httpclient.execute(httppost);

                    HttpEntity httpEntity = responce.getEntity();

                    response = EntityUtils.toString(httpEntity);

                    Log.d("response is", response);

                    return new JSONObject(response);

                } catch (Exception ex) {

                    ex.printStackTrace();

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("JSON Received");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            /*
            super.onPostExecute(result);

            if(result != null)
            {
                try
                {
                    JSONObject jobj = result.getJSONObject("result");

                    String status = jobj.getString("status");

                    if(status.equals("true"))
                    {
                        JSONArray array = jobj.getJSONArray("data");

                        for(int x = 0; x < array.length(); x++)
                        {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("name", array.getJSONObject(x).getString("name"));

                            map.put("date", array.getJSONObject(x).getString("date"));

                            map.put("description", array.getJSONObject(x).getString("description"));

                            list.add(map);
                        }

                        CalendarAdapter adapter = new CalendarAdapter(Calendar.this, list);

                        list_of_calendar.setAdapter(adapter);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(Calendar.this, "Network Problem", Toast.LENGTH_LONG).show();
            }
            */
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
