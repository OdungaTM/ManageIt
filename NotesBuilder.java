package io.futurebound.manageit;

/**
 * Created by HP on 11/16/2017.
 */

public class NotesBuilder {

    private String title,date,
            content;


    public  NotesBuilder(){

    }
    public NotesBuilder(String title, String content,String date){
        this.title=title;
        this.content=content;
        this.date=date;
    }
    public  String getTitle(){
        return title;
    }
    public String getMessage(){
        return content;
    }
    public String get_Date(){return date;}
}
