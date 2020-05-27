package org.uvsq.fr.simulation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameOfLifeAutomatonTest {

  private Grid grid;

  @BeforeEach
  public void setup() {
    grid = new Grid(3, 3);
  }

  @Test
  public void testLivesOn2() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(0, 0);
    grid.toggleCellAt(2, 2);
    grid.toggleCellAt(1, 1);

    LifeAutomaton.nextStep();

    assertTrue(grid.getCellAt(1, 1).isAlive());
  }

  @Test
  public void testLivesOn3() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(0, 0);
    grid.toggleCellAt(1, 0);
    grid.toggleCellAt(2, 1);
    grid.toggleCellAt(1, 1);

    LifeAutomaton.nextStep();

    assertTrue(grid.getCellAt(1, 1).isAlive());
  }

  @Test
  public void testUnderpopulation() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(0, 0);
    grid.toggleCellAt(1, 1);

    LifeAutomaton.nextStep();

    assertFalse(grid.getCellAt(1, 1).isAlive());
  }

  @Test
  public void testUnderpopulationEmpty() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(1, 1);

    LifeAutomaton.nextStep();

    assertFalse(grid.getCellAt(1, 1).isAlive());
  }

  @Test
  public void testOverpopulation() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(0, 0);
    grid.toggleCellAt(0, 1);
    grid.toggleCellAt(2, 1);
    grid.toggleCellAt(2, 2);
    grid.toggleCellAt(1, 1);

    LifeAutomaton.nextStep();

    assertFalse(grid.getCellAt(1, 1).isAlive());
  }

  @Test
  public void testReproduction() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(0, 0);
    grid.toggleCellAt(1, 0);
    grid.toggleCellAt(2, 2);

    LifeAutomaton.nextStep();

    assertTrue(grid.getCellAt(1, 1).isAlive());
  }

  @Test
  public void testStale() {
    Generation LifeAutomaton = new GameOfLifeAutomaton(grid);

    grid.toggleCellAt(1, 0);
    grid.toggleCellAt(2, 2);

    LifeAutomaton.nextStep();

    assertFalse(grid.getCellAt(1, 1).isAlive());
  }
}
