package com.cmdpresta.cookmaster.cookmasterapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuView {

    private Stage primaryStage;
    private VBox menuLayout;
    private Scene menuScene;
    private String authToken;
    private EventsView eventsView;
    private Stage menuStage;

    private void generatePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));


   /*     // Retrieve events from the API
        Events events = Events.retrieveEvents(authToken);

        // Check if events were successfully retrieved
        if (events != null) {
            // Iterate over the events and display their details
            for (Event event : events.getEvents()) {
                System.out.println("Event ID: " + event.getId());
                System.out.println("Name: " + event.getName());
                System.out.println("Status: " + event.getStatus());
                // ... display other event details
                System.out.println();
            }
        } else {
            System.out.println("Failed to retrieve events.");
        }*/

        File file = fileChooser.showSaveDialog(menuStage);
        if (file != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Retrieve events from the API
                Events events = Events.retrieveEvents(authToken);

                List<Event> topEvents = events.getTop5Events();

                if (events == null) {
                    System.out.println("Failed to retrieve events.");
                }

                // Generate the first pie chart (Frequency of Planification)
                JFreeChart chart1 = createPlanificationFrequencyChart(events);
                File chartFile1 = File.createTempFile("chart1", ".png");
                ChartUtils.saveChartAsPNG(chartFile1, chart1, 500, 300);
                contentStream.drawImage(PDImageXObject.createFromFile(chartFile1.getAbsolutePath(), document), 5, 500, 300, 200);
                chartFile1.delete();

                // Generate the second pie chart (Repartition by Event Type)
                JFreeChart chart2 = createEventTypeRepartitionChart(events);
                File chartFile2 = File.createTempFile("chart2", ".png");
                ChartUtils.saveChartAsPNG(chartFile2, chart2, 500, 300);
                contentStream.drawImage(PDImageXObject.createFromFile(chartFile2.getAbsolutePath(), document), 305, 500, 300, 200);
                chartFile2.delete();

                // Generate the third curve diagram (Top 5 Most Wanted Events)
                JFreeChart chart3 = createMostWantedEventsChart(topEvents);
                File chartFile3 = File.createTempFile("chart3", ".png");
                ChartUtils.saveChartAsPNG(chartFile3, chart3, 500, 300);
                contentStream.drawImage(PDImageXObject.createFromFile(chartFile3.getAbsolutePath(), document), 5, 250, 300, 200);
                chartFile3.delete();

                // Generate the fourth curve diagram (Placeholder)
                // Modify this part to create your desired diagram based on the events data
                JFreeChart chart4 = createPlaceholderChart();
                File chartFile4 = File.createTempFile("chart4", ".png");
                ChartUtils.saveChartAsPNG(chartFile4, chart4, 500, 300);
                contentStream.drawImage(PDImageXObject.createFromFile(chartFile4.getAbsolutePath(), document), 305, 250, 300, 200);
                chartFile4.delete();

                contentStream.close();

                // Save the PDF file
                document.save(file);
                document.close();

                System.out.println("PDF generated successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private JFreeChart createPlanificationFrequencyChart(Events events) {

        DefaultPieDataset dataset = new DefaultPieDataset();

        int low = events.getNumEventsWithCompletionRateLessThan(30);
        int high = events.getNumEventsWithCompletionRateGreaterThan(70);

        dataset.setValue("LESS THAN 30%", low);
        dataset.setValue("BEETWEN 30% AND 70", high - low);
        dataset.setValue("MORE THAN 70%", high);

        JFreeChart chart = ChartFactory.createPieChart("Percentage of events completion", dataset);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        return chart;
    }

    private JFreeChart createEventTypeRepartitionChart(Events events) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue("Tasting", events.getNumTastings());
        dataset.setValue("Meeting", events.getNumMeetings());
        dataset.setValue("Other", events.getNumOther());

        JFreeChart chart = ChartFactory.createPieChart("Event Type Repartition", dataset);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        return chart;
    }

    private JFreeChart createMostWantedEventsChart(List<Event> topEvents) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Event topEvent : topEvents) {
            dataset.addValue(topEvent.getCurrentParticipants(), topEvent.getEventType(), topEvent.getName());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Most Wanted Events", // Chart title
                "Event Name", // X-axis label
                "Number of Participants", // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        return chart;
    }

    private JFreeChart createPlaceholderChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10, "Series 1", "Category 1");
        dataset.addValue(20, "Series 2", "Category 2");
        dataset.addValue(30, "Series 3", "Category 3");

        JFreeChart chart = ChartFactory.createBarChart(
                "Placeholder Chart",
                "Category",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);

        return chart;
    }


    public MenuView(Stage primaryStage, String authToken) {
        this.primaryStage = primaryStage;
        this.authToken = authToken;
        createMenuLayout();
    }

    public void show() {
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }

    private void createMenuLayout() {
        // Create UI components for menu
        Button clientAccountButton = new Button("See Client Account");
        Button eventsButton = new Button("See Events");
        Button prestationsButton = new Button("See Prestations");
        Button generatePdfButton = new Button("Generate PDF");

        // Set up the layout for menu
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(clientAccountButton, eventsButton, prestationsButton, generatePdfButton);

        // Set up the button actions
        clientAccountButton.setOnAction(e -> {
            // Add the logic to open the client account view using the authToken
            System.out.println("Opening Client Account view with auth token: " + authToken);
        });

        eventsButton.setOnAction(e -> {
            // Add the logic to open the events view using the authToken
            System.out.println("Opening Events view with auth token: " + authToken);
            openEventView(authToken);
        });

        prestationsButton.setOnAction(e -> {
            // Add the logic to open the prestations view using the authToken
            System.out.println("Opening Prestations view with auth token: " + authToken);
        });

        generatePdfButton.setOnAction(e -> {
            // Add the logic to generate the PDF using the authToken
            System.out.println("Generating PDF with auth token: " + authToken);
            generatePdf();
        });

        // Create the scene for menu layout
        menuLayout = vbox;
        menuScene = new Scene(menuLayout);
    }

    private void openEventView(String token) {
        eventsView = new EventsView(primaryStage, token);
        eventsView.show();
    }
}
