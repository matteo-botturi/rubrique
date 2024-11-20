package fr.mb.rubrique.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Utility class for managing and displaying a real-time clock in the application.
 * Updates a {@link Label} with the current date and time at regular intervals.
 */
public class ClockManager {
	
	private ClockManager() {
		throw new UnsupportedOperationException("Utility class");
	}

    private static final String CLOCK_FORMAT = "EEE dd MMM yyyy, HH:mm:ss";

    /**
     * Starts a clock that updates the specified label every second with the current date and time.
     *
     * @param clockLabel the label to update with the current time
     */
    public static void startClock(Label clockLabel) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock(clockLabel)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates the specified label with the current date and time.
     * The date and time are formatted according to the {@code CLOCK_FORMAT}.
     *
     * @param clockLabel the label to update with the current date and time
     */
    private static void updateClock(Label clockLabel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CLOCK_FORMAT);
        String currentTime = LocalDateTime.now().format(formatter);
        clockLabel.setText(currentTime);
    }
}