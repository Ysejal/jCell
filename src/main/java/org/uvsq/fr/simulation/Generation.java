package org.uvsq.fr.simulation;

public interface Generation {

  String name();

  void nextStep();

  void newLoad();
}
