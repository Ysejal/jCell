package org.uvsq.fr.simulation;

import java.util.HashMap;
import java.util.Random;

public class EpidemicAutomaton extends Automaton {
  private final Random random;
  private static final String NAME = "Epidemic";
  private Cell[][] gridCopy;
  private int cellsHealthy;
  private int cellsDead;
  private int cellsInfected;
  private int cellsVaccinated;
  public HashMap<String, Integer> statGeneration;

  public EpidemicAutomaton(Grid grid) {
    super(grid);
    this.random = new Random();
  }

  public Grid getGrid() {
    return grid;
  }

  @Override
  public void setStatistics() {
    statGeneration = new HashMap<String, Integer>();
    statGeneration.put("healthy", cellsHealthy);
    statGeneration.put("infected", cellsInfected);
    statGeneration.put("vaccinated", cellsVaccinated);
    statGeneration.put("dead", cellsDead);
  }

  public void resetStatistics() {
    statGeneration.clear();
  }

  public int getNumGeneration() {
    return grid.getNumberGeneration();
  }

  public void copy() {
    gridCopy = new Cell[grid.getWidth()][grid.getHeight()];
    for (int i = 0; i < grid.getWidth(); i++)
      for (int j = 0; j < grid.getHeight(); j++)
        gridCopy[i][j] = grid.getCellAt(i, j);
  }

  @Override
  void load() {
    grid.setNumberGeneration(0);
    grid.reset();
    for (int y = 0; y < grid.getHeight(); y++)
      for (int x = 0; x < grid.getWidth(); x++) {
        int rand = random.nextInt(1550);
        if (rand < 300)
          if (!random.nextBoolean())
            grid.getCellAt(y, x).setEpidemic("INFECTED");
        if (rand > 350)
          if (!random.nextBoolean())
            grid.getCellAt(y, x).setEpidemic("HEALTHY");
        if (rand < 200)
          if (!random.nextBoolean())
            grid.getCellAt(y, x).setEpidemic("VACCINATED");
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

  public void load2(int probabilityInfected, int probabilityHealthy, int probabilityVaccinated, int width, int height) {
    grid.setNumberGeneration(0);
    grid.reset();
    for (int y = 0; y < height; y++)
      for (int x = 0; x < width; x++) {
        if (conditionProbability(probabilityInfected))
          grid.getCellAt(y, x).setEpidemic("INFECTED");
        if (conditionProbability(probabilityHealthy))
          grid.getCellAt(y, x).setEpidemic("HEALTHY");
        if (conditionProbability(probabilityVaccinated))
          grid.getCellAt(y, x).setEpidemic("VACCINATED");
      }
  }

  @Override
  void printOutInformationSimulation() {
    System.out.print("\n");
    System.out.println(
        "~~~~~~~~~~~~~~~~~~~~ GENERATION NUMERO" + "[" + grid.getNumberGeneration() + "] ~~~~~~~~~~~~~~~~~~~~");
    System.out.println("TYPE SIMULATION: " + NAME);

    cellsHealthy = 0;
    cellsDead = 0;
    cellsInfected = 0;
    cellsVaccinated = 0;
    for (int i = 0; i < grid.getWidth(); i++)
      for (int j = 0; j < grid.getHeight(); j++)
        if (grid.getCellAt(i, j).isHealthy())
          cellsHealthy++;
        else if (grid.getCellAt(i, j).isDead())
          cellsDead++;
        else if (grid.getCellAt(i, j).isInfected())
          cellsInfected++;
        else if (grid.getCellAt(i, j).isVaccinated())
          cellsVaccinated++;
    System.out.println("NOMBRE DE CELLULES SAINES: " + cellsHealthy);
    System.out.println("NOMBRE DE CELLULES MORTES: " + cellsDead);
    System.out.println("NOMBRE DE CELLULES INFECTEES: " + cellsInfected);
    System.out.println("NOMBRE DE CELLULES IMMUNISEES: " + cellsVaccinated);

    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    System.out.print("\n");
  }

  @Override
  void prepareNextGeneration() {

    for (int i = 0; i < grid.getHeight(); i++)
      for (int j = 0; j < grid.getWidth(); j++) {
        copy();
        int count = grid.countInfectedNeighborsAt(i, j);
        Cell cellUpdate = gridCopy[i][j];
        Cell cellCurrent = grid.getCellAt(i, j);
        if (cellCurrent.isHealthy() && count >= 1) {
          cellUpdate.setEpidemic("INFECTED");
          cellCurrent.setUpdateInfected(true);
        } else if (cellCurrent.isInfected()) {
          if (cellCurrent.isUpdateInfected()) {
            cellUpdate.setEpidemic("DEAD");
            cellCurrent.setUpdateInfected(false);
          } else {
            cellUpdate.setUpdateInfected(true);
          }
        }
      }

    // printOutInformationSimulation();
    for (int x = 0; x < grid.getHeight(); x++)
      for (int y = 0; y < grid.getWidth(); y++)
        grid.setCellAt(x, y, gridCopy[x][y]);
  }

  /**
   * Automate Name
   *
   * @return String
   */
  @Override
  public String name() {
    return NAME;
  }
}
