package MyVersion.Cells;

import java.util.Random;

public class Genome {
	static Random r=new Random();

	/*
	 * TODO зделать ети переменные изменяемыми в ходе еволюции,константы временный
	 * ход ,не перемещать в конфиг
	 */
	public static final int unusedGenes=10;// this genes codes nothing(for exemple if unusedGenes==0 all cells will multiply in all directions)

	public static final int chromosomesNum=32;
	private Сhromosome[] chromosomes=new Сhromosome[chromosomesNum];

	/** FOR TEST AND FIRST INITALIZATION, GENERATES RANDOM GENOME */
	public Genome() {
		for (int i=0; i<chromosomes.length; i++) {
			chromosomes[i]=new Сhromosome();
			for (int j=0; j<4; j++) {// 4 is referenses to anouther chromosomes
				chromosomes[i].genes[j]=(byte) r.nextInt(chromosomesNum);
			}
			chromosomes[i].genes[4]=(byte) r.nextInt(3);// random Cell type
		}
	}
	public Genome(Genome genome) {
		for (int i=0; i<chromosomes.length; i++) {
			chromosomes[i]=new Сhromosome();
			for (int j=0; j<chromosomes[i].genes.length; j++) {// 4 is referenses to anouther chromosomes
				chromosomes[i].genes[j]=genome.chromosomes[i].genes[j];
				
				//TODO MUTATION STUB
				if(r.nextInt(10)==1) {
					chromosomes[i].genes[j]=(byte) r.nextInt(chromosomesNum);
				}
				
			}
		}
	}
	class Сhromosome {
		// up,down,right,left,cell type
		byte[] genes=new byte[5]; // 0-3 is referenses to anouther chromosomes, 4 codes type of cell
	}

	public byte[] getChromosomeGenes(int chromosomeIndex) {
		return chromosomes[chromosomeIndex].genes;
	}

	static LiveCellType getCellType(byte cellType) {
		if (cellType==0)
			return LiveCellType.NormCell;
		else if (cellType==1)
			return LiveCellType.Protoplast;
		else
			return LiveCellType.RootCell;
	}

	byte[] getChromosome(byte chromosomeNum) {
		//System.out.println(chromosomeNum);
		if (chromosomeNum>=0 && chromosomeNum<chromosomesNum) {
			return chromosomes[chromosomeNum].genes;
		} else {
			return null;
		}
	}

}
