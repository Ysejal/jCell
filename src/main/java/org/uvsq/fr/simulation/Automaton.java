package org.uvsq.fr.simulation;

/** Base cellular automaton with common methods and utility functions */
abstract class Automaton implements Generation {

  Grid grid;

  Automaton(Grid grid) {
    this.grid = grid;
  }

  /**
   * Prepare next Automaton according to automaton rules
   *
   * @param
   */
  abstract void prepareNextGeneration();

  /** @param */
  abstract void load();

  /** @param */
  abstract void printOutInformationSimulation();

  /** Advances automaton to next Automaton */
  private void commit() {
    int gen = grid.getNumberGeneration() + 1;
    grid.setNumberGeneration(gen);
    for (int y = 0; y < grid.getHeight(); y++) {
      for (int x = 0; x < grid.getWidth(); x++) {
        grid.getCellAt(y, x).update();
      }
    }
  }

  abstract void setStatistics();

  @Override
  public void newLoad() {
    load();
  }

  @Override
  public void nextStep() {
    printOutInformationSimulation();
    prepareNextGeneration();
    setStatistics();
    commit();
  }
}
