
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javax.swing.*;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
 class MyClockProject extends JFrame {
    JLabel clockLabel, timerLabel, stopwatchLabel;
    Timer clockTimer, countdownTimer, stopwatchTimer, worldClockTimer;

    int countdownSeconds = 0;
    int stopwatchSeconds = 0;
    boolean isStopwatchRunning = false;

    JLabel nyLabel, londonLabel, tokyoLabel, sydneyLabel, indiaLabel;

    public MyClockProject() {
        setTitle("MyClock - Clock | Timer | Stopwatch | World Clock");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Clock", createClockPanel());
        tabbedPane.add("Timer", createTimerPanel());
        tabbedPane.add("Stopwatch", createStopwatchPanel());
        tabbedPane.add("World Clock", createWorldClockPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);

        startClock();
        startWorldClock();
    }

    private JPanel createClockPanel() {
        JPanel panel = new BackgroundPanel("background.jpg");
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.BOLD, 30));
        clockLabel.setForeground(Color.BLACK);
        panel.add(clockLabel);
        return panel;
    }

    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            Calendar calendar = Calendar.getInstance();
            String time = String.format("%02d:%02d:%02d",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
            clockLabel.setText(time);
        });
        clockTimer.start();
    }

    private JPanel createTimerPanel() {
        JPanel panel = new BackgroundPanel("background.jpg");
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel minLabel = new JLabel("Minutes:");
        JTextField minField = new JTextField();
        JLabel secLabel = new JLabel("Seconds:");
        JTextField secField = new JTextField();
        JButton startBtn = new JButton("Start Timer");
        JButton stopBtn = new JButton("Stop Timer");
        JButton resetBtn = new JButton("Reset Timer");
        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 25));

        startBtn.addActionListener(e -> {
            try {
                int minutes = Integer.parseInt(minField.getText());
                int seconds = Integer.parseInt(secField.getText());
                countdownSeconds = minutes * 60 + seconds;
                if (countdownSeconds <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter time > 0");
                    return;
                }
                startCountdownTimer();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers");
            }
        });

        stopBtn.addActionListener(e -> {
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
                timerLabel.setText("Stopped at: " + String.format("%02d:%02d", countdownSeconds / 60, countdownSeconds % 60));
            }
        });

        resetBtn.addActionListener(e -> {
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
            }
            countdownSeconds = 0;
            minField.setText("");
            secField.setText("");
            timerLabel.setText("00:00");
        });

        panel.add(minLabel);
        panel.add(minField);
        panel.add(secLabel);
        panel.add(secField);
        panel.add(startBtn);
        panel.add(stopBtn);
        panel.add(resetBtn);
        panel.add(timerLabel);

        return panel;
    }

    private void startCountdownTimer() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }

        countdownTimer = new Timer(1000, e -> {
            if (countdownSeconds > 0) {
                countdownSeconds--;
                int min = countdownSeconds / 60;
                int sec = countdownSeconds % 60;
                timerLabel.setText(String.format("%02d:%02d", min, sec));
            } else {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Time's up!");
            }
        });
        countdownTimer.start();
    }

    private JPanel createStopwatchPanel() {
        JPanel panel = new BackgroundPanel("background.jpg");
        panel.setLayout(new BorderLayout());

        stopwatchLabel = new JLabel("00:00", SwingConstants.CENTER);
        stopwatchLabel.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel btnPanel = new JPanel();

        JButton startBtn = new JButton("Start");
        JButton stopBtn = new JButton("Stop");
        JButton resetBtn = new JButton("Reset");

        startBtn.addActionListener(e -> startStopwatch());
        stopBtn.addActionListener(e -> stopStopwatch());
        resetBtn.addActionListener(e -> resetStopwatch());

        btnPanel.add(startBtn);
        btnPanel.add(stopBtn);
        btnPanel.add(resetBtn);

        panel.add(stopwatchLabel, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void startStopwatch() {
        if (isStopwatchRunning) return;

        isStopwatchRunning = true;
        stopwatchTimer = new Timer(1000, e -> {
            stopwatchSeconds++;
            int min = stopwatchSeconds / 60;
            int sec = stopwatchSeconds % 60;
            stopwatchLabel.setText(String.format("%02d:%02d", min, sec));
        });
        stopwatchTimer.start();
    }

    private void stopStopwatch() {
        if (stopwatchTimer != null) {
            stopwatchTimer.stop();
        }
        isStopwatchRunning = false;
    }

    private void resetStopwatch() {
        stopStopwatch();
        stopwatchSeconds = 0;
        stopwatchLabel.setText("00:00");
    }

    private JPanel createWorldClockPanel() {
        JPanel panel = new BackgroundPanel("background.jpg");
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        nyLabel = new JLabel("New York: ", SwingConstants.CENTER);
        londonLabel = new JLabel("London: ", SwingConstants.CENTER);
        tokyoLabel = new JLabel("Tokyo: ", SwingConstants.CENTER);
        sydneyLabel = new JLabel("Sydney: ", SwingConstants.CENTER);
        indiaLabel = new JLabel("India: ", SwingConstants.CENTER);

        Font font = new Font("Arial", Font.BOLD, 20);
        nyLabel.setFont(font);
        londonLabel.setFont(font);
        tokyoLabel.setFont(font);
        sydneyLabel.setFont(font);
        indiaLabel.setFont(font);

        panel.add(nyLabel);
        panel.add(londonLabel);
        panel.add(tokyoLabel);
        panel.add(sydneyLabel);
        panel.add(indiaLabel);

        return panel;
    }

    private void startWorldClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        worldClockTimer = new Timer(1000, e -> {
            nyLabel.setText("New York: " + ZonedDateTime.now(ZoneId.of("America/New_York")).format(formatter));
            londonLabel.setText("London: " + ZonedDateTime.now(ZoneId.of("Europe/London")).format(formatter));
            tokyoLabel.setText("Tokyo: " + ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter));
            sydneyLabel.setText("Sydney: " + ZonedDateTime.now(ZoneId.of("Australia/Sydney")).format(formatter));
            indiaLabel.setText("India: " + ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).format(formatter));
        });

        worldClockTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyClockProject::new);
    }
}
