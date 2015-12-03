# moa-bico-experiment

This is an extension for the [MOA framework](http://moa.cms.waikato.ac.nz) which provides an experiment environment for the [BICO algorithm](http://dx.doi.org/10.1007/978-3-642-40450-4_41). In order to use this extension, you must add the `moa-bico-experiment.jar` to the classpath when launching MOA.

**Example:**
```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.gui.GUI
```

## Experiment with the MOA framework

For a clustering experiment with the MOA framework you should use the following settings.

**Stream**: `class moa.streams.clustering.SimpleCSVStream`

  - `csvFile`: path to input file
  - `splitChar`: input CSV split character (optional, typical `,`)
  - `classIndex`: `true` if the last component of an input point is a class label (optional, typical `false`)
  - `decayHorizon`: number of input points
  - `decayThreshold`: (is not needed)
  - `evaluationFrequency`: number of input points

**Algorithm**: `class moa.clusterers.bico.BICO`

  - `Cluster`: number of desired centers
  - `Dimensions`: dimension of an input point
  - `MaxClusterFeatures`: coreset size (typical `Cluster * 200`)
  - `Projections`: number of random projections used for nearest neighbor search in first level (typical `Dimensions`)
  - `evaluateMicroClustering`: `true` if the coreset should be the result (optional, typical `false`)

### Run the MOA framework from the command line

```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.DoTask EvaluateClustering -s \(moa.streams.clustering.SimpleCSVStream -f csvFile -s splitChar -c classIndex -h decayHorizon -e evaluationFrequency\) -l \(moa.clusterers.bico.BICO -k Cluster -d Dimensions -n MaxClusterFeatures -p Projections -M evaluateMicroClustering\) -i instanceLimit -d dumpFile
```

#### Arguments

  - `csvFile`: path to input file
  - `splitChar`: input CSV split character (optional, typical `,`)
  - `classIndex`: `true` if the last component of an input point is a class label (optional, typical `false`)
  - `decayHorizon`: number of input points
  - `evaluationFrequency`: number of input points
  - `Cluster`: number of desired centers
  - `Dimensions`: dimension of an input point
  - `MaxClusterFeatures`: coreset size (typical `Cluster * 200`)
  - `Projections`: number of random projections used for nearest neighbor search in first level (typical `Dimensions`)
  - `evaluateMicroClustering`: `true` if the coreset should be the result (optional, typical `false`)
  - `instanceLimit`: number of input points
  - `dumpFile`: path to summary file (comma-separated)

## Stand-alone experiment

This extension also provides three different classes to launch the BICO algorithm without the MOA framework.

### "Experiments"

```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.clusterers.bico.experiment.Experiments input n k d space output projections [seed]
```

#### Arguments

  - `input`: path to input file (space-separated)
  - `n`: number of input points
  - `k`: number of desired centers
  - `d`: dimension of an input point
  - `space`: coreset size (typical `k * 200`)
  - `output`: path to output file (space-separated)
  - `projections`: number of random projections used for nearest neighbor search in first level (typical `d`)
  - `seed`: random seed (optional)

### "Quickstart"

```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.clusterers.bico.experiment.Quickstart input n k d space output projections [splitchar [seed]]
```

#### Arguments

  - `input`: path to input file
  - `n`: number of input points
  - `k`: number of desired centers
  - `d`: dimension of an input point
  - `space`: coreset size (typical `k * 200`)
  - `output`: path to output file (space-separated)
  - `projections`: number of random projections used for nearest neighbor search in first level (typical `d`)
  - `splitchar`: input CSV split character (optional, typical `,`)
  - `seed`: random seed (optional)

### "Script"

```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.clusterers.bico.experiment.Script input k [r]
```

With this class the BICO algorithm evaluates all points of the input file, uses `k * 200` as coreset size and the dimension of the input points as number of random projections used for nearest neighbor search in first level.

#### Arguments

  - `input`: path to input file (comma-separated)
  - `k`: number of desired centers
  - `r`: number of runs (optional, typical `1`)

## Reference

> Hendrik Fichtenberger, Marc Gillé, Melanie Schmidt, Chris Schwiegelshohn, Christian Sohler:
> BICO: BIRCH Meets Coresets for k-Means Clustering.
> ESA 2013: 481-492 (2013)
> http://ls2-www.cs.tu-dortmund.de/bico/

## License

See [License](LICENSE.txt)

