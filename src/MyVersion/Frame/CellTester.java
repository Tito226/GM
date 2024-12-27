package MyVersion.Frame;

import java.util.ArrayList;
import java.util.Random;

import MyVersion.Cells.NormCell;
import MyVersion.Core.Data_Set;

public class CellTester {
	static void cellDiagnostic(NormCell norm, ArrayList<Double[]> inputData) {
		System.out.println("Run Cell diag.");
		System.out.print(":::: "+norm.calculateOutput(new Double[] { 0d, 15d, 6d, 0d, 0d, 0d, 0d })+"тест 1     ");
		System.out.print(":::: "+ // тесты 1 должны быть одинаковыми
				norm.calculateOutput(new Double[] { 0d, 15d, 6d, 0d, 0d, 0d, 0d, 23456d })+"тест 1     ");
		Random r=new Random();
		System.out.print(norm
				.calculateOutput(new Double[] { 0d, (double) Data_Set.rnd(1,4), (double) Data_Set.rnd(7,100),
						(double) r.nextInt(2), (double) r.nextInt(2), (double) r.nextInt(2), (double) r.nextInt(2) })
				+"тест 2               ");
		if (inputData==null)
			return;
		for (int i=0; i<inputData.size(); i++) {
			if (i%2==1)
				continue;
			for (int j=0; j<7; j++) {
				switch (j) {
				case 0:
					System.out.print("multiplyReady:");
					break;

				case 1:
					System.out.print("energy:");
					break;

				case 2:
					System.out.print("organic:");
					break;

				case 3:
					System.out.print("upCell:");
					break;
				case 4:
					System.out.print("downCell:");
					break;

				case 5:
					System.out.print("leftCell:");
					break;
				case 6:
					System.out.print("rightCell:");
					break;
				}
				Double[] buf=inputData.get(i);
				System.out.print(buf[j]+" ");
			}
			System.out.println(":::: "+norm.calculateOutput(inputData.get(i)));
			System.out.print(" ");
			System.out
					.print(norm.calculateOutput(new Double[] { 0d, (double) Data_Set.rnd(3,15), 6d, 0d, 0d, 0d, 0d }));
			System.out.print(" ");
		}
	}
}
