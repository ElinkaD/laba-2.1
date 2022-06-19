package main;
//изменила где length() copy конструтор, и исключение из публичных классов
public class ListString {

    private StringItem head;

    public ListString(String str) { //Преобразующий конструктор разбивающий строку по 16 на StringItem
        String maschar;
        if (str.length() > 16) {                 // если строка больше 16 символов, то мы прописываем алгоритм расcчитанный на все символы
            head = new StringItem(str.substring(0, 16).toCharArray(), (byte) 16, null);  //создаем неизменяемую голову
           StringItem newItem = head;
            for (int i = 16; i < str.length(); i = i + 16) {
                if (str.length() < i + 16) maschar = str.substring(i);                   //вырезаем оставшийся блок если он меньше 16
                else maschar = str.substring(i, i + 16);
                char[] b = new char[16];                                                  // блок по 16 символов
                copyarray(maschar.toCharArray(), b, 0, 0, maschar.length());
                newItem.next = new StringItem(b, (byte) maschar.length(), null);     //создаем новый блок и привязываем его к предыдущему
                newItem = newItem.next;                                                  // переходим на новый блок
            }
        } else {                                // если символов хватает только на один блок
            char [] a = new char[16];
            copyarray(str.toCharArray(),a,0,0,str.length());
            head = new StringItem(a, (byte) str.length(), null);
        }
    }

    public ListString(ListString copynewItem){ // копирующий конструктор, применяемый для создания нового объекта как копии уже существующего
        this.head = new StringItem(copynewItem.head);
        StringItem h = head;
        StringItem h1 = copynewItem.head.next;
        while (h1 != null){
            h.next = new StringItem(h1);
            h = h.next;
            h1 = h1.next;
        }
    }

    public ListString() {
        head = null;
    }

    public int length() {  // возвращает длину строки и объединяют блоки с маленьким количеством символов
        StringItem h = head;
        int size = 0;
        while (h != null) {
            if (h.next != null && h.size + h.next.size <= 16){ // объединение массивов в один если есть место в первом
                join(h,h.next);
            }
            size += h.size;
            h = h.next;
        }
        return size;
    }
//
    private void join(StringItem first, StringItem second) { //объединение блоков в один
        copyarray(second.symbols,first.symbols,0, first.size, second.size);
        first.size = (byte) (second.size+first.size);         //изменение размера блока
        second.size = 0;
        first.next = second.next;   //изменение ссылки на ссылку через блок second
    }

    public void append(char ch) {
        StringItem h = last(head);
        if (h.size < 16) h.symbols[h.size++] = ch;   // если в последнем StringItem было место
        else {                                      // если в последнем StringItem не было места
            char[] c = new char[16];
            c[0] = ch;
            StringItem newItem = new StringItem(c, (byte) 1, null); // создаем новый блок и привязываем его к концу списка
            h.next = newItem;          // Привязываем новый узел к концу
        }
    }

    private StringItem last(StringItem a){// нахождение последнего блока
        StringItem h = a;
        StringItem n = null;
        while (h != null){
            n = h;
            h = h.next;
        }
        return n;
    }

    public void append(ListString string){
        StringItem h = last(head);
        ListString copystring = new ListString(string); //  создание копии уже имеющегося в памяти ListString
        h.next = copystring.head;    // соединение последнего next у исходного ListString с новым
    }

    public void append(String string){
        ListString listString = new ListString(string); //преобразование строки в связный список
        StringItem h = last(head);
        h.next = listString.head;       // соединение последнего next у исходного ListString с новым
    }

    private static void copyarray(char[] a, char[] b, int iotkuda, int ikuda, int howmuch) { //копирывание элементов из a в b
        for (int i = iotkuda; i < iotkuda + howmuch; i++) {
            b[ikuda] = a[i];
            ikuda++;
        }
    }

    private Position searh(int index){ //метод поиска позиции элемента по index
        StringItem h = head;
        int Sizeall = 0;
        while (h != null && Sizeall <= index - h.size) {   //Поиск необходимого блока StringItem
            Sizeall += h.size;
            h = h.next;
        }
        return new Position(h, index - Sizeall); //объединяет в адрес ячейки блок и позицию в строчке
    }

    private static class Position{
        private StringItem block;
        private int place;
        private Position(StringItem block, int place) {
            this.block = block;
            this.place = place;
        }
    }

    public char charAt(int index) throws IndexException{ //возвращает символ из строки по конкретному index
        Position p = searh(index);
        if(p.block == null & index < 0){
            throw new IndexException("Введенный индекс за границами Liststring");
        }
        else if (p.block == null){
            return head.symbols[index];
        }
        else {
            return p.block.symbols[p.place];
        }
    }

    public void setCharAt(int index, char ch) throws IndexException{ //заменяет символ в позиции index на ch
        Position p = searh(index);
        if(p.block == null & index < 0){
            throw new IndexException("Введенный индекс за границами Liststring");
        }
        else if(p.block == null){
            head.symbols[index] = ch;
        }
        else {
            p.block.symbols[p.place] = ch; // замена найденной позиции по индексу на исходный символ
        }
    }

