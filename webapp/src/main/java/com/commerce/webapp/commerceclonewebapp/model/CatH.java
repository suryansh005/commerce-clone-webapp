package com.commerce.webapp.commerceclonewebapp.model;

import com.commerce.webapp.commerceclonewebapp.model.entity.Category;

import java.util.HashMap;
import java.util.Map;

public class CatH {
    private String name;
    private Category category;
    private Map<String,CatH> hierarchy;

    public  CatH(String name){
        this.name =  name;
        hierarchy = new HashMap<>();
    }
    public CatH findNode(String [] testArr,int current ,int required){

        CatH obj = hierarchy.get(testArr[current]);

        if(obj ==  null)
            return null;

        else if (obj.getCategory().getCategoryName().equalsIgnoreCase(testArr[required]))
            return obj;

        return obj.findNode(testArr,++current,required);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<String, CatH> getHierarchy() {
        return hierarchy;
    }
}
