import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.util.Random;
import java.awt.geom.GeneralPath; 

public class PasswordGeneratorApp extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JLabel typeLabel;
    private JRadioButton passwordRadio;
    private JRadioButton pinRadio;
    private JLabel lengthLabel;
    private JSlider lengthSlider;
    private JTextField lengthField;
    private JButton generateButton;
    private JButton copyButton;
    private JButton clearButton;
    private JTextArea passwordArea;

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String COMPLEX = "!@#$%&*_+=-";

    private boolean passwordGenerated = false;

    public PasswordGeneratorApp() {
        //default window 
    	setTitle("Password Generator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("C:\\Users\\anand\\eclipse-workspace\\deml\\src\\theme1.jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        typeLabel = new JLabel("Select Type:");
        typeLabel.setForeground(Color.BLACK);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 23));
        typeLabel.setBounds(100, 16, 200, 50);

        passwordRadio = new JRadioButton("Password");
        passwordRadio.setSelected(true);
        passwordRadio.setFont(new Font("Arial", Font.BOLD, 18));
        passwordRadio.setBounds(110, 60, 120, 30);

        pinRadio = new JRadioButton("PIN");
        pinRadio.setFont(new Font("Arial", Font.BOLD, 18));
        pinRadio.setBounds(230, 60, 80, 30);

        ButtonGroup group = new ButtonGroup();
        group.add(passwordRadio);
        group.add(pinRadio);

        lengthLabel = new JLabel("Length:");
        lengthLabel.setForeground(Color.BLACK);
        lengthLabel.setFont(new Font("Arial", Font.BOLD, 24));
        lengthLabel.setBounds(100, 110, 200, 50);
        
        // Default range for password
        lengthSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 10); 
        lengthSlider.setMajorTickSpacing(10);
        lengthSlider.setMinorTickSpacing(1);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);

        // Set the default value of the slider to 10
        lengthSlider.setValue(10);

        lengthSlider.setBounds(100, 160, 300, 50);

        // Customize the appearance of the lengthSlider
        lengthSlider.setUI(new BasicSliderUI(lengthSlider) {
            @Override
            protected Dimension getThumbSize() {
                return new Dimension(12, 15); 
            }

            @Override
            public void paintTrack(Graphics g) {
                Rectangle trackBounds = trackRect;
                Graphics2D g2d = (Graphics2D) g;
             
                g2d.setColor(new Color(200, 175, 135)); // Left side color
                g2d.fillRect(trackBounds.x, trackBounds.y + 4, thumbRect.x - trackBounds.x, trackBounds.height - 8); // Adjust the thickness (4, 8)
               
                g2d.setColor(new Color(192, 192, 192)); // Right side White color
                g2d.fillRect(thumbRect.x, trackBounds.y + 4, trackBounds.width - (thumbRect.x - trackBounds.x), trackBounds.height - 8); // Adjust the thickness (4, 8)
            }
            @Override
            public void paintThumb(Graphics g) {
                Rectangle thumbBounds = thumbRect;
                Graphics2D g2d = (Graphics2D) g;

                // Define the shape of an upside-down link symbol
                GeneralPath path = new GeneralPath();
                path.moveTo(thumbBounds.x + thumbBounds.width / 2, thumbBounds.y + thumbBounds.height);
                path.lineTo(thumbBounds.x, thumbBounds.y + thumbBounds.height / 2);
                path.lineTo(thumbBounds.x + thumbBounds.width / 4, thumbBounds.y + thumbBounds.height / 2);
                path.lineTo(thumbBounds.x + thumbBounds.width / 4, thumbBounds.y);
                path.lineTo(thumbBounds.x + 3 * thumbBounds.width / 4, thumbBounds.y);
                path.lineTo(thumbBounds.x + 3 * thumbBounds.width / 4, thumbBounds.y + thumbBounds.height / 2);
                path.lineTo(thumbBounds.x + thumbBounds.width, thumbBounds.y + thumbBounds.height / 2);

                // Upside-down link symbol with black outline and black fill
                g2d.setColor(Color.BLACK);
                g2d.draw(path);
                g2d.fill(path);
            }
        });

        lengthField = new JTextField();
        lengthField.setFont(new Font("Arial", Font.BOLD, 15));
        lengthField.setBounds(100, 220, 50, 40);

        // Default value of the lengthField to 10
        lengthField.setText("10");

        lengthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int maxRange = pinRadio.isSelected() ? 12 : 50;
                lengthSlider.setMaximum(maxRange);
                lengthField.setText(Integer.toString(lengthSlider.getValue()));
            }
        });

        generateButton = new JButton("Generate");
        generateButton.setBounds(100, 280, 150, 50);
        generateButton.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 18));
        generateButton.setBackground(new Color(200, 175, 135));
        generateButton.setForeground(Color.BLACK);
        generateButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray));
        generateButton.addActionListener(this);

        copyButton = new JButton("Copy");
        copyButton.setBounds(270, 280, 100, 50);
        copyButton.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 18));
        copyButton.setBackground(new Color(200, 175, 135)); 
        copyButton.setForeground(Color.BLACK); 
        copyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray));
        copyButton.addActionListener(this);
        copyButton.setVisible(false);


        clearButton = new JButton("Clear");
        clearButton.setBounds(380, 280, 100, 50);
        clearButton.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 18));
        clearButton.setBackground(new Color(200, 175, 135));
        clearButton.setForeground(Color.BLACK);
        clearButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray));
        clearButton.addActionListener(this);
        clearButton.setVisible(false);

        passwordArea = new JTextArea();
        passwordArea.setEditable(false);
        passwordArea.setBackground(new Color(240, 240, 240));
        passwordArea.setFont(new Font("Arial", Font.BOLD, 20));
        passwordArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));
        passwordArea.setBounds(70, 350, 630, 100);

        mainPanel.add(typeLabel);
        mainPanel.add(passwordRadio);
        mainPanel.add(pinRadio);
        mainPanel.add(lengthLabel);
        mainPanel.add(lengthSlider);
        mainPanel.add(lengthField);
        mainPanel.add(generateButton);
        mainPanel.add(copyButton);
        mainPanel.add(clearButton);
        mainPanel.add(passwordArea);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    
    //Warnings and copy messages 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            try {
                int length = Integer.parseInt(lengthField.getText());
                int maxRange = pinRadio.isSelected() ? 12 : 50;

                if (length <= maxRange && length >= 1) {
                    String complexity = pinRadio.isSelected() ? DIGITS : askComplexity(length);
                    String password = generatePassword(length, complexity);
                    passwordArea.setText(password);
                    copyButton.setVisible(true);
                    clearButton.setVisible(true);
                    passwordGenerated = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a number between 1-" + maxRange);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for password length.");
            }
        } else if (e.getSource() == copyButton) {
            String password = passwordArea.getText();
            if (!password.isEmpty()) {
                copyToClipboard(password);
                JOptionPane.showMessageDialog(this, "Password copied to clipboard.");
            }
        } else if (e.getSource() == clearButton) {
            clearAll();
        }
    }

    //ask complexity
    private String askComplexity(int length) {
        String[] options = {"Simple (Lowercase only)", "Medium (Lowercase and digits)", "Complex (All characters)"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select the password complexity:",
                "Password Complexity",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                return LOWERCASE;
            case 1:
                return LOWERCASE + DIGITS;
            default:
                return LOWERCASE + DIGITS + UPPERCASE + COMPLEX;
        }
    }

    private String generatePassword(int length, String complexity) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(complexity.length());
            password.append(complexity.charAt(index));
        }

        return password.toString();
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    private void clearAll() {
        passwordArea.setText("");
        lengthSlider.setValue(10);
        lengthField.setText("10");
        passwordRadio.setSelected(true);
        copyButton.setVisible(false);
        clearButton.setVisible(false);
        passwordGenerated = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasswordGeneratorApp());
    }
}
