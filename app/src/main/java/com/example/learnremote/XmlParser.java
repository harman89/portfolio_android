package com.example.learnremote;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class XmlParser {
    private ArrayList<Test> tests;
    public XmlParser ()
    {
        tests=new ArrayList<>();
    }

    public ArrayList<Test> getTests() {
        return tests;
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }
    public boolean parse(FileInputStream fis){
        Log.e("Parsing", "FUNC");
        boolean status = true;
        Test currentTest = null;
        Answer currentAnswer = null;
        Question currentQuestion = null;
        Part currentPart = null;
        ArrayList<Answer> answers = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<Part> parts = new ArrayList<>();
        boolean inEntry = false;
        String textValue = "";
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(fis));
            int eventType = xpp.getEventType();
            Log.e("Parsing", "Started");
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("test".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentTest = new Test();
                            currentTest.setTheme(xpp.getAttributeValue(0));
                            Log.e("Parsing", currentTest.getTheme());

                        }
                        else if("part".equalsIgnoreCase(tagName))
                        {
                            currentPart = new Part();
                            currentPart.setNumber(Integer.parseInt(xpp.getAttributeValue(0)));
                            currentPart.setPart_id(Integer.parseInt(xpp.getAttributeValue(1)));
                        }
                        else if("question".equalsIgnoreCase(tagName))
                        {
                            currentQuestion = new Question();
                            currentQuestion.setNumber(Integer.parseInt(xpp.getAttributeValue(0)));
                        }
                        else if("answer".equalsIgnoreCase(tagName))
                        {
                            currentAnswer = new Answer();
                            currentAnswer.setIsTrue(Integer.parseInt(xpp.getAttributeValue(0)));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("test".equalsIgnoreCase(tagName)){
                                currentTest.setParts(parts);
                                tests.add(currentTest);
                                inEntry = false;
                            } else if("text".equalsIgnoreCase(tagName)&&xpp.getDepth()==3){
                                textValue = textValue.replace(":","");
                                currentPart.setText(textValue);
                            } else if("text".equalsIgnoreCase(tagName)&&xpp.getDepth()==4){
                                currentQuestion.setText(textValue);
                            }
                            else if("text".equalsIgnoreCase(tagName)&&xpp.getDepth()==6){
                                currentAnswer.setText(textValue);
                            }
                            else if("answer".equalsIgnoreCase(tagName)){
                                answers.add(currentAnswer);
                            }
                            else if("question".equalsIgnoreCase(tagName)){
                                currentQuestion.setAnswers(answers);
                                answers=new ArrayList<>();
                                questions.add(currentQuestion);
                            }
                            else if("part".equalsIgnoreCase(tagName)){
                                currentPart.setQuestions(questions);
                                questions=new ArrayList<>();
                                parts.add(currentPart);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e){
            status = false;
            Log.e("Parsing", e.toString());
            e.printStackTrace();
        }
        return  status;
    }
}
