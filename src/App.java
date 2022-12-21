import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.print("Введите операцию: ");
        String input = System.console().readLine();

        calc(input);
        }

    public static String calc(String input) {

        char[] symbols = { '+', '-', '*', '/' };

        int firstNumber = 0;
        int secondNumber = 0;

        boolean OnAction = false;
        char symbol = ' ';
        MyNumber myNumber = new MyNumber();

        for (char ch : input.toCharArray()) {

            for (char c : symbols) {
                if (ch == c) {
                    symbol = ch;
                    OnAction = true;

                    var index = input.indexOf(ch);

                    var line1 = input.substring(0, index).trim().toUpperCase();
                    firstNumber = myNumber.getNumberFromString(line1);
                    if(firstNumber<0){
                        return "Завершение работы.";
                    }

                    var line2 = input.substring(index + 1, input.length()).trim().toUpperCase();
                    secondNumber = myNumber.getNumberFromString(line2);
                    if(secondNumber<0){
                        return "Завершение работы.";
                    }
                    if(!myNumber.IsCorrectly(new int[]{firstNumber,secondNumber})){
                        return "Завершение работы.";
                    }

                    myNumber.MakeAction(Action.getAction(symbol));
                    break;
                }
            }
        }        
        if (!OnAction) {
            return "Выражение не найдено.";
        }
        return "Завершение работы.";
    }

    static enum Action {
        Plus('+'),
        Minus('-'),
        Multiply('*'),
        Divide('/');

        char symb;
        static Action[] allActions = { Action.Plus, Action.Minus, Action.Multiply, Action.Divide };
        Action(char ch) {
            symb = ch;
        }

        public char getSymb() {
            return symb;
        }

        static Action getAction(char ch) {
            for (Action action : allActions) {
                if (ch == action.getSymb()) {
                    return action;
                }
            }
            return null;
        }    

        static int makeAction(int number1, int number2, Action action) {
            switch (action) {
                case Plus:
                    return number1 + number2;
                case Minus:
                    return number1 - number2;
                case Multiply:
                    return number1 * number2;
                case Divide:
                    return number1 / number2;
    
                default:
                    return 0;
            }
        }    
    }

    static class MyNumber{
        boolean IsRomeNumbers=false;

        enum NumberType {
            Rome, Arabic
        }
        List<NumberType> typesNumbers = new ArrayList<>();

        private Map<String,Integer> romeNumbers = new HashMap<String,Integer>(){
            {
                put("C",100);
                put("XC",90);
                put("L",50);
                put("XL",40);
                put("X",10);
                put("IX",9);
                put("V",5);
                put("IV",4);
                put("I",1);
            }
        };

        public int getNumberFromString(String line){
            int number = 0;
            IsRomeNumbers=false;
            try {
                if(romeNumbers.containsKey(line)){
                    IsRomeNumbers=true;
                    number = romeNumbers.get(line);
                }else{
                    boolean findedOne=false;

                    for(char x:line.toCharArray()){
                        if(romeNumbers.containsKey(Character.toString(x))){
                            IsRomeNumbers=true;
                            findedOne=true;
                            number+=romeNumbers.get(Character.toString(x));
                        }
                    }
                    if(!findedOne){
                        number = Integer.parseInt(line);
                    }
                }
            } catch (Exception exc) {
                System.out.println("Ошибка! Не удалось определить число.");
                return -1;
            }
            if (number > 10 || number < 1) {
                System.out.println("Ошибка! Число должно находиться в пределах от 1 до 10: "+line);
                return -1;
            }

            if(IsRomeNumbers){
                typesNumbers.add(NumberType.Rome);
            }else{
                typesNumbers.add(NumberType.Arabic);
            }

            if (typesNumbers.size() > 1) {
                if (!CheckTypeNubmers()) {
                    System.out.println("Ошибка! Используются одновременно разные системы счисления!");
                    return -1;
                }
            }
            return number;
        }
        
        private boolean CheckTypeNubmers(){
            return typesNumbers.get(0)==typesNumbers.get(1);
        }

        private int[] allNumbers;
        public boolean IsCorrectly(int[] array){
            for (int number : array) {
                if(number<0){
                    return false;
                }
            }    
            allNumbers=array;
            return true;
        }

        public void MakeAction(Action action){
            var result = Action.makeAction(allNumbers[0], allNumbers[1], action);

            if(IsRomeNumbers){
                var stringResult = getRomeSymbol(result);
                System.out.println("Результат: "+stringResult);

            }else{
                System.out.println("Результат: "+result);
            }
        }

        private String getRomeSymbol(int number){
            if(number<0){
                return "В римской системе нет отрицательных чисел";
            }
            if(number==0){
                return "Ноль";
            }

            var sortedArray = romeNumbers.values().stream().toArray();
            Arrays.sort(sortedArray,Collections.reverseOrder());

            String symbol ="";
            while(number!=0){
                boolean gettedNumber=false;

                for(var value : sortedArray){
                    if(number>=(int)value&&!gettedNumber){
                        for(String key : romeNumbers.keySet()){
                            if(romeNumbers.get(key)==(int)value){                           
                                 symbol+=key;
                                 number-=(int)value;
                                 gettedNumber=true;    
                            }
                        }
                    }
                }
    
            }
            return symbol;
        }

    }
}