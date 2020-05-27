package org.uvsq.fr.simulation;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import java.util.HashMap;

public abstract class Statistics extends LineChart {
  private NumberAxis x;
  private NumberAxis y;

  public Statistics(NumberAxis axis, NumberAxis axis1) {
    super(axis, axis1);
  }

  abstract void setAutomatonData(int generation, HashMap<String, Integer> stat);
}
