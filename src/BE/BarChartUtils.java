package BE;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author DennisPC-bit
 *
 */

public class BarChartUtils {
    private final List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

    public BarChart<String, Number> getTotalIndividualAttendanceBarChart(List<Person> people) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableList(days));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Attendance");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Individual Attendance");

        for (Person person : people) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(person.getName());
            List<LocalDateTime> attendedDates = new ArrayList<>(person.getDaysAttended());
            int[] dayFreq = new int[5];
            attendedDates.forEach(d -> {
                if (d.getDayOfWeek().getValue() < 6)
                    dayFreq[d.getDayOfWeek().getValue() - 1] += 1;
            });
            for (int i = 0; i <= 4; i++) {
                series.getData().add(new XYChart.Data<>(days.get(i), dayFreq[i]));
            }
            barChart.getData().add(series);
        }
        return barChart;
    }

    public BarChart<String, Number> getTotalAttendanceBarChart(Person person) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableList(days));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Attendance");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(person.getName() + "'s Attendance");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(person.getName());
        List<LocalDateTime> attendedDates = new ArrayList<>(person.getDaysAttended());
        int[] dayFreq = new int[5];
        attendedDates.forEach(d -> {
            if (d.getDayOfWeek().getValue() < 6)
                dayFreq[d.getDayOfWeek().getValue() - 1] += 1;
        });
        for (int i = 0; i <= 4; i++) {
            series.getData().add(new XYChart.Data<>(days.get(i), dayFreq[i]));
        }
        barChart.getData().add(series);

        return barChart;
    }

    public BarChart<String, Number> getTotalAttendanceBarChart(List<Person> personList) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Person");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Attendance");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Total Attendance");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Total attendance");
        personList.forEach(p -> {
            AtomicInteger i = new AtomicInteger();
            p.getDaysAttended().forEach(d -> {
                if (d.getDayOfWeek().getValue() < 6)
                    i.getAndIncrement();
            });
            series1.getData().add(new XYChart.Data<>(p.getName(), i));
        });
        barChart.getData().add(series1);
        return barChart;
    }

    public BarChart<String, Number> getAttendancePerDay(List<Person> personList) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableList(days));
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Attendance");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Total Attendance");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total attendance");
        int[] dayFreq = new int[5];
        personList.forEach(p -> {
            p.getDaysAttended().forEach(d -> {
                if (d.getDayOfWeek().getValue() < 6)
                    dayFreq[d.getDayOfWeek().getValue() - 1] += 1;
            });
        });
        for (int i = 0; i < 5; i++)
            series.getData().add(new XYChart.Data<>(days.get(i), dayFreq[i]));
        barChart.getData().add(series);
        return barChart;
    }
}
