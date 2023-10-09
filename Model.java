package Lab3;

import java.io.*;
import java.util.*;


/** The Model class for executing the program
 * some ideas were taken and modified from
 * https://algorithms.tutorialhorizon.com/evaluation-of-infix-expressions/
 */


public class Model {
    Hashtable<Integer, String> lineToLine;
    Hashtable<String, Double> mapValues;
    private Vector<String> textFileContent ;
    private Vector<String> output;
    /** Constructor with no parameters
     */
    public Model(){
        lineToLine = new Hashtable<>();
        output = new Vector<>();
        textFileContent = new Vector<>();
        mapValues = new Hashtable<>();
    }


    /**
     * @param path Absolute path for file to read from.
     * @return Vector that contains file content.
     * @throws IOException when file cannot be found.
     */
    public Vector<String> readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String st;
        // Condition holds true if there is character in a string
        while ((st = reader.readLine()) != null) {
            //empty lines are meaningless
            if(st.equals(""))
                continue;
            textFileContent.add(st+"\n");
        }
        reader.close();

        return textFileContent;
    }

    /**
     * Executes program once the file has been read in successfully.
     */
    public void run(){


        createTable();
        for(int i=0; i<textFileContent.size(); i++){
            String line = textFileContent.get(i).trim();
            System.out.println(line);

            if(isComment(line)){ //comment lines should be skipped.
                continue;
            }
            else if(isAssignment(line)){

            }
            //done
            else if(isPrint(line)){
                String variable = line.substring(line.indexOf("t")+1).trim();

                if(hasVariable(variable)){
                    output.add(mapValues.get(variable)+"\n");
                }
                else{ //if user tries to print out variable that does not exist.
                    throw new IllegalArgumentException("Error at line: \""+ line +"\". \n Variable \"" + variable + "\" does not exist");
                }
                System.out.println(variable  + " = " + mapValues.get(variable));


            }
            //
            else if(isConditional(line)){
                String variable = findConditionalVar(line);
                double value = findConditionalValue(line);

                if(hasVariable(variable)){
                    if(mapValues.get(variable)  ==  value){
                        int lineNum = findLineNumber(line);//collects line number to  go to

                        int newLineToGoTo = findLineThatContains(lineNum); //finds string that starts with that line

                        if(newLineToGoTo>-1) {
                            //since i will be incremented next iteration. i+1-1 =i;
                            i = newLineToGoTo-1;
                        }
                        else{
                            throw new IllegalArgumentException("Error at line: \""+ line+"\" . Line does not exist");
                        }
                    }


                }

                else{
                    throw new IllegalArgumentException("Error at line: \""+ line+"\" . Variable does not exist");
                }

            }
            //done
            else if(islineChange(line)){
                int lineNum = findLineNumber(line);//finds line to go to

                int newLineToGoTo = findLineThatContains(lineNum); //finds string that starts with that line

                if(newLineToGoTo>-1) {
                    //since i will be incremented next iteration. i+1-1 =i;
                    i = newLineToGoTo-1;
                }
                else{
                    throw new IllegalArgumentException("Error at line: \""+ line+"\" ");
                }

            }

            else if(isEnd(line)){
                    break;
            }
            else{
                throw new IllegalArgumentException("Error at line: \""+ line+"\" ");
            }


        }

    }
    private void createTable(){

        for(String line: textFileContent) {
            int indx = 0;
            String lineNumber = "";
            String lineContent = "";
            while (indx < line.length() && Character.isDigit(line.charAt(indx))) {
                lineNumber += line.charAt(indx);
                indx++;
            }
            while (indx < line.length()) {
                lineContent += line.charAt(indx);
                indx++;
            }

            if(lineToLine.containsKey(Integer.parseInt(lineNumber))){
                System.out.println(lineNumber);
                throw new IllegalArgumentException("Problem at line: "+lineNumber +". Line numbers must be unique");
            }

            lineToLine.put(Integer.parseInt(lineNumber), lineContent);
        }

    }

    /**
     * @return String that contains print statement output from program
     */
    public String getOutput(){
        StringBuilder result = new StringBuilder();
        for(String outputLine : output){
            result.append(outputLine);
        }
        return result.toString();
    }

    private boolean hasVariable(String variable){
        return mapValues.containsKey(variable);
    }

    private void performAssignment(String variableName, Double value){
        mapValues.put(variableName, value);
    }

    private boolean isAssignment(String line){
        //simple assignment such as x=5
        if(line.matches("^[0-9]+[\\s]*[a-zA-Z]+[\\s]*=[\\s]*[0-9]+$")){

            String variable = findAssignmentVar(line);
            Double value = findAssignmentValue(line);

            performAssignment(variable, value);

            return true;
        }
        //matches complex operational assignment such as x = i+5/4 * 6
        else if(line.matches("^[0-9]+[\\s]*[a-zA-Z]+[\\s]*=.+$")){

            String variable = findAssignmentVar(line);
            String expression = getExpression(line);

            Double result = evaluate(expression);

            performAssignment(variable, result);

            return true;

        }
        return false;
    }
    private boolean isPrint(String line){
        return line.matches("^[0-9]+[\\s]*print[\\s]+[A-Za-z]+.*");
    }
    private boolean islineChange(String line){
        return line.matches("^[0-9]+[\\s]*goto[\\s]+[0-9]+");
    }
    private boolean isConditional(String line){
        return line.matches("^[0-9]+[\\s]*if[\\s]*[(][\\s]*[a-zA-Z]+[\\s]*=[\\s]*[\\d+(\\.\\d+)?]+[\\s]*[)][\\s]*goto[\\s]+[0-9]+[\\s]*[/]*.*");
    }
    private boolean isComment(String line){
        return line.matches("^[0-9]+[\\s]*[/]{2}.*");
    }
    private boolean isEnd(String line){
        return line.matches("^[0-9]+[\\s]*end.*");

    }
    private String getExpression(String line){
        String supportedOperators = "+-/*^().";
        StringBuilder expression = new StringBuilder();


        //pointer begins at indx after equals("=") symbol
        int indx = line.indexOf("=") + 1;

        //skip whitespace
        while(indx<line.length() && line.charAt(indx) == ' '){
            indx++;
        }

        while(indx<line.length()){
            char curr = line.charAt(indx);

            if(Character.isLetter(curr)){
                String variableName = "";

                //variable name can more than one character
                while(indx < line.length() && Character.isLetter(line.charAt(indx))){
                    variableName= variableName+ (line.charAt(indx)+"");
                    indx++;
                }

                //prevents skipping of next character in next iteration.
                indx--;

                //all variables in the expression should already exist within program. i.e, be initialized.
                if(!hasVariable(variableName)) {
                    throw new IllegalArgumentException("Variable : " + variableName + " not initialized");
                }

                expression.append(mapValues.get(variableName));

            }
            //check if is operator
            else if(supportedOperators.indexOf(curr) >= 0){
                expression.append(curr);
            }

            //check if is digit
            else if(Character.isDigit(curr)) {
                expression.append(curr);
            }
            else if(curr == ' '){
                expression.append(" ");
            }
            else{
                throw new UnsupportedOperationException("Cannot use \" " + curr + " operator"  );
            }

            indx++;
        }
        return expression.toString();

    }
    private double evaluate(String expression){
        //Stack for Numbers
        Stack<Double> numbers = new Stack<>();

        //Stack for operators
        Stack<Character> operations = new Stack<>();
        for(int i=0; i<expression.length();i++) {
            char c = expression.charAt(i);
            //check if it is number
            if(Character.isDigit(c)){
                //Entry is Digit, it could be greater than one digit number and be a decimal
                int startPos = i;
                double num = 0;
                while (Character.isDigit(c)||c=='.') {
                    i++;
                    if(i < expression.length())
                        c = expression.charAt(i);
                    else
                        break;
                }
                num = Double.parseDouble(expression.substring(startPos, i));
                i--;
                //push it into stack
                numbers.push(num);
            }else if(c=='('){
                //push it to operators stack
                operations.push(c);
            }
            //Closed brace, evaluate the entire brace
            else if(c==')') {
                while(operations.peek()!='('){
                    double output = performOperation(numbers, operations);
                    //push it back to stack
                    numbers.push(output);
                }
                operations.pop();
            }
            // current character is operator
            else if(isOperator(c)){
                //1. If current operator has higher precedence than operator on top of the stack,
                //the current operator can be placed in stack
                // 2. else keep popping operator from stack and perform the operation in  numbers stack till
                //either stack is not empty or current operator has higher precedence than operator on top of the stack
                while(!operations.isEmpty() && precedence(c)<=precedence(operations.peek())){
                    double output = performOperation(numbers, operations);
                    //push it back to stack
                    numbers.push(output);
                }
                //now push the current operator to stack
                operations.push(c);
            }
        }
        //If here means entire expression has been processed,
        //Perform the remaining operations in stack to the numbers stack

        while(!operations.isEmpty()){
            double output = performOperation(numbers, operations);
            //push it back to stack
            numbers.push(output);
        }
        return numbers.pop();
    }

    private int precedence(char c){
        switch (c){
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    private double performOperation(Stack<Double> numbers, Stack<Character> operations) {
        double a = numbers.pop();
        double b = numbers.pop();
        char operation = operations.pop();
        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return b - a;
            case '*':
                return a * b;
            case '/':
                if (a == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return b / a;
        }
        return 0;
    }

    private boolean isOperator(char c){
        return (c=='+'||c=='-'||c=='/'||c=='*'||c=='^');
    }

    private String findAssignmentVar(String line){

        int indx=0;
        while(indx<line.length() && !Character.isLetter(line.charAt(indx)) ){
            indx++;
        }
        String variable = "";
        while(Character.isLetter(line.charAt(indx))){
            variable+= line.charAt(indx);
            indx++;
        }

        return variable;
    }

    private Double findAssignmentValue(String line){
        int indx=0;
        //skip till "="
        while(indx<line.length() && line.charAt(indx)!= '=' ){
            indx++;
        }
        indx++;

        //skip whitespace
        while(indx<line.length() && line.charAt(indx)==' ' ){
            indx++;
        }

        //get digit values
        String value ="";
        while(indx<line.length() && Character.isDigit(line.charAt(indx))){
            value+= line.charAt(indx)+"";
            indx++;
        }



        return Double.parseDouble(value);
    }
    private Integer findLineNumber(String line){
        //skip to the 'o' in the goto statement then skip that as well.
        int indx=line.lastIndexOf('o')+1;

        //skip whitespace
        while(indx<line.length() && line.charAt(indx)==' '){
            indx++;
        }

        Integer result = 0;

        while(indx<line.length() && Character.isDigit(line.charAt(indx))){
            int digit = line.charAt(indx) - '0';
            result  = result * 10 + digit;
            indx++;
        }

        return result;

    }
    private String findConditionalVar(String line){
        int indx = line.indexOf("if")+2;
        //skip whitespace,brackets and everything that is not the conditional variable
        while(indx<line.length() && !Character.isLetter(line.charAt(indx))){
            indx++;
        }
        String variable = "";

        while(indx<line.length() && Character.isLetter(line.charAt(indx))){
            variable+=line.charAt(indx);
            indx++;
        }

        return variable;
    }
    private double findConditionalValue(String line){
        int indx = line.indexOf("=")+1;

        //skip whitespace
        while(indx<line.length() && line.charAt(indx)== ' '){
            indx++;
        }
        String value = "";

        while(indx<line.length() && Character.isDigit(line.charAt(indx))){
            value+=line.charAt(indx);
            indx++;
        }
        return Double.parseDouble(value);
    }
    private int findLineThatContains(int lineNum){
        for(int i=0; i<textFileContent.size(); i++){
            if(textFileContent.get(i).startsWith(""+lineNum))
                return i;
        }
        return -1;
    }


}