    public ListString substring(int start, int end) throws IndexException{ //2 блока с началом и концом, а между ними можно копир объект
        Position s = searh(start);
        if(s.block == null & start < 0){
            throw new IndexException("Введенный индекс за границами Liststring");
        }
        else {
            Position e = searh(end);

            ListString sublistString = new ListString();
            char[] masstart = new char[16];  // первый блок вкл элементы от start до конца блока

            if(s.block == e.block){    //если start и end находятся в одном блоке
                copyarray(s.block.symbols,masstart,s.place,0,e.place-s.place);
                sublistString.head = new StringItem(masstart, (byte) (e.place-s.place),null);
            }
            else{                 //если start и end находятся в разных блоках
                copyarray(s.block.symbols,masstart,s.place,0,s.block.size-s.place);   // копирую со start до конца блока в первый блок
                sublistString.head = new StringItem(masstart, (byte) (s.block.size-s.place),null);

                char[] masend = new char[16];                     //второй блок вкл элементы последнего нужного блока до end
                copyarray(e.block.symbols, masend,0,0,e.place-1);
                StringItem endblock = new StringItem(masend,(byte) (e.place-1), null);

                if(end-start > s.block.size-s.place+e.place){             //если между start и end есть другие блоки
                    StringItem newI = sublistString.head;
                    while(s.block.next != e.block){
                        newI.next =  new StringItem(s.block.next);
                        newI = newI.next;
                        s.block = s.block.next;
                    }
                    newI.next = endblock;
                }
                else{                              //если блоков не существует, соединяю первый блок и второй блок
                    sublistString.head.next = endblock;
                }
            }
            return sublistString;
        }
    }

    // 2 случая, если вставка происходит в первый элемент блока,
    // то мы просто перекидываем ссылки с предыдущего на вставляемый и с вставляемого на следующий блок.
    // Иначе разрываем блок (который вставляем) на 2 части,
    // соединяем блок с элементами до index с головой полученного liststring,
    // а конец данного liststring с новым блоком,в который перекопируем оставшиеся элементы после разрыва
    private void insertdop(int index, ListString string){
        StringItem startblock = string.head;
        StringItem lastblock = last(startblock);  //последний блок поступившей строки
        Position start = searh(index);  // находим место вставки в исходной строке

        if(start.place == 0){                  // вставляем полученный liststring без разделения строки
            lastblock.next = start.block;         // соединяем конец поступающей строки с исходной
            if(index == 0) head = startblock;          // вставка в начало строки
            else {
                Position prev = searh(index-16);  //предыдущий блок
                prev.block.next = startblock;
            }
        }

        else{                                  // если вставка производиться с разделением блока
            char[] a = new char[16];               // для блока с элементами до вставленной позиции
            copyarray(start.block.symbols, a,0,0,start.place);
            StringItem osta = new StringItem(a, (byte) start.place,startblock);

            char[] b = new char[16];                   // для блока с элементами после вставленной позиции
            copyarray(start.block.symbols, b,start.place,0,16-start.place);
            StringItem ostb = new StringItem(b, (byte) (16-start.place),start.block.next);
            if(index < 16) head = osta;          // если вставка в первый блок
            else{
                Position prev = searh(index-16); //предыдущий блок
                prev.block.next = osta;
            }
            lastblock.next = ostb;
        }
    }

    public void insert(int index, ListString string){
        ListString copylist = new ListString(string); // копия вход строки
        if(index < 0){
            throw new IndexException("Введенный индекс за границами Liststring");
        }
        else{
            insertdop(index,copylist);
        }
    }

    public void insert(int index, String string){
        ListString newlist = new ListString(string);
        if(index < 0){
            throw new IndexException("Введенный индекс за границами Liststring");
        }
        else{
            insertdop(index,newlist);
        }
    }

    private static class StringItem {
        private final static int SIZE = 16;
        private char[] symbols = new char[SIZE];
        private byte size;
        private StringItem next;

        private StringItem(char[] symbols, byte size, StringItem next) { // конструктор для массива const 16
            this.symbols = symbols;
            this.size = size;
            this.next = next;
        }

        private StringItem(StringItem newItem) {   // Копирующий конструктор
            copyarray(newItem.symbols, symbols,0,0, newItem.size);
            size = newItem.size;
            next = null;
        }
    }

    public String toString() { //преобразует ListString в тип String
        StringItem h = head;
        char[] stroca = new char[length()]; //создаем новый массив размером строки
        int i = 0;
        while (h != null) {    //переберыет все блоки
            copyarray(h.symbols, stroca, 0,i,h.size); // копируем массив блока в итоговый символьный массив, избегая пустых мест и повторов
            i += h.size;
            h = h.next;
        }
        return new String(stroca); // преобразует символьный массив в строку и возвращает
    }
}

