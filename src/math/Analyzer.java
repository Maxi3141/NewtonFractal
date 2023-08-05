package math;

import java.util.ArrayList;
import java.util.Arrays;

public class Analyzer {

	ArrayList<Operator> operatorList;
	ArrayList<Operator> variableList;

	String variableName = "z";

	public Analyzer() {

	}

	public ArrayList<Operator>[] createFuncFromString(String inputString) {
		ArrayList<String> prepString = prepareReadableString(inputString);
		ArrayList<String> blockList = summarizeString(prepString);
		variableList = new ArrayList<Operator>();
		operatorList = new ArrayList<Operator>();
		analyzeBlockArray(blockList, variableName);

		ArrayList<Operator>[] result = new ArrayList[2];
		result[0] = operatorList;
		result[1] = variableList;

		return result;
	}

	ArrayList<String> prepareReadableString(String input) { 
		ArrayList<String> answer = new ArrayList<String>();
		ArrayList<String> TMPansArray = splitStrTotal(input);

		for(int index = 0; index < TMPansArray.size(); index++) {
			String element = TMPansArray.get(index);
			if(element.equals("-")) {
				if(index == 0) {
					answer.addAll(splitStrTotal("(0-1)*"));
				} else if(TMPansArray.get(index - 1) != ")" && !isSimpleNumber(TMPansArray.get(index - 1)) && !isOtherValue(TMPansArray.get(index - 1))) {
					answer.addAll(splitStrTotal("(0-1)*"));
				} else {
					answer.add("-");
				}
			}else if((isOtherValue(element) || element.equals("(")) && index > 0) {
				String preElement = TMPansArray.get(index - 1);
				if(isOtherValue(preElement) || isSimpleNumber(preElement) || preElement.equals(")")) {
					answer.add("*");
				}
				answer.add(element);
			}else{
				answer.add(element);
			}
		}
		return answer;
	}

	ArrayList<String> summarizeString(ArrayList<String> input) {
		ArrayList<String> answer = new ArrayList<String>();
		int index = 0;
		while(index < input.size()) {
			String currentBlock = "";
			if(isSimpleNumber(input.get(index))) {
				int currentTMPIndexShift = 0;
				while(isSimpleNumber(input.get(index + currentTMPIndexShift))) {
					currentBlock += input.get(index + currentTMPIndexShift);
					currentTMPIndexShift += 1;
					if(input.size() <= index + currentTMPIndexShift) {
						break;
					}
				}
			}else if(input.get(index).equals("e") && input.get(index + 1).equals("x") && input.get(index + 2).equals("p")) {
				currentBlock = "exp";
			}else if(input.get(index).equals("s") && input.get(index + 1).equals("i") && input.get(index + 2).equals("n")) {
				currentBlock = "sin";
			}else if(input.get(index).equals("c") && input.get(index + 1).equals("o") && input.get(index + 2).equals("s")) {
				currentBlock = "cos";
			}else if(input.get(index).equals("l") && input.get(index + 1).equals("o") && input.get(index + 2).equals("g")) {
				currentBlock = "log";
			}else{
				currentBlock = input.get(index);
			}
			answer.add(currentBlock);
			index += currentBlock.length();
		}
		return answer;
	}

	Operator analyzeBlockArray(ArrayList<String> inputBlockList, String varName) {
		ArrayList<String> blockList = inputBlockList;
		while(hasSurroundingBrackets(blockList)) {
			blockList.remove(0);
			blockList.remove(blockList.size() - 1);
		}

		int highestElementIndex = 0;
		int highestElementOrder = -1;
		int bracketOrder = 0;
		for(int index = 0; index < blockList.size(); index++) {
			String element = blockList.get(index);
			if(element.equals("(")) {
				bracketOrder += 1;
			}
			if(element.equals(")")) {
				bracketOrder -= 1;
			}
			if(bracketOrder == 0) {
				if(getElementOrder(element) >= highestElementOrder) {
					highestElementOrder = getElementOrder(element);
					highestElementIndex = index;
				}
			}
		}

		Operator newOperator = new Operator();

		if(blockList.get(highestElementIndex).equals(varName)) {
			newOperator.setType(OperatorType.VAR);
			operatorList.add(newOperator);
			variableList.add(newOperator);
		}
		if(isNumber(blockList.get(highestElementIndex))) {
			newOperator.setType(OperatorType.CONST);
			newOperator.setValue(new ComplexNumber(Double.valueOf(blockList.get(highestElementIndex)), 0));
			operatorList.add(newOperator);
		}
		if(blockList.get(highestElementIndex).equals("i")) {
			newOperator.setType(OperatorType.CONST);
			newOperator.setValue(new ComplexNumber(0, 1));
			operatorList.add(newOperator);
		}
		if(operatorStringType(blockList.get(highestElementIndex)) == 1) {
			newOperator.setType(getNonTrivialOperatorTypeFromString(blockList.get(highestElementIndex)));
			newOperator.addInput(analyzeBlockArray(getSubBlockList(blockList, 0, highestElementIndex), varName));
			newOperator.addInput(analyzeBlockArray(getSubBlockList(blockList, highestElementIndex+1, blockList.size()), varName));
			operatorList.add(newOperator);
		}
		if(operatorStringType(blockList.get(highestElementIndex)) == 2) {
			newOperator.setType(getNonTrivialOperatorTypeFromString(blockList.get(highestElementIndex)));
			newOperator.addInput(analyzeBlockArray(getSubBlockList(blockList, highestElementIndex+1, blockList.size()), varName));
			operatorList.add(newOperator);
		}

		return newOperator;
	}

