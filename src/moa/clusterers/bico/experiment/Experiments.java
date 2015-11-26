/*
 *    Experiments.java
 *    Copyright (C) 2015 TU Dortmund University, Germany
 *    @author Jan Stallmann (jan.stallmann@tu-dortmund.de)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *    
 */
package moa.clusterers.bico.experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.yahoo.labs.samoa.instances.DenseInstance;
import com.yahoo.labs.samoa.instances.Instance;

import moa.clusterers.bico.BICO;
import moa.gui.visualization.DataPoint;
import moa.streams.clustering.SimpleCSVStream;

/**
 * Class to execute the BICO clustering algorithm.
 *
 */
public class Experiments {

	/**
	 * Starts execution of the BICO algorithm.
	 * 
	 * @param args
	 *            args[0] - path to wsv.b file
	 *            args[1] - number of input points
	 *            args[2] - number of desired centers
	 *            args[3] - dimension of an input point
	 *            args[4] - coreset size
	 *            args[5] - path to coreset file
	 *            args[6] - number of random projections used for nearest 
	 *                      neighbour search in first level
	 *            args[7] - seed (optional)
	 * 
	 */
	public static void main(String[] args) {
		if(args.length < 7) {
			System.err.println("7 arguments expected, got " + args.length);
			return;
		}
		String inputPath = args[0];
		int n = Integer.parseInt(args[1]);
		int k = Integer.parseInt(args[2]);
		int d = Integer.parseInt(args[3]);
		int space = Integer.parseInt(args[4]);
		String outputPath = args[5];
		int p = Integer.parseInt(args[6]);
		int seed = 0;
		if (args.length > 7) {
			seed = Integer.parseInt(args[7]);
		}

		SimpleCSVStream input = new SimpleCSVStream();
		input.csvFileOption.setValue(inputPath);
		input.splitCharOption.setValue(" ");
		input.decayHorizonOption.setValue(n);
		input.evaluationFrequencyOption.setValue(n);
		input.restart();

		BICO processing = new BICO();
		processing.numClustersOption.setValue(k);
		processing.maxNumClusterFeaturesOption.setValue(space);
		processing.numDimensionsOption.setValue(d);
		processing.numProjectionsOption.setValue(p);
		if (seed != 0) {
			processing.setRandomSeed(seed);
		}
		processing.prepareForUseImpl(null, null);

		int m_timestamp = 0;
		while (input.hasMoreInstances()) {
			Instance next = input.nextInstance().getData();
			DataPoint point0 = new DataPoint(next, ++m_timestamp);
			Instance traininst0 = new DenseInstance(point0);
			traininst0.deleteAttributeAt(point0.classIndex());
			processing.trainOnInstanceImpl(traininst0);
			if (traininst0.numAttributes() == d) {
				processing.trainOnInstanceImpl(traininst0);
			} else {
				System.err.println("Line skipped because line dimension is "
						+ traininst0.numAttributes() + " instead of " + d);
			}
		}

		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputPath));
			fileWriter.write(String.valueOf(processing.getMicroClusteringSize()));
			fileWriter.write('\n');
			processing.printMicroClusteringResult(fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException ioe) {
			throw new RuntimeException("Write next instance failed.", ioe);
		}
	}

}
