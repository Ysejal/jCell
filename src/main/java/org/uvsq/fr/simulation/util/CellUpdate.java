package org.uvsq.fr.simulation.util;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.uvsq.fr.simulation.Cell;

/** A bridge between the cell state and displayed rectangle state. */
public class CellUpdate {

  private final Rectangle rectangle;

  public CellUpdate(final Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  /** Updates rectangle on cell state change. */
  public void onUpdate(final Cell cell) {
    if (cell.isAlive())
      this.rectangle.setFill(Color.BLACK);
    else if (cell.isInfected())
      this.rectangle.setFill(Color.RED);
    else if (cell.isHealthy())
      this.rectangle.setFill(Color.GREEN);
    else if (cell.isDead())
      this.rectangle.setFill(Color.GREY);
    else if (cell.isVaccinated())
      this.rectangle.setFill(Color.BLUE);
    else if (cell.isWall())
      this.rectangle.setFill(Color.BROWN);
    else
      this.rectangle.setFill(Color.WHITE);
  }
}
