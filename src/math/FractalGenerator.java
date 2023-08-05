package math;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import gui.ProgressFrame;

public class FractalGenerator {

	public FractalGenerator() {

	}

	public Color[][] createFractal(int pixelWidth, int pixelHeight, ComplexNumber topLeftComplex, ComplexNumber botRightComplex, Function map, ArrayList<Double> colorBasis,
			double rootTol, double groupTol, int maxIter) {
		double brightLevel = 10;
		double brightMinLevel = 0.4;
		double brightMaxLevel = 1.0;
		ArrayList<Double> colorRange = colorBasis;

		int[][][] rawColorMap = analyzeConvergenceMap(createConvergenceMap(pixelWidth, pixelHeight, topLeftComplex, botRightComplex, map, rootTol, maxIter), rootTol, groupTol);

		int numNeededColors = 0;
		for(int colIndex = 0; colIndex < rawColorMap.length; colIndex++) {
			for(int rowIndex = 0; rowIndex < rawColorMap[colIndex].length; rowIndex++) {
				if(rawColorMap[colIndex][rowIndex][0] > numNeededColors) {
					numNeededColors = rawColorMap[colIndex][rowIndex][0];
				}
			}
		} 

		Random rndColorGen = new Random();
		while(colorBasis.size() < numNeededColors) {
			colorRange.add(rndColorGen.nextDouble());
		}

		Color[][] answer = new Color[pixelWidth][pixelHeight];
		for(int colIndex = 0; colIndex < rawColorMap.length; colIndex++) {
			Color[] nextColorColumn = new Color[pixelHeight];
			for(int rowIndex = 0; rowIndex < rawColorMap[colIndex].length; rowIndex++) {
				if(rawColorMap[colIndex][rowIndex][0] == 0) {
					nextColorColumn[rowIndex] = Color.BLACK;
				}else{
					double TMPhue = colorRange.get(rawColorMap[colIndex][rowIndex][0] - 1);
					double TMPbright = brightMaxLevel - (brightMaxLevel - brightMinLevel)/brightLevel*Double.valueOf(rawColorMap[colIndex][rowIndex][1]);
					if(TMPbright < brightMinLevel) {
						TMPbright = brightMinLevel;
					}
					nextColorColumn[rowIndex] = Color.getHSBColor((float) TMPhue, 1.0f, (float)TMPbright);
				}
			}
			answer[colIndex] = nextColorColumn;
			System.out.println("Computed column " + colIndex + " / " + rawColorMap.length + " for color map.");
		}

		return answer;
	}

	ComplexNumber[][][] createConvergenceMap(int pixelWidth, int pixelHeight, ComplexNumber topLeftComplex, ComplexNumber botRightComplex, Function map, double tol, int maxI) {
		ComplexNumber[][][] answer = new ComplexNumber[pixelWidth][pixelHeight][3];
		Function mDeriv = map.getDerivative();

		for(int colIndex = 0; colIndex < pixelWidth; colIndex++) {
			ComplexNumber[][] currentColumn = new ComplexNumber[pixelHeight][3];
			for(int rowIndex = 0; rowIndex < pixelHeight; rowIndex++) {
				double currentReal = (botRightComplex.getReal() - topLeftComplex.getReal())*(((double)colIndex)/((double)pixelWidth)) + topLeftComplex.getReal();
				double currentImag = (topLeftComplex.imag - botRightComplex.imag)*((double)(pixelHeight - rowIndex)/((double)pixelHeight)) + botRightComplex.getImag();

				ComplexNumber startValue = new ComplexNumber(currentReal, currentImag);
				currentColumn[rowIndex] = approximateRoot(map, mDeriv, startValue, tol, maxI);
			}
			answer[colIndex] = currentColumn;
			System.out.println("Computed convergence for column " + colIndex + " / " + pixelWidth);
		}

		return answer;
	}

	int[][][] analyzeConvergenceMap(ComplexNumber[][][] inputData, double rootTol, double groupTol) {
		int[][][] arrayAnswer = new int[inputData.length][inputData[0].length][2];

		ArrayList<ComplexNumber> rootList = new ArrayList<ComplexNumber>();

		for(int colIndex = 0; colIndex < inputData.length; colIndex++) {
			int[][] currentColumn = new int[inputData[colIndex].length][2];
			for(int rowIndex = 0; rowIndex < inputData[colIndex].length; rowIndex++) {
				int convType = 0;
				if(inputData[colIndex][rowIndex][0].getAbs() <= rootTol) {
					boolean foundMatchingRoot = false;
					for(int rootIndex = 0; rootIndex < rootList.size(); rootIndex++) {
						if (inputData[colIndex][rowIndex][1].getSub(rootList.get(rootIndex)).getAbs() <= groupTol) {
							convType = rootIndex+1;
							foundMatchingRoot = true;
							break;
						}
					}
					if(!foundMatchingRoot) {
						rootList.add(inputData[colIndex][rowIndex][1]);
						convType = rootList.size();
					}
				}
				currentColumn[rowIndex][0] = convType;
				currentColumn[rowIndex][1] = (int)inputData[colIndex][rowIndex][2].getReal();
			}
			arrayAnswer[colIndex] = currentColumn;
		}

		return arrayAnswer;
	}

	ComplexNumber[] approximateRoot(Function mapping, Function mapDerivative, ComplexNumber startVal, double tolerance, int maxIter) {

		ComplexNumber limit = new ComplexNumber(startVal.getReal(), startVal.getImag());
		int iterations = 0;

		for(int i = 0; i < maxIter; i++) {
			if(mapping.evaluate(limit).getAbs() < tolerance) {
				break;
			}
			iterations += 1;
			ComplexNumber mappingNow = mapping.evaluate(limit);
			ComplexNumber derivativeNow = mapDerivative.evaluate(limit);
			limit.sub(mappingNow.getDiv(derivativeNow));
		}

		ComplexNumber[] answer = new ComplexNumber[3];
		answer[0] = mapping.evaluate(limit);
		answer[1] = limit;
		answer[2] = new ComplexNumber(iterations, 0);
		return answer;
	}

}
