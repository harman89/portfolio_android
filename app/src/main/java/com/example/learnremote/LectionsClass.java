package com.example.learnremote;

import java.util.ArrayList;
import java.util.Map;

public class LectionsClass {
    private String theme;
    private String sub_theme;
    private String filename;
    private int id;
    public LectionsClass(int id, String theme, String sub_theme, String filename)
    {
        this.id=id;
        this.theme=theme;
        this.sub_theme = sub_theme;
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }
    public String getSub_theme()
    {
        return  sub_theme;
    }

    public String getFilename() {
        return filename;
    }
}
