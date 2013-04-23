package com.analysis;

public class CalculateAverage {
	double average;
	int numberCases;

	public CalculateAverage() {
		average = 0;
		numberCases = 0;
	}
	public void newCase(double value){
		average = average + value;
		numberCases = numberCases + 1;
	}
	public double getAverage() {
		return (average / numberCases);
	}
}
