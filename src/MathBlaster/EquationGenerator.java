/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathBlaster;

import static java.lang.Math.pow;
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
		limitNumberExponents(symbols);
		limitExponentValues(symbols, numbers);
		limitNumberDivisions(symbols);
		preventDivisionExponentError(symbols);
		preventZeroDivision(symbols, numbers);
		forceGreaterNumerator(symbols, numbers);
		changeLogValues(symbols, numbers);
	}
	
	private void changeLogValues(char[] symbols, int[] numbers){
		Random rand = new Random();
		for(int i = 0; i < symbols.length; i++){
			if(symbols[i] == '%'){
				numbers[i] = 2;
				numbers[i+1] = (int)pow(2, (rand.nextInt(6)+1));
			}
		}
	}
	
	private void limitNumberDivisions(char[] symbols){
		Random rand = new Random();
		boolean foundDivision = false;
		for(int i = 0; i < symbols.length; i++){
			while(foundDivision && (symbols[i] == '/' || symbols[i] == '^'))
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
	
	private void preventDivisionExponentError(char[] symbols){
		//removes the possibility of division and exponents interfering with result
		Random rand = new Random();
		for(int i = 0; i < symbols.length-1; i++){
			if(symbols[i] == '^' || symbols[i] == '/' || symbols[i] == '%'){
				while(symbols[i+1] == '/' || symbols[i+1] == '^' || symbols[i+1] == '%')
					symbols[i+1] = operations.charAt(rand.nextInt(operations.length()));
			}
		}
	}
	
	private void limitNumberExponents(char[] symbols){
		Random rand = new Random();
		boolean foundExponent = false;
		for(int i = 0; i < symbols.length; i++){
			while(foundExponent && (symbols[i] == '^' || symbols[i] == '%'))
				symbols[i] = operations.charAt(rand.nextInt(operations.length()));
			if(symbols[i] == '^' || symbols[i] == '%')
				foundExponent = true;
		}
	}
	
	private void limitExponentValues(char[] symbols, int[] numbers){
		//limits the maximum value of an exponential number
		Random rand = new Random();
		for(int i = 0; i < symbols.length; i++){
			if(symbols[i] == '^'){
				// >5 = ^2 limit
				// 4-5 = ^3 limit
				// 3 = ^4 limit
				// <3 = ^5 limit
				int exponentLimit = -1;
				if(numbers[i] > 5){ //5 and up
					exponentLimit = 2;
				}
				else if(numbers[i] > 3){ //4 and 5
					exponentLimit = 3;
				}
				else if(numbers[i] == 3){ //3, obviously
					exponentLimit = 4;
				}
				else{ //less than 3
					exponentLimit = 5;
				}
				numbers[i+1] = rand.nextInt(exponentLimit + 1);
				System.out.println("exponent limited to: " + exponentLimit);
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
		while(elements.size() > 1){
			if(elements.contains("^") || elements.contains("%")){
				int index = -1; // must find a way to determine which operand comes first
				do{
					index += 2;
				}while(!elements.get(index).equals("^") && !elements.get(index).equals("%"));
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
			System.out.println("Stuck here");
		}
		result = Integer.parseInt(elements.get(0));
		return result;
	}
	
	private int answerHelp(int num1, String op, int num2){
		int result = Integer.MIN_VALUE;
		if(op.equals("^"))
			result = (int) Math.pow(num1, num2);
		else if(op.equals("%")){
			boolean found = false;
			for(int i = 0; i < 7 && !found; i++){
				if(pow(num1, i) == num2){
					result = i;
					found = true;
				}
			}
		}
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
		while(scanEquation.hasNext()){
			String temp = scanEquation.next();
			if(temp.equals("%")){
				String withLog = "";
				for(int i = 0; i < result.length()-2; i++){
					withLog += result.charAt(i);
				}
				result = withLog;
				result += "log" + Constants.baby2 + "(" + scanEquation.next() + ") ";
			}
			else
				result += temp + " ";
		}
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
