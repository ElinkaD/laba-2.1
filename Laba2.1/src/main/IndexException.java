package main;

public class IndexException extends RuntimeException {

    public IndexException(String str){
        super(str);
        System.out.println(str);
    }
}
