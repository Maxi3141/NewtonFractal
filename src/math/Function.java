package math;

import java.util.ArrayList;

public class Function {

	ArrayList<Operator> operatorList = new ArrayList<Operator>();
	ArrayList<Operator> variableList = new ArrayList<Operator>();

	public Function() {

	}

	public Function(ArrayList<Operator> operatorListNew, ArrayList<Operator> variableListNew) {
		operatorList = operatorListNew;
		variableList = variableListNew;
	}

	public Function(ArrayList<Operator>[] inputListArray) {
		operatorList = inputListArray[0];
		variableList = inputListArray[1];
	}

	void setOperatorList(ArrayList<Operator> operatorListNew) {
		operatorList = operatorListNew;
	}

	void setVariableList(ArrayList<Operator> variableListNew) {
		variableList = variableListNew;
	}

	ArrayList<Operator> getOperatorList() {
		return operatorList;
	}
	
	ArrayList<Operator> getVariableList() {
		return variableList;
	}

	public void debugTypePrint() {
		// TODO: Add indentation to highlight operator rank
		for(int i = 0; i < operatorList.size(); i++) {
			if(operatorList.get(i).getType() == OperatorType.CONST) {
				System.out.println("constant : real = " + operatorList.get(i).getValue().getReal() + " | imag = " + operatorList.get(i).getValue().getImag());
			}else {
				System.out.println(operatorList.get(i).getType());
			}
		}
	}

	ComplexNumber evaluate(ComplexNumber input) {

		for(int elementIndex = 0; elementIndex < variableList.size(); elementIndex++) {
			variableList.get(elementIndex).setValue(input);
		}

		for(int elementIndex = 0; elementIndex < operatorList.size(); elementIndex++) {
			operatorList.get(elementIndex).evaluate();
		}

		return operatorList.get(operatorList.size()-1).getValue();
	}

	public Function getDerivative() {
		Function result = getRawDerivative(this);

		result.variableList = new ArrayList<Operator>();
		for(int elementIndex = 0; elementIndex < result.getOperatorList().size(); elementIndex++) {
			if(result.getOperatorList().get(elementIndex).getType() == OperatorType.VAR) {
				result.getVariableList().add(result.getOperatorList().get(elementIndex));
			}
		}
		
		System.out.println("Found derivative:");
		result.debugTypePrint();
		System.out.println("Finished printing derivative.");
		
		return result;
	}

