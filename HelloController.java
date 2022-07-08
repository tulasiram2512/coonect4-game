package com.example.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {

    private static final  int columns=7;
    private static final int rows=6;
    private static final int circlediameter=80;
    private static final String discColor1="#24303E";
    private static final String discColor2="#4CAA88";

    private static String playerone="Player one";
    private static String playertwo="Player two";

    private boolean isPlayerOneTurn=true;

    private Disc[][] insertedDiscsArray=new Disc[rows][columns];  //For structrual changes of developers


    @FXML
    public GridPane rootgridpane;
    @FXML
    public Pane inserteddiskpane;
    @FXML
    public Label playernamelabel;

    public void createPlayGround(){
        Shape rectanglewithholes=createGameStructuralGrid();
        rootgridpane.add(rectanglewithholes,0,1);

        List<Rectangle> rectangleList=createclickablecolumns();
        for(Rectangle rectangle:rectangleList){
            rootgridpane.add(rectangle,0,1);
        }


    }

    private Shape createGameStructuralGrid() {
        Shape rectanglewithholes=new Rectangle((columns+1)*circlediameter,(rows+1)*circlediameter);

        for(int row=0;row<rows;row++){
            for(int col=0;col<columns;col++){
                Circle circle=new Circle();
                circle.setRadius(circlediameter/2);
                circle.setCenterX(circlediameter/2);
                circle.setCenterY(circlediameter/2);

                circle.setTranslateX(col*(circlediameter+5)+circlediameter/4);
                circle.setTranslateY(row*(circlediameter+5)+circlediameter/4);

                rectanglewithholes=Shape.subtract(rectanglewithholes,circle);
            }
        }
        rectanglewithholes.setFill(Color.WHITE);
        rootgridpane.add(rectanglewithholes,0,1);
        return rectanglewithholes;

    }

    private List<Rectangle> createclickablecolumns(){
        List<Rectangle> rectangleList=new ArrayList<>();
        for(int col=0;col<columns;col++) {

            Rectangle rectangle = new Rectangle(circlediameter, (rows + 1) * circlediameter);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(circlediameter+5)+circlediameter / 4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event->rectangle.setFill(Color.TRANSPARENT));

            final int column=col;
            rectangle.setOnMouseClicked(event->{
                insertDisc(new Disc(isPlayerOneTurn),column);
            });
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }

    private void insertDisc(Disc disc,int column){

        int row=rows-1;
        while(row>=0){
            if(getDiscIfPresent(row,column)==null)
                break;
            row--;
        }

        if(row<0){    //it is full u cant insert disc
            return;
        }

        insertedDiscsArray[row][column]=disc;      //For structrual changes of developers
        inserteddiskpane.getChildren().add(disc);

        int currentrow=row;
        disc.setTranslateX(column*(circlediameter+5)+circlediameter / 4);
        disc.setTranslateY(5*(circlediameter+5)+circlediameter/4);

        TranslateTransition translatetransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translatetransition.setToY(row*(circlediameter+5)+circlediameter/4);
        translatetransition.setOnFinished(actionEvent -> {

            if(gameEnded(currentrow,column)){
                gameOver();
                return;
            }

            isPlayerOneTurn=!isPlayerOneTurn;
            playernamelabel.setText(isPlayerOneTurn?playerone:playertwo);
        });


        translatetransition.play();
    }

    private Disc getDiscIfPresent(int row,int column){  //to prevent arrey index out of bound exception
        if(row>=rows||row<0||column>=columns||column<0)  {   //if row or column index is out of bound
            return null;}
        return insertedDiscsArray[row][column];
    }

    private void gameOver() {

        String winner=isPlayerOneTurn?playerone:playertwo;
        System.out.println("Winner is"+winner);

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("connect four");
        alert.setHeaderText("The winner is"+winner);
        alert.setContentText("want to play again?");

        ButtonType yesbtn=new ButtonType("yes");
        ButtonType nobtn=new ButtonType("no,exit");
        alert.getButtonTypes().setAll(yesbtn,nobtn);

        Platform.runLater(()->{             //helps to resolve illegal state exception
            Optional<ButtonType> btnClicked=alert.showAndWait();

            if(btnClicked.isPresent()&&btnClicked.get()==yesbtn){
                //reset game;
                resetgame();
            }else{
                //exit game
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void resetgame() {
        inserteddiskpane.getChildren().clear();

        for(int row=0;row< insertedDiscsArray.length;row++){
            for(int column=0;column< insertedDiscsArray[row].length;col++){
                insertedDiscsArray[row][column]=null;
            }
            isPlayerOneTurn=true;
            playernamelabel.setText(playerone);
            createPlayGround();
        }
    }


    private static class Disc extends Circle {
        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove) {
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(circlediameter / 2);
            setFill(isPlayerOneMove ? Color.valueOf(discColor1) : Color.valueOf(discColor2));
            setCenterX(circlediameter / 2);
            setCenterY(circlediameter / 2);
        }
    }
    private boolean gameEnded(int row, int column) {

        List<Point2D> verticalpoints=IntStream.rangeClosed(row-3,row+3).    //0,1,2,3,4,5
                mapToObj(r->new Point2D(r,column)).                 //0,3 1,3 2,3 3,3 4,3 5,3
                collect(Collectors.toList());
        List<Point2D> horizontalpoints=IntStream.rangeClosed(column-3,column+3).
                mapToObj(col->new Point2D(row,col)).
                collect(Collectors.toList());

        Point2D starpoint1=new Point2D(row-3,column+3);
        List<Point2D> diagonal1points=IntStream.rangeClosed(0,6).
                mapToObj(i->starpoint1.add(i,-i).
                collect(Collectors.toList()));

        Point2D starpoint2=new Point2D(row-3,column-3);
        List<Point2D> diagonal2points=IntStream.rangeClosed(0,6).
                mapToObj(i->starpoint2.add(i,i).
                        collect(Collectors.toList()));

        boolean isEnded=checkCombinations(verticalpoints)||checkCombinations(horizontalpoints)||checkCombinations(diagonal1points)||checkCombinations(diagonal2points);

        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain=0;

        for(Point2D point:points){

            int rowIndexForArreay=(int) point.getX();
            int columnIndexForArray=(int) point.getY();

            Disc disc=getDiscIfPresent(rowIndexForArreay,columnIndexForArray);
            if(disc!=null && disc.isPlayerOneMove==isPlayerOneTurn){

                chain++;
                if(chain==4){
                    return true;
                }else{
                    chain=0;
                }
            }

        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
