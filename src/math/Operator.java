package math;

import java.util.ArrayList;

public class Operator {
	
	ComplexNumber value = new ComplexNumber(0, 0);
	OperatorType type = OperatorType.EMPTY;
	ArrayList<Operator> input;
    
	Operator() {
		input = new ArrayList<Operator>();
	}
    
	Operator(OperatorType typeNew) {
		type = typeNew;
		input = new ArrayList<Operator>();
	}
    
	Operator(OperatorType typeNew, ArrayList<Operator> inputNew) {
		type = typeNew;
		input = inputNew;
	}
    
	Operator(OperatorType typeNew, ComplexNumber valueNew) {
		type = typeNew;
		value = valueNew;
		input = new ArrayList<Operator>();
	}
		    
	void evaluate() {
		switch(type){
		case ADD:
			value = input.get(0).getValue().getAdd(input.get(1).getValue());
			break;
		case SUB:
			value = input.get(0).getValue().getSub(input.get(1).getValue());
			break;
		case MUL:
			value = input.get(0).getValue().getMul(input.get(1).getValue());
			break;
		case DIV:
			value = input.get(0).getValue().getDiv(input.get(1).getValue());
			break;
		case POW:
			value = input.get(0).getValue().power(input.get(1).getValue());
			break;
		case EXP:
			value = input.get(0).getValue().getExp();
			break;
		case LOG:
			value = input.get(0).getValue().getPrincipalLog();
			break;
		case SIN:
			value = input.get(0).getValue().getSin();
			break;
		case COS:
			value = input.get(0).getValue().getCos();
			break;
		default:
			break;
		}
	} 
    
	void setType(OperatorType typeNew) {
		type = typeNew;
	}
    
	void setValue(ComplexNumber valueNew) {
		value = valueNew;
	}
    
	void setInput(ArrayList<Operator> inputNew) {
		input = inputNew;
	}
    
	void addInput(Operator addInput) {
		input.add(addInput);
	}
    
	ArrayList<Operator> getInputs() {
		return input;
	}
    
	OperatorType getType() {
		return type;
	}
    
	ComplexNumber getValue() {
		return value;
	}
    
	int getNumberOfInputs() {
		return input.size();
	}
	
}
