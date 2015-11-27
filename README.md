# moa-bico-experiment

This is an extension for the [MOA framework](http://moa.cms.waikato.ac.nz) which provides an experiment environment for the [BICO algorithm](http://dx.doi.org/10.1007/978-3-642-40450-4_41). In order to use this extension, you must add the `moa-bico-experiment.jar` to the classpath when launching MOA.

**Example:**
```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.gui.GUI
```

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
  - `space`: coreset size
  - `output`: path to output file (space-separated)
  - `projections`: number of random projections used for nearest neighbor
 search in first level
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
  - `space`: coreset size
  - `output`: path to output file (space-separated)
  - `splitchar`: input CSV split character
  - `seed`: random seed (optional)

### "Script"

```sh
$ java -cp moa-bico-experiment.jar:moa.jar -javaagent:sizeofag.jar moa.clusterers.bico.experiment.Script input k [r]
```
#### Arguments

  - `input`: path to input file (comma-separated)
  - `r`: number of runs (optional)

## Reference

> Hendrik Fichtenberger, Marc Gillé, Melanie Schmidt, Chris Schwiegelshohn, Christian Sohler:
> BICO: BIRCH Meets Coresets for k-Means Clustering.
> ESA 2013: 481-492 (2013)
> http://ls2-www.cs.tu-dortmund.de/bico/

## License

See [License](LICENSE.txt)

