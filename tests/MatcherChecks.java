package com.hlayisa.pantrywise;
import java.util.*;
public final class MatcherChecks {
    private static int checks;
    private static Stock s(String n,double q,String u){return new Stock(-1,n,q,u,"");}
    private static void check(boolean condition,String label){checks++;if(!condition)throw new AssertionError(label);}
    public static void main(String[] args){
        Meal egg=new Meal(1,"Eggs","");egg.requirements.add(s("egg",2,"item"));
        check(!Matcher.qualifies(egg,Collections.emptyList()),"missing");
        check(!Matcher.qualifies(egg,Arrays.asList(s("eggs",1,"item"))),"shortfall");
        check(Matcher.qualifies(egg,Arrays.asList(s("eggs",2,"items"))),"plural");
        check(Matcher.qualifies(egg,Arrays.asList(s("egg",1,"item"),s("eggs",1,"item"))),"duplicate stock");
        egg.requirements.add(s("egg",1,"item"));
        check(!Matcher.qualifies(egg,Arrays.asList(s("eggs",2,"item"))),"repeated requirements");
        Meal flour=new Meal(2,"Flour","");flour.requirements.add(s("flour",500,"g"));
        check(Matcher.qualifies(flour,Arrays.asList(s("flour",0.5,"kg"))),"mass conversion");
        check(!Matcher.qualifies(flour,Arrays.asList(s("flour",500,"ml"))),"mass is not volume");
        Meal oil=new Meal(3,"Oil","");oil.requirements.add(s("oil",1,"tbsp"));
        check(Matcher.qualifies(oil,Arrays.asList(s("oil",3,"tsp"))),"spoon conversion");
        check(Matcher.qualifies(oil,Arrays.asList(s("oil",0.015,"l"))),"litres conversion");
        check(!Matcher.qualifies(new Meal(4,"Empty",""),Collections.emptyList()),"empty recipe excluded");
        check(!Matcher.name("cooked rice").equals(Matcher.name("rice")),"raw and cooked differ");
        check(Matcher.name("  TOMATOES ").equals("tomato"),"case whitespace alias");
        boolean invalid=false;try{Matcher.qualifies(egg,Arrays.asList(s("egg",Double.NaN,"item")));}catch(IllegalArgumentException ex){invalid=true;}
        check(invalid,"NaN rejected");
        System.out.println("PASS: "+checks+" matching checks");
    }
}
