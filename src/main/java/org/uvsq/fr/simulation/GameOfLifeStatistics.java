package org.uvsq.fr.simulation;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.HashMap;

public class GameOfLifeStatistics extends Statistics{
    private XYChart.Series alive;
    private XYChart.Series dead;


    public GameOfLifeStatistics(NumberAxis axis, NumberAxis axis1) {
        super(axis, axis1);
        this.setPrefWidth(580);
        this.setPrefHeight(580);
        this.setLayoutX(10);
        this.setLayoutY(10);

       axis.setLabel("Génération");
        axis.setAutoRanging(true);
        axis1.setLabel("Nombre cellules");
        axis1.setAutoRanging(true);



        alive = new XYChart.Series();
        alive.setName("Vivant");
        dead = new XYChart.Series();
        dead.setName("Mort");



        getData().add(alive);
        getData().add(dead);
        setTitle("Automate Game of Life statistiques");
        setAnimated(true);
        autosize();
    }

    @Override
    public void setAutomatonData(int generation, HashMap<String,Integer> stat) {
        alive.getData().add(new XYChart.Data(generation,stat.get("alive")));
        dead.getData().add(new XYChart.Data(generation,stat.get("dead")));

    }

}
