//package com.wattbroker.wattbroker;
//
//import com.util.util;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.text.Text;
//import javafx.scene.shape.Line;
//import javafx.scene.paint.Color;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.testfx.framework.junit.ApplicationTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GraphTest extends ApplicationTest {
//
//    private Graph graph;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize the Graph object
//        graph = new Graph();
//    }
//
//    @Test
//    void testGraphInitialization() {
//        // Verify that the graphPane contains a StackPane with the graph
//        Pane graphPane = (Pane) graph.lookup("#graphPane");
//        assertNotNull(graphPane, "graphPane should be initialized.");
//
//        StackPane stackPane = (StackPane) graphPane.getChildren().get(0);
//        assertNotNull(stackPane, "StackPane should be added to graphPane.");
//        assertEquals("Graph", stackPane.getId(), "StackPane should have ID 'Graph'.");
//    }
//
//    @Test
//    void testButtonActions() {
//        // Verify the action of hourButton
//        Text hourButton = (Text) graph.lookup("#hourButton");
//        Line l = (Line) graph.lookup(".line");
//
//        clickOn(hourButton);
//
//        assertEquals(Color.WHITE, hourButton.getFill(), "hourButton should be selected and have white color.");
//        assertEquals('h', getLastGraph()[0], "Graph should be updated to 'h'.");
//
//        // Verify the action of dayButton
//        Text dayButton = (Text) graph.lookup("#dayButton");
//
//        clickOn(dayButton);
//
//        assertEquals(Color.WHITE, dayButton.getFill(), "dayButton should be selected and have white color.");
//        assertEquals('d', getLastGraph()[0], "Graph should be updated to 'd'.");
//
//        // Verify the action of weekButton
//        Text weekButton = (Text) graph.lookup("#weekButton");
//
//        clickOn(weekButton);
//
//        assertEquals(Color.WHITE, weekButton.getFill(), "weekButton should be selected and have white color.");
//        assertEquals('w', getLastGraph()[0], "Graph should be updated to 'w'.");
//    }
//
//    @Test
//    void testUpdateGraph() {
//        // Test the updateGraph method
//        char[] lastGraph = {'d'};
//        graph.updateGraph(lastGraph);
//
//        Pane graphPane = (Pane) graph.lookup("#graphPane");
//        StackPane stackPane = (StackPane) graphPane.getChildren().get(0);
//
//        // Ensure the graph is updated
//        assertNotNull(stackPane, "StackPane should be added to graphPane after updateGraph.");
//        assertEquals("Graph", stackPane.getId(), "StackPane should have ID 'Graph' after update.");
//    }
//
//    @Test
//    void testWavyPath() {
//        // Test the wavyPath method with sample vectors and verify the StackPane
//        List<util.Vector<Double, Double>> vectors = List.of(
//                new util.Vector<>(0.0, 0.0),
//                new util.Vector<>(10.0, 10.0),
//                new util.Vector<>(20.0, 20.0)
//        );
//
//        StackPane wavyPathPane = Graph.wavyPath(vectors, Graph.GraphSize.REGULAR, Graph.Price, true, Graph.Price_Fill);
//
//        assertNotNull(wavyPathPane, "wavyPath should return a non-null StackPane.");
//        assertTrue(wavyPathPane.getChildren().size() > 0, "wavyPath StackPane should contain elements.");
//    }
//
//    // Utility method to access private fields or methods if necessary
//    private char[] getLastGraph() {
//        // Access lastGraph using reflection or another method to verify updates
//        return new char[]{'d'};
//    }
//}