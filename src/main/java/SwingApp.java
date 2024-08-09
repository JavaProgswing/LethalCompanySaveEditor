import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class SwingApp {

    private static JComboBox itemIdDropdown;
    private static JTextField itemPriceField;
    private static JTextField xPosField;
    private static JTextField yPosField;
    private static JTextField zPosField;
    private static Map<String, Integer> itemMap = new HashMap<>();

    static {
        itemMap.put("Binoculars", 0);
        itemMap.put("Boombox", 1);
        itemMap.put("Box", 2);
        itemMap.put("Flashlight", 3);
        itemMap.put("Jetpack", 4);
        itemMap.put("Key", 5);
        itemMap.put("Lockpicker", 6);
        itemMap.put("Apparatus", 7);
        itemMap.put("Mapper", 8);
        itemMap.put("Pro-flashlight", 9);
        itemMap.put("Shovel", 10);
        itemMap.put("Stun grenade", 11);
        itemMap.put("Extension ladder", 12);
        itemMap.put("TZP-Inhalant", 13);
        itemMap.put("Walkie-talkie", 14);
        itemMap.put("Zap gun", 15);
        itemMap.put("Magic 7 ball", 16);
        itemMap.put("Airhorn", 17);
        itemMap.put("Bell", 18);
        itemMap.put("Big bolt", 19);
        itemMap.put("Bottles", 20);
        itemMap.put("Brush", 21);
        itemMap.put("Candy", 22);
        itemMap.put("Cash register", 23);
        itemMap.put("Chemical jug", 24);
        itemMap.put("Clown horn", 25);
        itemMap.put("Large axle", 26);
        itemMap.put("Teeth", 27);
        itemMap.put("Dust pan", 28);
        itemMap.put("Egg beater", 29);
        itemMap.put("V-type engine", 30);
        itemMap.put("Golden cup", 31);
        itemMap.put("Fancy lamp", 32);
        itemMap.put("Painting", 33);
        itemMap.put("Plastic fish", 34);
        itemMap.put("Laser pointer", 35);
        itemMap.put("Gold bar", 36);
        itemMap.put("Hairdryer", 37);
        itemMap.put("Magnifying glass", 38);
        itemMap.put("Metal sheet", 39);
        itemMap.put("Cookie mold pan", 40);
        itemMap.put("Mug", 41);
        itemMap.put("Perfume bottle", 42);
        itemMap.put("Old phone", 43);
        itemMap.put("Jar of pickles", 44);
        itemMap.put("Pill bottle", 45);
        itemMap.put("Remote", 46);
        itemMap.put("Ring", 47);
        itemMap.put("Toy robot", 48);
        itemMap.put("Rubber Ducky", 49);
        itemMap.put("Red soda", 50);
        itemMap.put("Steering wheel", 51);
        itemMap.put("Stop sign", 52);
        itemMap.put("Tea kettle", 53);
        itemMap.put("Toothpaste", 54);
        itemMap.put("Toy cube", 55);
        itemMap.put("Hive", 56);
        itemMap.put("Radar-booster", 57);
        itemMap.put("Yield sign", 58);
        itemMap.put("Shotgun", 59);
        itemMap.put("Ammo", 60);
        itemMap.put("Spray paint", 61);
        itemMap.put("Homemade flashbang", 62);
        itemMap.put("Gift", 63);
        itemMap.put("Flask", 64);
        itemMap.put("Tragedy", 65);
        itemMap.put("Comedy", 66);
        itemMap.put("Whoopie cushion", 67);
    }

    public static void main(String[] args) {
        // Create frame
        JFrame frame = new JFrame("Item Editor");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2));

        // Add UI components
        frame.add(new JLabel("Item ID:"));
        itemIdDropdown = new JComboBox();
        for (String itemName : itemMap.keySet()) {
            itemIdDropdown.addItem(itemName);
        }
        frame.add(itemIdDropdown);

        frame.add(new JLabel("Item Price:"));
        itemPriceField = new JTextField();
        frame.add(itemPriceField);

        frame.add(new JLabel("X Position:"));
        xPosField = new JTextField("0.0");
        frame.add(xPosField);

        frame.add(new JLabel("Y Position:"));
        yPosField = new JTextField("0.0");
        frame.add(yPosField);

        frame.add(new JLabel("Z Position:"));
        zPosField = new JTextField("0.0");
        frame.add(zPosField);

        JButton saveButton = new JButton("Save Changes");
        frame.add(saveButton);



        // Action listener for the save button
        saveButton.addActionListener(e -> {
            try {
                int itemId = itemMap.get((String) itemIdDropdown.getSelectedItem());
                int itemPrice;
                try {
                    itemPrice = Integer.parseInt(itemPriceField.getText());
                } catch (NumberFormatException e1) {
                    itemPriceField.setText("500");
                    itemPrice = 500;
                }
                float x, y, z;
                try {
                    x = Float.parseFloat(xPosField.getText());
                } catch (NumberFormatException e1) {
                    xPosField.setText("0.0");
                    x = 0.0F;
                }
                try {
                    y = Float.parseFloat(yPosField.getText());
                } catch (NumberFormatException e1) {
                    yPosField.setText("0.0");
                    y = 0.0F;
                }
                try {
                    z = Float.parseFloat(zPosField.getText());
                } catch (NumberFormatException e1) {
                    zPosField.setText("0.0");
                    z = 0.0F;
                }
                ItemEditor.editGameSave(itemId, itemPrice, x, y, z);
                JOptionPane.showMessageDialog(frame, "File updated and saved successfully!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error occurred while updating the file.");
                ex.printStackTrace();
            } catch (InvalidAlgorithmParameterException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalBlockSizeException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (BadPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidKeySpecException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }
        });

        frame.setVisible(true);
    }
}
