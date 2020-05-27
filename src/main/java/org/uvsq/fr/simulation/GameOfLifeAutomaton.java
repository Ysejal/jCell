package org.uvsq.fr.simulation;

import java.util.HashMap;
import java.util.Random;

public class GameOfLifeAutomaton extends Automaton {
  private final Random random;
  private static final String NAME = "Game of Life";
  private int cellsAlive;
  private int cellsDead;
  public HashMap<String, Integer> statGeneration;

  public GameOfLifeAutomaton(Grid grid) {
    super(grid);
    this.random = new Random();
  }

  public Grid getGrid() {
    return grid;
  }

  public int getNumGeneration() {
    return grid.getNumberGeneration();
  }

  /** Randomly toggles cells in the grid */
  @Override
  void load() {
    grid.setNumberGeneration(0);
    grid.reset();
    for (int y = 0; y < grid.getHeight(); y++) {
      for (int x = 0; x < grid.getWidth(); x++) {
        if (random.nextBoolean()) {
          grid.toggleCellAt(y, x);
        }
      }
    }
  }

  private boolean conditionProbability(int probability) {
    int rand;
    if (probability == 5) {
      rand = random.nextInt(100);
      if (rand <= 5) {
        return true;
      }
      return false;
    } else if (probability == 10) {
      rand = random.nextInt(100);
      if (rand <= 10) {
        return true;
      }
      return false;
    } else if (probability == 15) {
      rand = random.nextInt(100);
      if (rand <= 15) {
        return true;
      }
      return false;
    } else if (probability == 20) {
      rand = random.nextInt(100);
      if (rand <= 20) {
        return true;
      }
      return false;
    } else if (probability == 25) {
      rand = random.nextInt(100);
      if (rand <= 25) {
        return true;
      }
      return false;
    } else if (probability == 30) {
      rand = random.nextInt(100);
      if (rand <= 30) {
        return true;
      }
      return false;
    } else if (probability == 40) {
      rand = random.nextInt(100);
      if (rand <= 40) {
        return true;
      }
      return false;
    } else if (probability == 50) {
      rand = random.nextInt(100);
      if (rand <= 50) {
        return true;
      }
      return false;
    } else if (probability == 75) {
      rand = random.nextInt(100);
      if (rand <= 75) {
        return true;
      }
      return false;
    } else if (probability == 80) {
      rand = random.nextInt(100);
      if (rand <= 80) {
        return true;
      }
      return false;
    } else if (probability == 85) {
      rand = random.nextInt(100);
      if (rand <= 85) {
        return true;
      }
      return false;
    } else if (probability == 90) {
      rand = random.nextInt(100);
      if (rand <= 90) {
        return true;
      }
      return false;
    }
    return true;
  }

  public void load2(int probabilityAlive, int width, int height) {
    grid.setNumberGeneration(0);
    grid.reset();
    int prob = random.nextInt(100);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (conditionProbability(probabilityAlive))
          grid.toggleCellAt(y, x);
      }
    }
  }

  @Override
  void printOutInformationSimulation() {
    System.out.print("\n");
    System.out.println(
        "~~~~~~~~~~~~~~~~~~~~ GENERATION NUMERO" + "[" + grid.getNumberGeneration() + "] ~~~~~~~~~~~~~~~~~~~~");
    System.out.println("TYPE SIMULATION: " + NAME);

    cellsAlive = 0;
    cellsDead = 0;
    for (int i = 0; i < grid.getWidth(); i++) {
      for (int j = 0; j < grid.getHeight(); j++) {

        if (grid.getCellAt(i, j).isAlive())
          cellsAlive++;
        else
          cellsDead++;

      }
    }

    System.out.println("NOMBRE DE CELLULES EN VIE: " + cellsAlive);
    System.out.println("NOMBRE DE CELLULES MORTES: " + cellsDead);

    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    System.out.print("\n");
  }

  @Override
  void setStatistics() {
    statGeneration = new HashMap<String, Integer>();
    statGeneration.put("alive", cellsAlive);
    statGeneration.put("dead", cellsDead);
  }

  public void resetStatistics() {
    statGeneration.clear();
  }

  /** Game of Life algorimth */
  @Override
  void prepareNextGeneration() {
    for (int y = 0; y < grid.getHeight(); y++) {
      for (int x = 0; x < grid.getWidth(); x++) {
        int aliveCount = grid.countAliveNeighborsAt(y, x);
        final Cell cell = grid.getCellAt(y, x);
        if (cell.isAlive()) {
          if (aliveCount == 2 || aliveCount == 3) {
            cell.setSurvivesNextGen(true);
          } else {
            cell.setSurvivesNextGen(false);
          }
        } else if (!cell.isAlive() && aliveCount == 3) {
          cell.setSurvivesNextGen(true);
        }
      }
    } // printOutInformationSimulation();

  }

  /** @return String */
  @Override
  public String name() {
    return NAME;
  }
}
