package models;

public class Member {
    private String name;
    public Member(String name){
        this.name = name;
    }

    public void test(){
        System.out.println(this.name);
    }
}
