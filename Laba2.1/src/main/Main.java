package main;

public class Main {

    public static void main(String[] args) {
        /*
        ListString stroca = new ListString("Логика может привести Вас от пункта А");
        ListString stroca1 = new ListString(" к пункту Б, ");
        stroca.append(stroca1);
        stroca.append("а воображение — куда угодно");
        stroca.append('!');
        System.out.println(stroca);
        stroca.insert(24,"1234567891234567");
        System.out.println(stroca);
        ListString stroca2 = new ListString("Логика может привести Вас от пункта А к пункту Б, а воображение — куда угодно!");
        System.out.println(stroca2.charAt(36));
        System.out.println(stroca2.substring(1,36));
         */
        ListString stroca = new ListString("Логика может привести Вас от пункта А к пункту Б, а воображение — куда угодно!");
        stroca.insert(15,"123");
        System.out.println(stroca);
        ListString stroca1 = stroca.substring(16,60);
        System.out.println(stroca1);
        /*
        ListString stroca1 = new ListString("Логика может привести Вас от пункта А к пункту Б, а воображение — куда угодно!");
        stroca1.insert(15,"123");
        System.out.println(stroca1);
        ListString stroca2 =  stroca1.substring(15,38);
        System.out.println(stroca2);

         */
    }
}
