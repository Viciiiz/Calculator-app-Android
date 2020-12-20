package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class MainActivity extends AppCompatActivity {

    private TextView textPrompt, textResult;
    private Button buttonDel, buttonSqrt, buttonPow, buttonDivide,
                    buttonSeven, buttonEight, buttonNine, buttonMult,
                    buttonFour, buttonFive, buttonSix, buttonNeg,
                    buttonOne, buttonTwo, buttonThree, buttonPlus,
                    buttonPercent, buttonZero, buttonPoint,
                    buttonClear, buttonLeft, buttonRight, buttonEquals;
    private String result = "", current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize buttons and textViews
        initialize();

        // give task to each button
        clickListener(buttonZero, textPrompt);
        clickListener(buttonOne, textPrompt);
        clickListener(buttonTwo, textPrompt);
        clickListener(buttonThree, textPrompt);
        clickListener(buttonFour, textPrompt);
        clickListener(buttonFive, textPrompt);
        clickListener(buttonSix, textPrompt);
        clickListener(buttonSeven, textPrompt);
        clickListener(buttonEight, textPrompt);
        clickListener(buttonNine, textPrompt);
        clickListener(buttonPercent, textPrompt);
        clickListener(buttonLeft, textPrompt);
        clickListener(buttonRight, textPrompt);
        clickListener(buttonPlus, textPrompt);
        clickListener(buttonNeg, textPrompt);
        clickListener(buttonDivide, textPrompt);
        clickListener(buttonMult, textPrompt);

        // delete button
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.length()>0){
                    current = current.substring(0, current.length()-1);
                    textPrompt.setText(current);
                }
            }
        });

        // clear button
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = "";
                textPrompt.setText(current);
            }
        });

        // SQRT button
        buttonSqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.length()==0){
                    current = current.concat("Sqrt(");
                } else {
                    if (Character.isDigit(current.charAt(current.length()-1))){
                        current = current.concat("xSqrt(");
                    } else {
                        current = current.concat("Sqrt(");
                    }
                }
                textPrompt.setText(current);
            }
        });

        // point button
        buttonPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.length()==0){
                    current = "0.";
                } else {
                    if (!Character.isDigit(current.charAt(current.length()-1))){
                        current = current.concat("0.");
                    } else {
                        current = current.concat(".");
                    }
                }
                textPrompt.setText(current);
            }
        });

        // pow button
        buttonPow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.length()>0) {
                    if (Character.isDigit(current.charAt(current.length() - 1)) || current.charAt(current.length() - 1)==')'
                            || current.charAt(current.length() - 1)=='.'|| current.charAt(current.length() - 1)=='%'){
                        current = current.concat("Pow(");
                    }
                }
                textPrompt.setText(current);
            }
        });

        // equals button
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = current;
                result = result.replaceAll("Sqrt", "Math.sqrt");
                result = result.replaceAll("%", "/100.0");
                //result = result.replaceAll("Pow", "Math.pow");
                result = result.replaceAll("x", "*");
                try {
                    if (firstArgPow(result).size() > 0 && secondArgPow(result).size() > 0) {
                        for (int i = 0; i < result.length(); i++) {
                            for (int j = 0; j < firstArgPow(result).size(); j++) {
                                String first = "";
                                String second = "";
                                first = first.concat(firstArgPow(result).get(j));
                                first = first.concat("Pow");
                                first = first.concat(secondArgPow(result).get(j));

                                // check if the first argument of Math.pow() is the beginning of the user input
                                boolean isFirst = false;
                                String curr = firstArgPow(result).get(j);
                                int count = 0;
                                for (int k = 0; k < curr.length(); k++){
                                    if (result.charAt(k) == curr.charAt(k)){
                                        count ++;
                                    }
                                }
                                if (count == curr.length()){
                                    isFirst = true;
                                }

                                String third = "";
                                if (isFirst){
                                    second = second.concat("Math.pow(");
                                } else {
                                    second = second.concat("*Math.pow(");
                                    third = third.concat("*");
                                }
                                second = second.concat(firstArgPow(result).get(j));
                                second = second.concat(",");
                                String temp = "";
                                double power = 0;
                                ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
                                try{
                                    power = ((double)engine.eval(secondArgPow(result).get(j)));
                                    //temp = Double.toString(power);
                                }catch(Exception e){
                                    Log.d("Pow ", "Error in pow");
                                }
                                String res = "";
                                if (power > 0) {
                                    for (int k = 0; k < (int) power - 1; k++) {
                                        res = res.concat("*");
                                        res = res.concat(firstArgPow(result).get(j));
                                    }
                                }
                                //second = second.concat(secondArgPow(result).get(j));
                                //second = second.concat(")");

                                third = third.concat(firstArgPow(result).get(j));
                                third = third.concat(res);
                                result = result.replace(first, third);
                            }
                        }
                    }
                    Log.d("calculation ", result);
                    calculation(result);
                } catch (Exception e){
                    result = "ERROR";
                }


            }
        });

    }

    /**
     * Method to initialize the textViews and buttons
     */
    private void initialize(){
        //buttons
        buttonDel = findViewById(R.id.buttonDel);
        buttonSqrt = findViewById(R.id.buttonSqrt);
        buttonPow = findViewById(R.id.buttonPow);
        buttonDivide = findViewById(R.id.buttonDivide);
        buttonSeven = findViewById(R.id.buttonSeven);
        buttonEight = findViewById(R.id.buttonEight);
        buttonNine = findViewById(R.id.buttonNine);
        buttonMult = findViewById(R.id.buttonMult);
        buttonFour = findViewById(R.id.buttonFour);
        buttonFive = findViewById(R.id.buttonFive);
        buttonSix = findViewById(R.id.buttonSix);
        buttonNeg = findViewById(R.id.buttonNeg);
        buttonOne = findViewById(R.id.buttonOne);
        buttonTwo = findViewById(R.id.buttonTwo);
        buttonThree = findViewById(R.id.buttonThree);
        buttonPlus= findViewById(R.id.buttonPlus);
        buttonPercent = findViewById(R.id.buttonPercent);
        buttonZero = findViewById(R.id.buttonZero);
        buttonPoint = findViewById(R.id.buttonPoint);
        buttonClear = findViewById(R.id.buttonClear);
        buttonLeft = findViewById(R.id.buttonLeftParenthesis);
        buttonRight = findViewById(R.id.buttonRightParenthesis);
        buttonEquals = findViewById(R.id.buttonEquals);
        // textViews
        textPrompt = (TextView) findViewById(R.id.textPrompt);
        textResult = findViewById(R.id.textResult);
    }


    /**
     * Method to set a click listener for a given button. Buttons 0 to 9, ., (), %, /X-+, will append
     * a string that stores the current calculation. For the other buttons, a specific clickListener will
     * be used.
     * @param button
     */
    private void clickListener(final Button button, final TextView text){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = current.concat(button.getText().toString());
                Log.d("prompt ", button.getText().toString() + " current: " + current);
                // set text in textView here
                text.setText(current);
            }
        });
    }


    /**
     * Method that takes a calculation as a string and does the prompted calculation which will be
     * stored in a double.
     * @param input
     */
    private void calculation(String input){
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        try{
            double res;
            res = ((double)engine.eval(input));
            // round result
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            result = Double.toString(Double.parseDouble(df.format(res)));
            textResult.setText(result);
        }catch(Exception e){
            Toast.makeText(this,"Error in the input.",Toast.LENGTH_SHORT).show();
            textResult.setText("Error");
        }
    }


    /**
     * Method to find the first argument of Math.pow() in the user input
     * @param res is the user input
     * @return the first argument of Math.pow()
     */
    private ArrayList<String> firstArgPow(String res){
        ArrayList<String> r = new ArrayList<>();
        for (int i = 0 ; i < res.length(); i++){
                if(res.charAt(i)=='P'){
                    String number = "";
                    int index = 1;
                    int parenthesisCount = 0;
                    while (true){
                        if (index > i){
                            break;
                        }
                        if (index!= 1 && (!Character.isDigit(res.charAt(i-index)) && res.charAt(i-index)!='(' && res.charAt(i-index)!=')' && res.charAt(i-index)!='.' && res.charAt(i-index)!='%')){
                            break;
                        }
                        if (res.charAt(i-index)==')'){
                            number = number.concat(Character.toString(res.charAt(i-index)));
                            parenthesisCount++;
                        }
                        if (Character.isDigit(res.charAt(i-index)) || res.charAt(i-index)=='.' || res.charAt(i-index)=='%'){
                            number = number.concat(Character.toString(res.charAt(i-index)));
                        }
                        if (index!= 1 && res.charAt(i-index)=='('){
                            number = number.concat(Character.toString(res.charAt(i-index)));
                            parenthesisCount--;
                            if (parenthesisCount == 0){
                                break;
                            }
                        }
                        index++;
                    }
                    r.add(number);
                }
        }
        // reverse the each String o the arrayList;
        ArrayList<String> fin = new ArrayList<>();
        for (int i = 0 ; i < r.size(); i++){
            String finalRes = "";
            for (int j = 0; j < r.get(i).length(); j++){
                finalRes = finalRes.concat(Character.toString(r.get(i).charAt(r.get(i).length() - 1 - j)));
            }
            fin.add(finalRes);
        }

        return fin;
    }


    /**
     * Method to get the second argument of Math.Pow() in the user input
     * @param res is the user input
     * @return the second argument of Math.Pow()
     */
    private ArrayList<String> secondArgPow(String res){
        ArrayList<String> r = new ArrayList<>();
        for (int i = 0; i < res.length(); i++){
            String finalRes = "";
            int index = 1;
            if (res.charAt(i)=='w'){
                boolean end = false;
                int parenthesisCount = 0;
                while (i + index < res.length() && !end){
                    if (res.charAt(i + index) == '('){
                        parenthesisCount++;
                    } else if (res.charAt(i + index) == ')'){
                        parenthesisCount--;
                    }
                    if (parenthesisCount == 0){
                        end = true;
                    }
                    finalRes = finalRes.concat(Character.toString(res.charAt(i+index)));
                }
                r.add(finalRes);
            }
        }
        return r;
    }
}