package ch.ethz.idsc.owl.math.planar;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.Interpolation;
import ch.ethz.idsc.tensor.opt.LinearInterpolation;

import java.util.Optional;

/* package */ enum InterpolationEntryFinder implements TrajectoryEntryFinder {
  ;

  @Override // from TrajectoryEntryFinder
  public Optional<Tensor> apply(Optional<Tensor> waypoints) {
    return apply(waypoints, RealScalar.ZERO);
  }

  @Override // from TrajectoryEntryFinder
  public Optional<Tensor> apply(Optional<Tensor> waypoints, Scalar index) {
    int index_ = index.number().intValue();
    if (waypoints.isPresent())
      if (index_ >= 0 && index_ < waypoints.get().length()) {
        Interpolation interpolation = LinearInterpolation.of(Tensors.of( //
            waypoints.get().get(index_), //
            waypoints.get().get(index_ + 1)));
        return Optional.of(interpolation.at(RealScalar.of(index.number().doubleValue() - index_)));
      }
    return Optional.empty();
  }
}
