// code by ob
package ch.ethz.idsc.owl.subdiv.curve;

import java.util.Objects;

import ch.ethz.idsc.owl.math.GeodesicInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** filter blends extrapolated value with measurement */
// TODO check and more tests
// TODO document oscillations for linear case (recommend alpha)
// TODO GeodesicCausal2Filter for 2nd order estimation
public class GeodesicCausal1Filter implements TensorUnaryOperator {
  private static final Scalar TWO = RealScalar.of(2);
  // ---
  private final GeodesicInterface geodesicInterface;
  private final Scalar alpha;
  // ---
  private Tensor p = null;
  private Tensor q = null;

  public GeodesicCausal1Filter(GeodesicInterface geodesicInterface, Scalar alpha) {
    this.geodesicInterface = geodesicInterface;
    this.alpha = alpha;
  }

  public GeodesicCausal1Filter(GeodesicInterface geodesicInterface, Scalar alpha, Tensor p, Tensor q) {
    this(geodesicInterface, alpha);
    this.p = p;
    this.q = q;
  }

  /** @return extrapolated "best guess" value from the previous predictions */
  public synchronized Tensor extrapolate() {
    if (Objects.isNull(p))
      return q;
    return geodesicInterface.split(p, q, TWO);
  }

  @Override
  public synchronized Tensor apply(Tensor tensor) {
    if (Objects.isNull(q)) {
      q = tensor.copy();
      return q.copy();
    }
    Tensor result = geodesicInterface.split(extrapolate(), tensor, alpha);
    p = q.copy();
    q = result.copy();
    return result;
  }
}
