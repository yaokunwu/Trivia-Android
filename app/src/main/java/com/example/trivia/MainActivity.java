package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //这个好厉害, 必须当result有了以后再传回来
        questions =  new Repository().getQuestions(questionArrayList -> {
            binding.questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
            updateCounter(questionArrayList);
        });


        binding.buttonNext.setOnClickListener(v -> {
            currentQuestionIndex = (currentQuestionIndex + 1) % questions.size();
            updateQuestion();
        });

        binding.buttonFalse.setOnClickListener(v -> {
            checkAnswer(false);
            updateQuestion();
        });

        binding.buttonTrue.setOnClickListener(v -> {
            checkAnswer(true);
            updateQuestion();
        });
    }

    private void updateCounter(List<Question> questionArrayList) {
        binding.textViewOutOf.setText(String.format("Question: %d/%d", currentQuestionIndex, questionArrayList.size()));
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void updateQuestion() {
        String question = questions.get(currentQuestionIndex).getAnswer();
        binding.questionTextview.setText(question);
        updateCounter(questions);
    }

    private void checkAnswer(boolean userChoseCorrect) {
        boolean answer = questions.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId = 0;
        if (userChoseCorrect == answer) {
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
        } else {
            snackMessageId = R.string.incorrect;
            shakeAnimation();
        }
        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_SHORT)
        .show();
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this,
                R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch(v.getId()) {
//            case R.id.button_next:
//
//                break;
//            case R.id.button_true:
//                break;
//            case R.id.button_false:
//                break;
//        }
//    }
}