	OperatorType getNonTrivialOperatorTypeFromString(String input) {
		switch(input.toUpperCase()) {
		case "+":
			return OperatorType.ADD;
		case "-":
			return OperatorType.SUB;
		case "*":
			return OperatorType.MUL;
		case "/":
			return OperatorType.DIV;
		case "^":
			return OperatorType.POW;
		case "SIN":
			return OperatorType.SIN;
		case "COS":
			return OperatorType.COS;
		case "EXP":
			return OperatorType.EXP;
		case "LOG":
			return OperatorType.LOG;
		default:
			return OperatorType.CONST;
		}
	}

	ArrayList<String> getSubBlockList(ArrayList<String> inputBlockList, int startIndex, int endIndex) {
		ArrayList<String> answer = new ArrayList<String>();
		for(int i = startIndex; i < endIndex; i++) {
			String nextElement = inputBlockList.get(i);
			answer.add(nextElement);
		}
		return answer;
	}

	boolean hasSurroundingBrackets(ArrayList<String> blockList) {
		boolean answer = false;
		if(blockList.get(0).equals("(") && blockList.get(blockList.size() - 1).equals(")")) {
			answer = true;
			int bracketOrder = 1;
			for(int i = 1; i < blockList.size() - 1; i++) {
				if(blockList.get(i).equals("(")) {
					bracketOrder += 1;
				}
				if(blockList.get(i).equals(")")) {
					bracketOrder -= 1;
				}
				if(bracketOrder == 0) {
					answer = false;
				}
			}
		}
		return answer;
	}

	int getElementOrder(String input) {
		int answer = 0;

		String[] trigFunc = {"sin", "cos", "exp", "log"};
		if(Arrays.asList(trigFunc).contains(input)) {
			answer = 0;
		}else if(input.equals("^")) {
			answer = 1;
		}else if(input.equals("*") || input.equals("/")) {
			answer = 2;
		}else if(input.equals("+") || input.equals("-")) {
			answer = 3;
		}else{
			answer = -1;
		}

		return answer;
	}

	boolean isSimpleNumber(String input) {
		String[] numLetters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};
		return  Arrays.asList(numLetters).contains(input);
	}

	boolean isOtherValue(String input) {
		String[] otherLetters = {"i", variableName};
		return Arrays.asList(otherLetters).contains(input);
	}

	boolean isNumber(String input) {
		boolean answer = true;
		try {
			double testDouble = Double.valueOf(input);
		}catch(Exception e) {
			answer = false;
		}
		return answer;
	}

	ArrayList<String> splitStrTotal(String input) {
		String[] totalSimpleSplitArrayTMP = input.split("");
		ArrayList<String> answer = new ArrayList<String>();
		for(int i = 0; i < totalSimpleSplitArrayTMP.length; i++) {
			answer.add(totalSimpleSplitArrayTMP[i]);
		}
		return answer;
	}

	int operatorStringType(String input) {
		String[] firstTypeOperators = {"+", "-", "*", "/", "^"};
		String[] secTypeOperators = {"log", "exp", "sin", "cos"};
		if(Arrays.asList(firstTypeOperators).contains(input)) {
			return 1;
		}else if(Arrays.asList(secTypeOperators).contains(input)) {
			return 2;
		}else{
			return 0;
		}
	}

}
