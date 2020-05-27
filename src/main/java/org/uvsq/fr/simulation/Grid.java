package org.uvsq.fr.simulation;

/** In memory representation of a two-dimensional grid of cells */
public class Grid {
  private int numberGeneration;
  private int width;
  private int height;
  private int neighborsIndex[];
  private int probCellAlive;
  private int probCellHealthy;
  private int probCellInfected;
  private int probCellVaccinated;

  private int[] offsetsVertical = {1, 1, 1, 0, -1, -1, -1, 0};
  private int[] offsetsLateral = {-1, 0, 1, 1, 1, 0, -1, -1};

  private Cell[][] cells;


  public Grid(int height, int width) {
    this.width = width;
    this.height = height;
    this.cells = new Cell[height][width];
    this.neighborsIndex = new int[2];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        cells[y][x] = new Cell();
      }
    }
  }

  /** @return int */
  int getHeight() {
    return height;
  }

  /** @return int */
  int getWidth() {
    return width;
  }

  public int getNeighborsIndex(int i) {
    return neighborsIndex[i];
  }

  /**
   * @param y
   * @param x
   * @return Cell
   */
  public Cell getCellAt(int y, int x) {
    return cells[y][x];
  }
  public void setCellAt(int y,int x,Cell val){
    cells[y][x] = val;
  }
  /** Toggles cell's state */
  public void toggleCellAt(int y, int x) {
    this.getCellAt(y, x).toggle();
  }

  /** Activates all cells in the grid */
  void activateAll() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.getCellAt(y, x).setAlive(true);
      }
    }
  }

  /** Kills all cells in the grid */
  public void reset() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.getCellAt(y, x).kill();
      }
    }
  }

  /** Returns number of activated adjacent cells */
  int countAliveNeighborsAt(int y, int x) {
    int count = 0;
    for (int k = 0; k < 8; k++) {
      int nextRow = y + offsetsVertical[k];
      int nextCol = x + offsetsLateral[k];
      if (nextRow >= 0 && nextRow < height && nextCol >= 0 && nextCol < width) {
        Cell adjCell = getCellAt(nextRow, nextCol);
        if (adjCell.isAlive()) count++;
      }
    }
    return count;
  }


  /**
   * @param i
   * @param j
   */
  public int countInfectedNeighborsAt(int i, int j) {
    int countInfected = 0;
   getLeftNeighborIndex(i, j);
    if (getCellAt(getNeighborsIndex(0), getNeighborsIndex(1)).isInfected()) countInfected++;
     /*System.out.println("COMPTEUR VOISIN GAUCHE:"+countInfected);
    System.out.println("________________________________________");*/

    getRightNeighborIndex(i, j);
    if (getCellAt(getNeighborsIndex(0), getNeighborsIndex(1)).isInfected()) countInfected++;
    /*System.out.println("COMPTEUR VOISIN DROITE:"+countInfected);
    System.out.println("________________________________________");*/

    getTopNeighborIndex(i, j);
    if (getCellAt(getNeighborsIndex(0), getNeighborsIndex(1)).isInfected()) countInfected++;
   /* System.out.println("COMPTEUR VOISIN HAUT:"+countInfected);
    System.out.println("________________________________________"); */

    getBottomNeighborIndex(i, j);
    if (getCellAt(getNeighborsIndex(0), getNeighborsIndex(1)).isInfected()) countInfected++;
    /*System.out.println("COMPTEUR VOISIN BAS:"+countInfected);
    System.out.println("________________________________________");*/
   // System.out.println("COMPTEUR INFECTES:"+countInfected);
  return countInfected;
  }

  public void printGrid() {
    char state = ' ';
    System.out.println("###########################################################");
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        cells[i][j].printCellState();
      }
      System.out.print("\n");
    }
    System.out.println("###########################################################");
  }
  /**
   * @param i
   * @param j
   */
  public void getBottomNeighborIndex(int i, int j) {
    int row, col;
    if(i<cells.length-1){
      row = i+1;
      col = j;
    }
    else{
      row = 0;
      col = j;
    }
    /*
    System.out.print("\n");
    System.out.println("########################################");
    System.out.println("I:"+i+" J:"+j);
    System.out.println("Row: "+row+" Col: "+col);
    System.out.println("########################################");*/
    neighborsIndex[0] = row;
    neighborsIndex[1] = col;
  }

  /**
   * @param i
   * @param j
   */
  public void getTopNeighborIndex(int i, int j) {
    int row, col;
     if(i>0){
       row = i-1;
       col = j;
     }
     else {
       row = cells.length-1;
       col = j;
     }    /*
    System.out.print("\n");
    System.out.println("########################################");
    System.out.println("I:"+i+" J:"+j);
    System.out.println("Row: "+row+" Col: "+col);
    System.out.println("########################################");
     */
    neighborsIndex[0] = row;
    neighborsIndex[1] = col;
  }

  /**
   * @param i
   * @param j
   */
  public void getRightNeighborIndex(int i, int j) {
    int row, col;
    if(j<cells.length-1){
      row = i;
      col = j+1;
    }
    else {
      row = i;
      col = 0;
    }
    /*
    System.out.print("\n");
    System.out.println("########################################");
    System.out.println("I:"+i+" J:"+j);
    System.out.println("Row: "+row+" Col: "+col);
    System.out.println("########################################");
   */
    neighborsIndex[0] = row;
    neighborsIndex[1] = col;

  }

  /**
   * @param i
   * @param j
   */
  public void getLeftNeighborIndex(int i, int j) {
    int row, col;
    if (j>0) {
      row = i;
      col = j - 1;
    } else {
      row = i;
      col = cells.length - 1;
    }
    /*
    System.out.print("\n");
    System.out.println("########################################");
    System.out.println("I:"+i+" J:"+j);
    System.out.println("Row: "+row+" Col: "+col);
    System.out.println("########################################"); */
    neighborsIndex[0] = row;
    neighborsIndex[1] = col;
  }

  /**
   * @param y
   * @param x
   */
  // Récupére le voisinage d'une cellule à la position x,y
  public void findWalls(int y, int x) {

    for (int i = 0; i < 8; i++) {

      int nextRow = y + offsetsVertical[i];
      int nextCol = x + offsetsLateral[i];

      if (this.getCellAt(nextCol, nextRow).isAlive()) this.getCellAt(y, x).setNeighbours(i, 1);
      else this.getCellAt(y, x).setNeighbours(i, 0);
    }
  }

  // Effectue la méthode findWalls sur l'ensemble des cellules de la grille
  public void allwalls() {
    for (int y = 1; y < getHeight() - 1; y++) {
      for (int x = 1; x < getWidth() - 1; x++) {
        findWalls(y, x);
      }
    }
  }

  public int getNumberGeneration() {
    return numberGeneration;
  }

  public void setNumberGeneration(int numberGeneration) {
    this.numberGeneration = numberGeneration;
  }

  public int getProbCellAlive() {
    return probCellAlive;
  }

  public void setProbCellAlive(int probCellAlive) {
    this.probCellAlive = probCellAlive;
  }

  public int getProbCellHealthy() {
    return probCellHealthy;
  }

  public void setProbCellHealthy(int probCellHealthy) {
    this.probCellHealthy = probCellHealthy;
  }

  public int getProbCellInfected() {
    return probCellInfected;
  }

  public void setProbCellInfected(int probCellInfected) {
    this.probCellInfected = probCellInfected;
  }

  public int getProbCellVaccinated() {
    return probCellVaccinated;
  }

  public void setProbCellVaccinated(int probCellVaccinated) {
    this.probCellVaccinated = probCellVaccinated;
  }
}
