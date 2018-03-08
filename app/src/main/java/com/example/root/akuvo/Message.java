package com.example.root.akuvo;

/**
 * Created by root on 24/2/18.
 */

public class Message {
    private String content;
    public  Message(){

    }
    public Message(String content){
        this.content=content;
    }
    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content=content;
    }
}
