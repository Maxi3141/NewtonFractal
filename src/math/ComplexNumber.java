package math;

public class ComplexNumber {

	double real  = 0;
	double imag = 0;

	public ComplexNumber() {

	}

	public ComplexNumber(double realNew, double imagNew) {
		real = realNew;
		imag = imagNew;
	}

	public ComplexNumber(ComplexNumber compN) {
		real = compN.getReal();
		imag = compN.getImag();
	}

	double getReal() {
		return real;
	}
	double getImag() {
		return imag;
	}

	public void setReal(double realNew) {
		real = realNew;
	}

	public void setImag(double imagNew) {
		imag = imagNew;
	}

	void add(ComplexNumber summ) {
		real += summ.getReal();
		imag += summ.getImag();
	}

	void sub(ComplexNumber subt) {
		real -= subt.getReal();
		imag -= subt.getImag();
	}

	void mul(ComplexNumber mult) {
		double realOLD = real;
		double imagOLD = imag;
		real = realOLD*mult.real - imagOLD*mult.getImag();
		imag = realOLD*mult.imag + imagOLD*mult.getReal();
	}

	void div(ComplexNumber divi) {
		double realOLD = real;
		double imagOLD = imag;
		real = (realOLD*divi.getReal() + imagOLD*divi.getImag())/(Math.pow(divi.getReal(), 2) + Math.pow(divi.getImag(), 2));
		imag = (imagOLD*divi.getReal() - realOLD*divi.getImag())/(Math.pow(divi.getReal(), 2) + Math.pow(divi.getImag(), 2));
	}

	void mulScale(double scale) {
		real = real * scale;
		imag = imag * scale;
	}

	ComplexNumber getAdd(ComplexNumber summ) {
		ComplexNumber tmpNumber = new ComplexNumber(real, imag);
		tmpNumber.add(summ);
		return tmpNumber;
	}

	ComplexNumber getSub(ComplexNumber subt) {
		ComplexNumber tmpNumber = new ComplexNumber(real, imag);
		tmpNumber.sub(subt);
		return tmpNumber;
	}

	ComplexNumber getMul(ComplexNumber mult) {
		ComplexNumber tmpNumber = new ComplexNumber(real, imag);
		tmpNumber.mul(mult);
		return tmpNumber;
	}

	ComplexNumber getDiv(ComplexNumber divi) {
		ComplexNumber tmpNumber = new ComplexNumber(real, imag);
		tmpNumber.div(divi);
		return tmpNumber;
	}

	ComplexNumber power(ComplexNumber expo) {
		return new ComplexNumber(expo.getMul(getPrincipalLog())).getExp();
	}

	void conjugate() {
		imag = -imag;
	}

	double getAbs() {
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imag, 2));
	}

	double getTheta() {
		return Math.atan2(imag, real);
	}

	ComplexNumber getPrincipalLog() {
		return new ComplexNumber(Math.log(getAbs()), getTheta());
	}

	ComplexNumber getExp()  {
		return new ComplexNumber(Math.exp(real)*Math.cos(imag), Math.exp(real)*Math.sin(imag));
	}

	ComplexNumber getSin() {
		return new ComplexNumber(Math.sin(real)*Math.cosh(imag), Math.cos(real)*Math.sinh(imag));
	}

	ComplexNumber getCos() {
		return new ComplexNumber(Math.cos(real)*Math.cosh(imag), -Math.sin(real)*Math.sinh(imag));
	}

}
