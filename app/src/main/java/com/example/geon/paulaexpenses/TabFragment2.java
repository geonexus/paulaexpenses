package com.example.geon.paulaexpenses;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TabFragment2 extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public String concept = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);

        final TextView question_1 = (TextView) rootView.findViewById(R.id.question_1);
        final TextView title_category = (TextView) rootView.findViewById(R.id.title_category);

        final EditText quantity = (EditText) rootView.findViewById(R.id.quantity);

        final ImageButton button = (ImageButton) rootView.findViewById(R.id.button_1);
        button.setContentDescription(getString(R.string.expenses_category_1));

        final ImageButton button2 = (ImageButton) rootView.findViewById(R.id.button_2);
        button2.setContentDescription(getString(R.string.expenses_category_2));

        final Button button3 = (Button) rootView.findViewById(R.id.button_3);
        button3.setText(getString(R.string.expenses_category_3));

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                concept = button.getContentDescription().toString();
                title_category.setText(getString(R.string.title_category) + concept);
                title_category.setVisibility(View.VISIBLE);
                question_1.setVisibility(View.VISIBLE);
                quantity.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                concept = button2.getContentDescription().toString();
                title_category.setText(getString(R.string.title_category) + concept);
                title_category.setVisibility(View.VISIBLE);
                question_1.setVisibility(View.VISIBLE);
                quantity.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                concept = button3.getText().toString();
                title_category.setText(getString(R.string.title_category) + concept);
                title_category.setVisibility(View.VISIBLE);
                question_1.setVisibility(View.VISIBLE);
                quantity.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab.setVisibility(View.INVISIBLE);
                SheetsuResponse response = null;
                try {
                    response = sendRequestAPI(concept,
                            concept, quantity.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                title_category.setVisibility(View.INVISIBLE);
                question_1.setVisibility(View.INVISIBLE);
                quantity.setText("");
                quantity.setVisibility(View.INVISIBLE);

                Snackbar.make(view, buildMessage(response), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
    }

    private SheetsuResponse sendRequestAPI(String category, String concept, String quanty) throws JSONException {
        HttpURLConnection urlConnection = null;
        SheetsuResponse response = null;
        DataOutputStream printout;
        JSONObject jObjectData = new JSONObject();
        // Create Json Object using Facebook Data
        jObjectData.put("Category", category);
        jObjectData.put("Concept", concept);
        jObjectData.put("Quanty", quanty);
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = format1.format(rightNow.getTime());

        jObjectData.put("Date", date);
        try {
            URL url = new URL(getString(R.string.api_url));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            printout = new DataOutputStream(urlConnection.getOutputStream());
            String str = jObjectData.toString();
            byte[] data = str.getBytes("UTF-8");
            printout.write(data);
            printout.flush();
            printout.close();
            response = new SheetsuResponse();
            response.setCode(urlConnection.getResponseCode());
            response.setMessage(urlConnection.getResponseMessage());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            return response;

        }
    }

    private String buildMessage(SheetsuResponse response) {
        String message = "";

        if ((response.getCode() == 201) &&
                !(response.getMessage().contains("\"exception\":{\"class\":" +
                        "\"GoogleDrive::ResponseCodeError\""))) {
            message = getString(R.string.success_message);
            System.out.println(response.getMessage());

        }
        else {
            message = getString(R.string.fail_message);
            System.out.println(response.getMessage());
        }

        return message;
    }
}