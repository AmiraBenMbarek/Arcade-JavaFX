package GUI;

import Entities.Evenement;
import java.awt.Color;
import java.awt.Rectangle;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * 
 */
public class FullCalendarView {
    private final VBox view;
    private final Text calendarTitle;
    private ArrayList<AnchorPane> allCalendarDays = new ArrayList<>(35);
    private YearMonth currentYearMonth;
    private List<Evenement> listeEvenement;
    private Button previousMonth;
    private Button nextMonth;

        public FullCalendarView(YearMonth yearMonth) {
            currentYearMonth = yearMonth;
            // Create the calendar grid pane
            GridPane calendar = new GridPane();
            calendar.setPrefSize(600, 400);
            calendar.setGridLinesVisible(true);
            // Create rows and columns with anchor panes for the calendar
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    AnchorPane ap = new AnchorPane();
                    ap.setPrefSize(200, 200);
                    ap.setStyle("-fx-background-color: #2d292d; ");

                    calendar.add(ap, j, i);
                    allCalendarDays.add(ap);
                }
            }
            // Days of the week labels
            Text[] dayNames = new Text[]{new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                    new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                    new Text("Saturday")};
            GridPane dayLabels = new GridPane();
            dayLabels.setPrefWidth(600);
            int col = 0;
            for (Text txt : dayNames) {
                AnchorPane ap = new AnchorPane();
                ap.setPrefSize(200, 10);
                ap.setStyle("-fx-background-color: #180d1b");
                txt.setFill(Paint.valueOf("#FFFFFF"));
                txt.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                AnchorPane.setBottomAnchor(txt, 5.0);
                ap.getChildren().add(txt);
                dayLabels.add(ap, col++, 0);
            }
            // Create calendarTitle and buttons to change current month

            calendarTitle = new Text();
            calendarTitle.setFill(Paint.valueOf("WHITE"));
            calendarTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
            Button previousMonth = new Button("<<");
            previousMonth.setStyle("-fx-background-color: linear-gradient(to bottom, #4b0082, #ee5622); -fx-text-fill: WHITE;");
            previousMonth.setOnAction(e -> previousMonth());
            Button nextMonth = new Button(">>");
            nextMonth.setStyle("-fx-background-color: linear-gradient(to bottom, #4b0082, #ee5622); -fx-text-fill: WHITE");
            nextMonth.setOnAction(e -> nextMonth());
            HBox titleBar = new HBox(10, previousMonth, calendarTitle, nextMonth);
            titleBar.setStyle("-fx-background-color: #181b26");
            titleBar.setAlignment(Pos.BASELINE_CENTER);
            // Populate calendar with the appropriate day numbers
            populateCalendar(yearMonth, listeEvenement);
            // Create the calendar view
            view = new VBox(titleBar, dayLabels, calendar);
        }


   
        public void populateCalendar(YearMonth yearMonth, List<Evenement> events) {
            
          currentYearMonth = yearMonth;
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
          calendarTitle.setText(yearMonth.format(formatter));
          LocalDate firstOfMonth = yearMonth.atDay(1);
          int daysInMonth = yearMonth.lengthOfMonth();
          WeekFields weekFields = WeekFields.of(Locale.getDefault());
          int weekNumber = firstOfMonth.get(weekFields.weekOfWeekBasedYear());
          // Populate calendar with day numbers and events
          for (AnchorPane ap : allCalendarDays) {
              ap.getChildren().clear();
              int row = allCalendarDays.indexOf(ap) / 7;
              int col = allCalendarDays.indexOf(ap) % 7;
              int dayNumber = row * 7 + col + 1 - firstOfMonth.getDayOfWeek().getValue();
              if (dayNumber > 0 && dayNumber <= daysInMonth) {
                  VBox vbox = new VBox();
                  Label label = new Label(Integer.toString(dayNumber));
                  label.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold;");

                  vbox.getChildren().addAll(label);
                  // Check if there are any events on this day
                   if (events != null && !events.isEmpty()) {
                      for (Evenement event : events) {
                          highlightDays(event, yearMonth);
                      }
                   }
                  ap.getChildren().add(vbox);
                  AnchorPane.setTopAnchor(vbox, 5.0);
                  AnchorPane.setLeftAnchor(vbox, 5.0);
                  AnchorPane.setRightAnchor(vbox, 5.0);
                  AnchorPane.setBottomAnchor(vbox, 5.0);
                  // Highlight the day if it is today's date
                  if (LocalDate.now().equals(yearMonth.atDay(dayNumber))) {
                      ap.setStyle("-fx-background-color:  #413f63; -fx-border-color: #ff6b00;");
                  } else {
                      ap.setStyle("-fx-background-color: #180d1b ;-fx-border-color: #ff6b00; -fx-border-width: 0.2px;");
                  }
              } else {
                  ap.setStyle("-fx-background-color: #2d292d;");
              }
          }
      }


    
 
        private void previousMonth() {
            currentYearMonth = currentYearMonth.minusMonths(1);

            populateCalendar(currentYearMonth, listeEvenement);

        }


        private void nextMonth() {
            currentYearMonth = currentYearMonth.plusMonths(1);
            populateCalendar(currentYearMonth, listeEvenement);


        }


        public VBox getView() {
            return view;
        }
    
        public void highlightDays(Evenement event, YearMonth yearMonth) {
            Date startDate = event.getDateDebutE();
            Date endDate = event.getDateFinE();
            String eventName = event.getNomEvent();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // Highlight the days in the range of the event in the specified month
            while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                int dayNumber = calendar.get(Calendar.DAY_OF_MONTH);
                if (calendar.get(Calendar.MONTH) == yearMonth.getMonthValue() - 1) {
                    AnchorPane ap = allCalendarDays.get(dayNumber - 1);

                    // Create a VBox with the event name and set its style
                    VBox eventBox = new VBox();
                    eventBox.setStyle("-fx-background-color:  #781e77 ; -fx-padding: 2px;");
                    Label eventNameLabel = new Label(eventName);
                    eventNameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                    eventBox.getChildren().add(eventNameLabel);

                    ap.getChildren().add(eventBox);
                    AnchorPane.setTopAnchor(eventBox, 5.0);
                    AnchorPane.setLeftAnchor(eventBox, 5.0);
                    AnchorPane.setRightAnchor(eventBox, 5.0);
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1); 

               
            }
        }




}



