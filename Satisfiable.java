/**Jackson Gray
 * COSC 2375
 * 02/22/2020
 */
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
public class Satisfiable { 
    //Reads a file containing propositions
    public static void main(String[] args) {
        try {
            File file = new File("satisfiability.txt");
            Scanner scan = new Scanner(file);
            List<String> lines = new ArrayList<String>();
            while(scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            
            Satisfiable satisfiable = new Satisfiable();
            for(String s : lines) {
                if(satisfiable.isSatisfiable(s)) {
                    System.out.println(s + " is satisfiable");
                }
                else {
                    System.out.println(s + " is not satisfiable");
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Converts the propositions into symbols used in standard Java Boolean statements and tests if they are satisfiable
    public boolean isSatisfiable(String input) throws java.lang.Exception {
        String reformat = "";
        String variables = "";
        for(int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if(current == '¬' || current == '!') {
                reformat += "! ";
            }
            else if(current == '⋁') {
                reformat += "|| ";
            }
            else if(current == '⋀') {
                reformat += "&& ";
            }
            else if(current == '|' || current == '&') {
                //If not paired, throw
                if(i == input.length() || (i < input.length() - 1 && input.charAt(i + 1) != current)) {
                    System.out.println(input + " has improper syntax. & and | tokens must be paired.");
                    throw new Exception("Improper syntax: " + input);
                }
                reformat += "" + current + current + " ";
                i++;
            }
            else if(Character.isLetter(current)){
                //If character is adjacent to another character, throw
                if(i >  0 && Character.isLetter(input.charAt(i - 1))) {
                    System.out.println(input + " has improper syntax. Two variables cannot be adjacent.");
                    throw new Exception("Improper syntax: " + input);
                }
                //If variable isn't in queue, add to queue
                if(!variables.contains(current + "")) {
                    variables += current;
                }
                //Add variable to reformat
                reformat += current + " ";
            }
            else if (current != ' ' && current != '(' && current != ')') {
                System.out.println(input + " has improper syntax. " + current + " is not a valid character!");
                throw new Exception("Improper syntax. \n" + input);
            }
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String[] equation = reformat.split(" ");
        
        
        return solve(equation, variables);
    }
    
    //Recursively tests if equation can be satisfied replacing each variable with T or F
    private boolean solve(String[] equation, String variables) throws javax.script.ScriptException {
        if(variables.length() == 0) return false;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        boolean testBool = true;
        char testVariable = variables.charAt(0);
        //Test True, then False
        for(int i = 0; i < 2; i++) {
            //Replace each instance of variable with True or False
            for(int j = 0; j < equation.length; j++) {
                if(equation[j].equals(testVariable + "")) {
                    equation[j] = Boolean.toString(testBool);
                }
            }
            if(variables.length() == 1) {
                //All variables loaded. Ready to check.
                return Boolean.TRUE.equals(engine.eval(stringArrayToString(equation)));
            }
            else if (solve(equation, variables.substring(1))) {
                return true;
            }
            //Flip testBool to False
            testBool = !testBool;
        } 
        return false;
    }
    
    //converts a String[] into a String without separators
    public static String stringArrayToString(String[] stringArray) {
        String ret = "";
        for(String s : stringArray) {
            ret += s;
        }
        return ret;
    }
}