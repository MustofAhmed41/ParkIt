package com.example.isdmap.Models;


public class SlotOwner {
    private String mText1;
    private String mText2;


    public SlotOwner(String text1, String text2) {

        this.mText1 = text1;
        this.mText2 = text2;
    }

    public String getText1() {
        return mText1;
    }
    public String getText2() {
        return mText2;
    }
    public void setText1(String mText1) {
        this.mText1=mText1;
    }
    public void setText2(String mText2) {
        this.mText2=mText2;
    }

    @Override
    public String toString() {
        return "SlotOwner{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                '}';
    }

}
