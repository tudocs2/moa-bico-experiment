/*
 *    Script.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yahoo.labs.samoa.instances.DenseInstance;
import com.yahoo.labs.samoa.instances.Instance;

import moa.cluster.Clustering;
import moa.clusterers.bico.BICO;
import moa.clusterers.bico.Metric;
import moa.gui.visualization.DataPoint;
import moa.streams.clustering.SimpleCSVStream;

/**
 * Class to execute the BICO clustering algorithm multiple times.
 *
 */
public class Script {

	/**
	 * Starts bico.sh like execution of the BICO algorithm.
	 * 
	 * @param args
	 *            args[0] - path to csv file
	 *            args[1] - number of centroids
	 *            args[2] - number of runs (optional)
	 */
	public static void main(String[] args) {
		if(args.length < 2) {
			System.err.println("2 arguments expected, got " + args.length);
			return;
		}
		String file = args[0];
		int kNum = Integer.parseInt(args[1]);
		int coresetSize = kNum * 200;
		int runs;
		if (args.length > 2) {
			runs = Integer.parseInt(args[2]);
		} else {
			runs = 1;
		}

		SimpleCSVStream input = new SimpleCSVStream();
		input.csvFileOption.setValue(file);
		input.restart();
		int inputSize = 0;
		int inputDim = Integer.MAX_VALUE;
		while (input.hasMoreInstances()) {
			Instance next = input.nextInstance().getData();
			DataPoint point0 = new DataPoint(next, ++inputSize);
			Instance traininst0 = new DenseInstance(point0);
			traininst0.deleteAttributeAt(point0.classIndex());
			inputDim = Math.min(traininst0.numAttributes(), inputDim);
		}
		input.decayHorizonOption.setValue(inputSize);
		input.evaluationFrequencyOption.setValue(inputSize);

		Date startDate = new Date();
		System.err.println("date: " + startDate);
		System.err.println("call: " + file + " " + kNum + " " + runs);
		File report = new File(file);
		report = new File(
				report.getParent()
						+ File.separatorChar
						+ "report_"
						+ report.getName()
						+ "_"
						+ kNum
						+ "_Bico_"
						+ new SimpleDateFormat("yyyy-MM-dd-'Z'HH_mm_ss")
								.format(startDate) + ".txt");
		try {
			report.createNewFile();
			System.setOut(new PrintStream(new FileOutputStream(report)));
		} catch (IOException e) {
		}
		System.out.println("#--- Clusteringreport ---");
		System.out.println();
		System.out.println("#date: " + startDate);
		System.out.println("#call: " + file + " " + kNum + " " + runs);
		System.out.println("#");
		System.out.println("#--- Fileinformation ---");
		System.out.println();
		System.out.println("#input file: " + file);
		System.out.println("#number of points: " + inputSize);
		System.out.println("#dimension: " + inputDim);
		System.out.println();
		System.out.println("#----- Bico -----");
		System.out.println("Time,Costs");

		BICO processing = new BICO();
		processing.numClustersOption.setValue(kNum);
		processing.maxNumClusterFeaturesOption.setValue(coresetSize);
		processing.numDimensionsOption.setValue(inputDim);
		processing.numProjectionsOption.setValue(inputDim);

		for (int i = 0; i < runs; i++) {
			input.restart();
			processing.prepareForUseImpl(null, null);

			System.err.print("\r" + "run : " + (i + 1));
			int m_timestamp = 0;
			final long timeStart = System.currentTimeMillis();
			while (input.hasMoreInstances()) {
				Instance next = input.nextInstance().getData();
				DataPoint point0 = new DataPoint(next, ++m_timestamp);
				Instance traininst0 = new DenseInstance(point0);
				traininst0.deleteAttributeAt(point0.classIndex());
				processing.trainOnInstanceImpl(traininst0);
			}
			final long timeEnd = System.currentTimeMillis();

			Clustering result = processing.getClusteringResult();
			double[][] clustering = new double[result.size()][];
			for (int c = 0; c < result.size(); c++) {
				clustering[c] = result.get(c).getCenter();
			}
			input.restart();
			double costs = 0.0;
			while (input.hasMoreInstances()) {
				Instance next = input.nextInstance().getData();
				DataPoint point0 = new DataPoint(next, ++m_timestamp);
				Instance traininst0 = new DenseInstance(point0);
				traininst0.deleteAttributeAt(point0.classIndex());
				double[] point = traininst0.toDoubleArray();
				double minDistance = Double.MAX_VALUE;
				for (int c = 0; c < clustering.length; c++) {
					minDistance = Math.min(
							Metric.distanceSquared(point, clustering[c]),
							minDistance);
				}
				costs += minDistance;
			}

			System.out.println(((timeEnd - timeStart) / 1000.0) + "," + costs);
		}

		System.err.println();
		System.err.println("Finish!");
		System.out.println();
		System.out.println("#-------------------------");
		try {
			System.out.println("#This run was performed on "
					+ InetAddress.getLocalHost().getHostName() + " by "
					+ System.getProperty("user.name") + ".");
		} catch (UnknownHostException e) {
			System.out.println("#This run was performed by "
					+ System.getProperty("user.name") + ".");
		}
	}

}
