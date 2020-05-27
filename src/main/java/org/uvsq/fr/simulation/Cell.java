package org.uvsq.fr.simulation;

import java.util.ArrayList;
import org.uvsq.fr.simulation.util.*;


public class Cell {
  private String name ="";
  private boolean alive = false;
  private boolean infected = false;
  private boolean healthy = false;
  private boolean vaccinated = false;
  private boolean dead = false;
  private boolean wall = false;
  private boolean updateInfected;
  private boolean survivesNextGen;

  private ArrayList<CellUpdate> listeners = new ArrayList<>();

  private int[] neighbours = {-1, -1, -1, -1, -1, -1, -1, -1};

  /** @param alive */
  void setAlive(boolean alive) {
    this.alive = alive;
    notifyListeners();
  }

  /** @param healthy */
  void setHealthy(boolean healthy) {
    this.healthy = healthy;
    notifyListeners();
  }

  /** @param infected */
  void setInfected(boolean infected) {
    this.infected = infected;
    notifyListeners();
  }

  /** @param dead */
  void setDead(boolean dead) {
    this.dead = dead;
    notifyListeners();
  }

  /** @param vaccinated */
  void setVaccinated(boolean vaccinated) {
    this.vaccinated = vaccinated;
    notifyListeners();
  }

  /** @param wall */
  void setWall(boolean wall) {
    this.wall = wall;
    notifyListeners();
  }

  /**
   * @param i
   * @param wall
   */
  // Met l'état wall à la case i du tableau des voisins
  public void setNeighbours(int i, int wall) {

    this.neighbours[i] = wall;
  }

  /**
   * Update Epidemic State
   *
   * @param state
   */
  void setEpidemic(String state) {
    state = state.toLowerCase();
    switch (state) {
      case "infected":
        setInfected(true);
        setHealthy(false);
        setDead(false);
        setVaccinated(false);
        break;
      case "healthy":
        setInfected(false);
        setHealthy(true);
        setDead(false);
        setVaccinated(false);
        break;
      case "dead":
        setInfected(false);
        setHealthy(false);
        setDead(true);
        setVaccinated(false);

        break;
      case "vaccinated":
        setInfected(false);
        setHealthy(false);
        setDead(false);
        setVaccinated(true);

        break;
      case "kill":
        setInfected(false);
        setHealthy(false);
        setDead(false);
        setVaccinated(false);
        break;
    }
  }

  /** @return boolean */
  public boolean isAlive() {
    return alive;
  }

  /** @return boolean */
  public boolean isInfected() {
    return infected;
  }

  /** @return boolean */
  public boolean isHealthy() {
    return healthy;
  }

  /** @return boolean */
  public boolean isDead() {
    return dead;
  }

  /** @return boolean */
  public boolean isVaccinated() {
    return vaccinated;
  }

  /** @return boolean */
  public boolean isWall() {
    return wall;
  }

  /**
   * @param i
   * @return int
   */
  public int getItemNeighbours(int i) {
    return neighbours[i];
  }

  /** @param survives */
  void setSurvivesNextGen(boolean survives) {
    this.survivesNextGen = survives;
  }

  /** @return boolean */
  public boolean survivesNextGen() {
    return this.survivesNextGen;
  }

  void kill() {
    setSurvivesNextGen(false);
    setAlive(false);
    setVaccinated(false);
    setDead(false);
    setHealthy(false);
    setInfected(false);
    setWall(false);
  }

  public void toggle() {
    setAlive(!isAlive());
  }


  void update() {
      setAlive(survivesNextGen());
  }

  /** @param listener */
  public void attachListener(CellUpdate listener) {
    this.listeners.add(listener);
  }

  private void notifyListeners() {
    for (CellUpdate listener : listeners) {
      listener.onUpdate(this);
    }
  }

  void printCellState() {
    char c = ' ';
    if (this.infected == true) c = 'I';
    else if (this.healthy == true) c = 'H';
    else if(this.vaccinated == true) c = 'V';
    else if(this.dead == true) c ='D';
    else  c ='E';

    System.out.print("|"+c+"| ");
  }

  public String getState() {
    return name;
  }


  public boolean isUpdateInfected() {
    return updateInfected;
  }

  public void setUpdateInfected(boolean updateInfected) {
    this.updateInfected = updateInfected;
  }
}
