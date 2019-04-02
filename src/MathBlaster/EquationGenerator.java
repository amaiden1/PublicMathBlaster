/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathBlaster;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Austin Maiden
 */
public class EquationGenerator {
	private int difficulty = 3;
	private String operations = "+-";
	private String equation = "";
	private String formattedEquation = "";
	private int numOperators;
	private int numLimit = difficulty * 5;
	private int answer;
	
	public EquationGenerator(int givenDifficulty){
		difficulty = givenDifficulty;
	}
	
	public String newEquation() {
		adjustOperations();
		randomize();
		answer = getAnswer();
		System.out.println("Equation: " + equation);
		System.out.println("Answer: " + answer);
		return getEquation();
	}
	
	private void randomize(){
		//too high of exponents, divisor shouldn't be higher than numerator, division should result in whole numbers, final answer should always be positive?
		//limit to only one exponent and one division, cant divide by zero
		Random rand = new Random();
		numOperators = (difficulty >= 3)? 3 : difficulty;
		char[] operators = new char[numOperators];
		int[] numbers = new int[numOperators + 1];
		String result = "";
		for(int i = 0; i < numOperators; i++){
			//adds random operators (based on difficulty) to the operators String
			operators[i] = operations.charAt(rand.nextInt(operations.length()));
		}
		for(int i = 0; i <= numOperators; i++){
			//adds random numbers to the numbers String. Also adds comma delimiters
			numbers[i] = rand.nextInt(numLimit + 1);
		}
		checkEquation(operators, numbers);
		for(int i = 0; i < operators.length; i++){
			//combines the numbers and operators into a single pseudo-formatted String
			result += numbers[i];
			result += "," + operators[i] + ",";
		}
		result += numbers[numOperators];
		formatEquation(result); //will make it easily readable to the end user
		equation = result;
	}
	
	private void checkEquation(char[] symbols, int[] numbers){
		limitNumberDivisions(symbols);
		preventZeroDivision(symbols, numbers);
		forceGreaterNumerator(symbols, numbers);
	}
	
	private void limitNumberDivisions(char[] symbols){
		Random rand = new Random();
		boolean foundDivision = false;
		for(int i = 0; i < symbols.length; i++){
			while(foundDivision && symbols[i] == '/')
				symbols[i] = operations.charAt(rand.nextInt(operations.length()));
			if(symbols[i] == '/')
				foundDivision = true;
		}
	}
	
	private void forceGreaterNumerator(char[] symbols, int[] numbers){
		Random rand = new Random();
		for(int i = 0; i < symbols.length; i++){
			if(symbols[i] == '/'){
				int numerator = numbers[i];
				int divisor = numbers[i + 1];
				if(numerator % divisor != 0){
					int maxMultiple = numLimit / divisor;
					numbers[i] = divisor * rand.nextInt(maxMultiple + 1);
				}
			}
		}
	}
	
	private void preventZeroDivision(char[] symbols, int[] numbers){
		//ensures that the equation cannot divide by zero
		Random rand = new Random();
		for(int i = 0; i < symbols.length; i++){
			if(symbols[i] == '/'){
				while(numbers[i + 1] == 0)
					numbers[i+1] = rand.nextInt(numLimit) + 1;
			}
		}
	}
	
	public int getAnswer(){
		//will solve equation one part at a time, using PEMDAS
		int result = 0;
		Scanner elementScanner = new Scanner(equation);
		elementScanner.useDelimiter(",");
		ArrayList<String> elements = new ArrayList<>();
		
		while(elementScanner.hasNext())
			elements.add(elementScanner.next());
		System.out.println("Elements: " + elements.toString());
		while(elements.size() > 1){
			if(elements.contains("^")){
				int index = elements.indexOf("^");
				int num1 = Integer.parseInt(elements.get(index-1));
				int num2 = Integer.parseInt(elements.get(index+1));
				String op = elements.get(index);
				int resultOfOperation = answerHelp(num1, op, num2);
				elements.set(index - 1, "" + resultOfOperation);
				elements.remove(index);
				elements.remove(index);
			}
			else if(elements.contains("*") || elements.contains("/")){
				int index = -1; // must find a way to determine which operand comes first
				do{
					index += 2;
				}while(!elements.get(index).equals("*") && !elements.get(index).equals("/"));
				int num1 = Integer.parseInt(elements.get(index-1));
				int num2 = Integer.parseInt(elements.get(index+1));
				String op = elements.get(index);
				int resultOfOperation = answerHelp(num1, op, num2);
				elements.set(index - 1, "" + resultOfOperation);
				elements.remove(index);
				elements.remove(index);
			}
			else if(elements.contains("+") || elements.contains("-")){
				int index = -1; // must find a way to determine which operand comes first
				do{
					index += 2;
				}while(!elements.get(index).equals("+") && !elements.get(index).equals("-"));
				int num1 = Integer.parseInt(elements.get(index-1));
				int num2 = Integer.parseInt(elements.get(index+1));
				String op = elements.get(index);
				int resultOfOperation = answerHelp(num1, op, num2);
				elements.set(index - 1, "" + resultOfOperation);
				elements.remove(index);
				elements.remove(index);
			}
			System.out.println("New Elements: " + elements.toString());
		}
		result = Integer.parseInt(elements.get(0));
		return result;
	}
	
	private int answerHelp(int num1, String op, int num2){
		int result;
		if(op.equals("^"))
			result = (int) Math.pow(num1, num2);
		else if(op.equals("*"))
			result = num1 * num2;
		else if(op.equals("/"))
			result = num1 / num2;
		else if(op.equals("+"))
			result = num1 + num2;
		else if(op.equals("-"))
			result = num1 - num2;
		else
			result = 0;
		return result;
	}
	
	private void formatEquation(String given){
		//will make the equation appear pretty and easy to read
		String result = "X = ";
		Scanner scanEquation = new Scanner(given);
		scanEquation.useDelimiter(",");
		while(scanEquation.hasNext())
			result += scanEquation.next() + " ";
		System.out.println("Formatted String: " + result);
		formattedEquation = result;
	}
	
	public String getEquation(){
		return formattedEquation;
	}
	
	private void adjustOperations(){
		if(difficulty >= 3)
			operations += "*/";
		if(difficulty >= 4)
			operations += "^";
		if(difficulty >= 5)
			operations += "%";
	}
}