	Function getRawDerivative(Function inputFunc) {
		Function result = new Function();

		// I know, I know... but it works and it is only used once for every function, thus it can be slow.
		// Some day in the future this entire class needs to be rewritten and simplified.
		// ... and then we will also fix this mess.
		// ...
		// ... but today is not that day.

		if(dependsOnVar(inputFunc)) {
			OperatorType dominatingType = inputFunc.getOperatorList().get(inputFunc.getOperatorList().size() - 1).getType();
			switch(dominatingType) {
			case ADD:
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());
				int indexFirstInput = result.getOperatorList().size() - 1;
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1))).getOperatorList());
				ArrayList<Operator> inputList0TMP0 = new ArrayList<Operator>();
				inputList0TMP0.add(result.getOperatorList().get(indexFirstInput));
				inputList0TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.ADD, inputList0TMP0));
				break;
			case SUB:
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());
				int indexInput0 = result.getOperatorList().size() - 1;
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1))).getOperatorList());
				ArrayList<Operator> inputList1TMP0 = new ArrayList<Operator>();
				inputList1TMP0.add(result.getOperatorList().get(indexInput0));
				inputList1TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.SUB, inputList1TMP0));
				break;
			case MUL:
				if(!dependsOnVar(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)))) {
					// First term is constant:
					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
					int index2Input0 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1))).getOperatorList());
					ArrayList<Operator> inputList2TMP0 = new ArrayList<Operator>();
					inputList2TMP0.add(result.getOperatorList().get(index2Input0));
					inputList2TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList2TMP0));
				}else if(!dependsOnVar(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)))) {
					// Second term is constant:
					result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());
					int index2Input1 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
					ArrayList<Operator> inputList2TMP1 = new ArrayList<Operator>();
					inputList2TMP1.add(result.getOperatorList().get(index2Input1));
					inputList2TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList2TMP1));
				}else{
					// None of the terms is constant
					result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());
					int index2Input2 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
					ArrayList<Operator> inputList2TMP2 = new ArrayList<Operator>();
					inputList2TMP2.add(result.getOperatorList().get(index2Input2));
					inputList2TMP2.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList2TMP2));
					int index2Input3 = result.getOperatorList().size() - 1;

					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
					int index2Input4 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1))).getOperatorList());
					ArrayList<Operator> inputList2TMP3 = new ArrayList<Operator>();
					inputList2TMP3.add(result.getOperatorList().get(index2Input4));
					inputList2TMP3.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList2TMP3));
					int index2Input5 = result.getOperatorList().size() - 1;

					ArrayList<Operator> inputList2TMP4 = new ArrayList<Operator>();
					inputList2TMP4.add(result.getOperatorList().get(index2Input3));
					inputList2TMP4.add(result.getOperatorList().get(index2Input5));
					result.getOperatorList().add(new Operator(OperatorType.ADD, inputList2TMP4));
				}
				break;
			case DIV:
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());
				int index3Input0 = result.getOperatorList().size() - 1;
				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
				ArrayList<Operator> inputList3TMP0 = new ArrayList<Operator>();
				inputList3TMP0.add(result.getOperatorList().get(index3Input0));
				inputList3TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList3TMP0));
				int index3Input1 = result.getOperatorList().size() - 1;

				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
				int index3Input2 = result.getOperatorList().size() - 1;
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1))).getOperatorList());
				ArrayList<Operator> inputList3TMP1 = new ArrayList<Operator>();
				inputList3TMP1.add(result.getOperatorList().get(index3Input2));
				inputList3TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList3TMP1));
				int index3Input3 = result.getOperatorList().size() - 1;

				ArrayList<Operator> inputList3TMP2 = new ArrayList<Operator>();
				inputList3TMP2.add(result.getOperatorList().get(index3Input1));
				inputList3TMP2.add(result.getOperatorList().get(index3Input3));
				result.getOperatorList().add(new Operator(OperatorType.SUB, inputList3TMP2));
				int index3Input4 = result.getOperatorList().size() - 1;

				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
				int index3Input5 = result.getOperatorList().size() - 1;
				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
				ArrayList<Operator> inputList3TMP3 = new ArrayList<Operator>();
				inputList3TMP3.add(result.getOperatorList().get(index3Input5));
				inputList3TMP3.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList3TMP3));

				ArrayList<Operator> inputList3TMP4 = new ArrayList<Operator>();
				inputList3TMP4.add(result.getOperatorList().get(index3Input4));
				inputList3TMP4.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.DIV, inputList3TMP4));
				break;
			case POW:
				// Setup: "g^h"
				// Is h constant or does it depend on z?:
				Function TMPexponentFunction = getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1));

				if(dependsOnVar(TMPexponentFunction)) {
					// Add "exp(h*log(g))*(h'*log(g)+h*1/g*g'" 

					// Start "exp(h*log(g))"
					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
					int index4Input0 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
					ArrayList<Operator> inputList4TMP0 = new ArrayList<Operator>();
					inputList4TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.LOG, inputList4TMP0));

					ArrayList<Operator> inputList4TMP1 = new ArrayList<Operator>();
					inputList4TMP1.add(result.getOperatorList().get(index4Input0));
					inputList4TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList4TMP1));

					ArrayList<Operator> inputList4TMP2 = new ArrayList<Operator>();
					inputList4TMP2.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.EXP, inputList4TMP2));
					int index4Input1 = result.getOperatorList().size() - 1;

					// Assemble derivative of helper function "h*log(g)"
					Function helperFunction = new Function();
					helperFunction.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(1)).getOperatorList());
					int helper4Index0 = helperFunction.getOperatorList().size() - 1;
					helperFunction.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
					ArrayList<Operator> inputList4TMP3 = new ArrayList<Operator>();
					inputList4TMP3.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					helperFunction.getOperatorList().add(new Operator(OperatorType.LOG, inputList4TMP3));

					ArrayList<Operator> inputList4TMP4 = new ArrayList<Operator>();
					inputList4TMP4.add(result.getOperatorList().get(helper4Index0));
					inputList4TMP4.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					helperFunction.getOperatorList().add(new Operator(OperatorType.MUL, inputList4TMP4));
					helperFunction.debugTypePrint();

					result.getOperatorList().addAll(getRawDerivative(helperFunction).getOperatorList());
					ArrayList<Operator> inputList4TMP5 = new ArrayList<Operator>();
					inputList4TMP5.add(result.getOperatorList().get(index4Input1));
					inputList4TMP5.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList4TMP5));
				}else{
					// First "h*f^(h-1)"
					result.getOperatorList().addAll(TMPexponentFunction.getOperatorList());
					int index4Input2 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
					int index4Input3 = result.getOperatorList().size() - 1;
					result.getOperatorList().addAll(TMPexponentFunction.getOperatorList());
					result.getOperatorList().add(new Operator(OperatorType.CONST, new ComplexNumber(1, 0)));
					ArrayList<Operator> inputList4TMP6 = new ArrayList<Operator>();
					inputList4TMP6.add(result.getOperatorList().get(result.getOperatorList().size() - 2));
					inputList4TMP6.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.SUB, inputList4TMP6));

					ArrayList<Operator> inputList4TMP7 = new ArrayList<Operator>();
					inputList4TMP7.add(result.getOperatorList().get(index4Input3));
					inputList4TMP7.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.POW, inputList4TMP7));

					ArrayList<Operator> inputList4TMP8 = new ArrayList<Operator>();
					inputList4TMP8.add(result.getOperatorList().get(index4Input2));
					inputList4TMP8.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL,inputList4TMP8));
					int index4Input4 = result.getOperatorList().size() - 1;

					// Finish differentiating
					result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size() - 1).getInputs().get(0))).getOperatorList());
					ArrayList<Operator> inputList4TMP9 = new ArrayList<Operator>();
					inputList4TMP9.add(result.getOperatorList().get(index4Input4));
					inputList4TMP9.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
					result.getOperatorList().add(new Operator(OperatorType.MUL, inputList4TMP9));
				}
				break;
			case EXP:
				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
				ArrayList<Operator> inputList5TMP0 = new ArrayList<Operator>();
				inputList5TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.EXP, inputList5TMP0));
				int index5Input0 = result.getOperatorList().size() - 1;

				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());

				ArrayList<Operator> inputList5TMP1 = new ArrayList<Operator>();
				inputList5TMP1.add(result.getOperatorList().get(index5Input0));
				inputList5TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList5TMP1));
				break;
			case LOG:
				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size() - 1).getInputs().get(0))).getOperatorList());
				int index6Input0 = result.getOperatorList().size() - 1;
				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size() - 1).getInputs().get(0)).getOperatorList());

				ArrayList<Operator> inputList6TMP0 = new ArrayList<Operator>();
				inputList6TMP0.add(result.getOperatorList().get(index6Input0));
				inputList6TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.DIV, inputList6TMP0));
				break;
			case SIN:
				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
				ArrayList<Operator> inputList7TMP0 = new ArrayList<Operator>();
				inputList7TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.COS, inputList7TMP0));
				int index7Input0 = result.getOperatorList().size() - 1;

				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());

				ArrayList<Operator> inputList7TMP1 = new ArrayList<Operator>();
				inputList7TMP1.add(result.getOperatorList().get(index7Input0));
				inputList7TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList7TMP1));
				break;
			case COS:
				result.getOperatorList().addAll(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0)).getOperatorList());
				ArrayList<Operator> inputList8TMP0 = new ArrayList<Operator>();
				inputList8TMP0.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.SIN, inputList8TMP0));

				result.getOperatorList().add(new Operator(OperatorType.CONST, new ComplexNumber(-1, 0)));

				ArrayList<Operator> inputList8TMP1 = new ArrayList<Operator>();
				inputList8TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 2));
				inputList8TMP1.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList8TMP1));
				int index8Input0 = result.getOperatorList().size() - 1;

				result.getOperatorList().addAll(getRawDerivative(getSubFunc(inputFunc, inputFunc.getOperatorList().get(inputFunc.getOperatorList().size()-1).getInputs().get(0))).getOperatorList());

				ArrayList<Operator> inputList8TMP2 = new ArrayList<Operator>();
				inputList8TMP2.add(result.getOperatorList().get(index8Input0));
				inputList8TMP2.add(result.getOperatorList().get(result.getOperatorList().size() - 1));
				result.getOperatorList().add(new Operator(OperatorType.MUL, inputList8TMP2));
				break;
			case VAR:
				result.getOperatorList().add(new Operator(OperatorType.CONST, new ComplexNumber(1,0)));
				break;
			case CONST:
				result.getOperatorList().add(new Operator(OperatorType.CONST, new ComplexNumber(0,0)));
				break;
			default:
				break;
			}
		}else{
			result.getOperatorList().add(new Operator(OperatorType.CONST, new ComplexNumber(0,0)));
		}

		return result;
	}

	Function getSubFunc(Function inputFunc, Operator entryElement) {
		int entryElementIndex = inputFunc.getOperatorList().indexOf(entryElement);
		return getSubFuncFromIndex(inputFunc, entryElementIndex);
	}

	boolean dependsOnVar(Function inputFunc) {
		boolean answer = false;
		for(int i = 0; i < inputFunc.getOperatorList().size(); i++) {
			if(inputFunc.getOperatorList().get(i).getType() == OperatorType.VAR) {
				answer = true;
			}
		}
		return answer;
	}

	Function getSubFuncFromIndex(Function inputFunc, int entryIndex) {
		ArrayList<Operator> subOperatorList = new ArrayList<Operator>();
		subOperatorList.add(new Operator(inputFunc.getOperatorList().get(entryIndex).getType()));
		ArrayList<Operator> subVariableList  = new ArrayList<Operator>();
		int NumOpenBranches = inputFunc.getOperatorList().get(entryIndex).getNumberOfInputs();

		// Copy all operators with type and value

		int index = entryIndex;
		while(NumOpenBranches > 0) {
			index += -1;
			ArrayList<Operator> newElementOnSubOpList = new ArrayList<Operator>();
			newElementOnSubOpList.add(new Operator(inputFunc.getOperatorList().get(index).getType()));
			newElementOnSubOpList.get(0).setValue(inputFunc.getOperatorList().get(index).getValue());
			newElementOnSubOpList.addAll(subOperatorList);
			subOperatorList = newElementOnSubOpList;
			NumOpenBranches += inputFunc.getOperatorList().get(index).getNumberOfInputs() - 1;
		}

		// Link the operators using the input list
		int lastIndexUsed = index;
		for(int elementIndex = 0; elementIndex < subOperatorList.size(); elementIndex++) {
			for(int originalInputIndex = 0; originalInputIndex < inputFunc.getOperatorList().get(lastIndexUsed + elementIndex).getInputs().size(); originalInputIndex++) {
				int indexOriginalInput = inputFunc.getOperatorList().indexOf(inputFunc.getOperatorList().get(lastIndexUsed + elementIndex).getInputs().get(originalInputIndex));
				subOperatorList.get(elementIndex).addInput(subOperatorList.get(indexOriginalInput - lastIndexUsed));
			}
		}

		subOperatorList.get(subOperatorList.size() - 1).setValue(inputFunc.getOperatorList().get(entryIndex).getValue());

		// Add all operators of type VAR to variableList

		for(int i = 0; i < subOperatorList.size(); i++) {
			if(subOperatorList.get(i).getType() == OperatorType.VAR) {
				subVariableList.add(subOperatorList.get(i));
			}
		}

		return new Function(subOperatorList, subVariableList);
	}

}
