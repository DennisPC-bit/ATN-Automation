package BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author DennisPC-bit
 */

public class PieChartUtils {
    private final List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

    public PieChart getPersonPieChart(List<Person> personList) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Person person : personList)
            pieChartData.add(new PieChart.Data(person.getName(), person.getDaysAttended().size()));
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Fraction of total attendance");
        return pieChart;
    }

    public PieChart getPersonPieChart(Person person) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<LocalDateTime> attendedDates = new ArrayList<>(person.getDaysAttended());
        int[] dayFreq = new int[5];
        attendedDates.forEach(d -> {
            if (d.getDayOfWeek().getValue() < 6)
                dayFreq[d.getDayOfWeek().getValue() - 1] += 1;
        });
        for (int i = 0; i <= 4; i++) {
            if (dayFreq[i] > 0)
                pieChartData.add(new PieChart.Data(days.get(i), dayFreq[i]));
        }
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Pie chart of " + person.getName() + "'s Attendance");
        return pieChart;
    }

    public PieChart getAttendancePerDayPieChart(List<Person> personList) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int[] dayFreq = new int[5];
        personList.forEach(p -> {
            p.getDaysAttended().forEach(d -> {
                if (d.getDayOfWeek().getValue() < 6)
                    dayFreq[d.getDayOfWeek().getValue() - 1] += 1;
            });
        });
        for (int i = 0; i < 5; i++) {
            if (dayFreq[i] > 0)
                pieChartData.add(new PieChart.Data(days.get(i), dayFreq[i]));
        }
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Fraction of total attendance");
        return pieChart;
    }
}
