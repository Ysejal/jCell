package org.uvsq.fr.simulation;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.HashMap;


public class EpidemicStatistics extends Statistics{
   private XYChart.Series healthy;
   private  XYChart.Series infected;
   private XYChart.Series vaccinated;
   private XYChart.Series dead;



    public EpidemicStatistics(NumberAxis axis, NumberAxis axis1) {
        super(axis, axis1);
        this.setPrefWidth(580);
        this.setPrefHeight(580);
        this.setLayoutX(10);
        this.setLayoutY(10);

        axis.setLabel("Génération");
        axis.setAutoRanging(true);
        axis1.setLabel("Nombre cellules");
        axis1.setAutoRanging(true);

        //healthy-> vert
        //infected->rouge
        //vaccinated->bleu
        //dead->gris

        healthy = new XYChart.Series();
        healthy.setName("En bonne santé");
        infected = new XYChart.Series();
        infected.setName("Infecté");
        vaccinated = new XYChart.Series();
        vaccinated.setName("Vacciné");
        dead = new XYChart.Series();
        dead.setName("Mort");


        getData().add(healthy);
        getData().add(infected);
        getData().add(vaccinated);
        getData().add(dead);
        setTitle("Automate Epidemic statistiques");
        setAnimated(true);
        autosize();
    }

    @Override
   public void setAutomatonData(int generation, HashMap<String,Integer> stat) {
       healthy.getData().add(new XYChart.Data(generation,stat.get("healthy")));
       infected.getData().add(new XYChart.Data(generation,stat.get("infected")));
       vaccinated.getData().add(new XYChart.Data(generation,stat.get("vaccinated")));
       dead.getData().add(new XYChart.Data(generation,stat.get("dead")));
    }


}
