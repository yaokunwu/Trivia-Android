package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";


    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
//        List<Question> res = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONArray curObj = response.getJSONArray(i);
                    Question curr = new Question(curObj.getString(0), curObj.getBoolean(1));
                    questionArrayList.add(curr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (callBack != null) {
                callBack.processFinished(questionArrayList);
            }
//            Log.d("Main", "onCreate: " + response.toString());
        }, error -> {
            Log.d("Main", "onCreate: fails");
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return questionArrayList;
    }

}
