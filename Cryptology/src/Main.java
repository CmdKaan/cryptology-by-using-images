import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.FileWriter;
class User//we used encapsulation in this user class
{
    private final static String password ="12345";
    private static String userName;
    public User() {
    }
    public User(String userName) {
        this.userName = userName;
    }
    public  static String getUserName() {
        return userName;
    }
    public static String getPassword() {
        return password;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
public class Main extends JFrame {
    private JLabel titleLabel;
    private JPanel mainPanel;
    private JPanel enPanel;
    private JPanel dePanel;
    private JButton enButton;
    private JButton deButton;
    private JButton backButton;
    private JButton backButton2;
    private JTextField messageBox;
    private JButton encryptButton;
    private JButton choseImageButton;
    private JButton choseImageButtonDecrypt;
    private JButton decryptButton;
    private JTextField userBox;
    private JPasswordField passwordBox;
    private JLabel passwordLabel;
    private JLabel userLabel;
    private JButton loginButton;
    public boolean imgChosen = false;
    public String pathImg;

    public String message;

    public Main(){
        createLogFile logFile = new createLogFile();

        logFile.createLogFile();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                logFile.writeLogFile(userBox.getText(),timeStamp,"User Logged off.\n");}
        });
        enPanel.setVisible(false);
        dePanel.setVisible(false);
        enButton.setVisible(false);
        deButton.setVisible(false);
        setContentPane(mainPanel);
        setTitle("Encrypt / Decrypt ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400,250);
        setLocationRelativeTo(null);
        setVisible(true);
        enButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enPanel.setVisible(true);
                deButton.setVisible(false);
                enButton.setVisible(false);
            }
        });
        deButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dePanel.setVisible(true);
                deButton.setVisible(false);
                enButton.setVisible(false);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enPanel.setVisible(false);
                deButton.setVisible(true);
                enButton.setVisible(true);
            }
        });
        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dePanel.setVisible(false);
                deButton.setVisible(true);
                enButton.setVisible(true);
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(imgChosen == true) {
                    try{
                        File image1 = new File(pathImg);
                        BufferedImage img = ImageIO.read(image1);
                        randomTransactions Transaction = new randomTransactions();
                        int messageLength = messageBox.getText().length();
                        String message1 = messageBox.getText();
                        boolean control = true;
                        for(int i = 0 ;i < messageLength;i++)
                        {
                            if(message1.charAt(i)>255)
                            {
                                JOptionPane.showMessageDialog(Main.this,"You can only use english characters ");
                                messageBox.setText("");
                                control =false;
                                break;
                            }

                        }
                        if(control) {
                            if (messageLength < 1 || messageLength > 255) {
                                JOptionPane.showMessageDialog(Main.this, "Your message's length has to be between 0 and 255");
                                messageBox.setText("");
                            } else {
                                message = messageBox.getText();
                            }
                            int targetBlue = Transaction.chooseTargetBlue();
                            encryptRgbValues.checkBlueValue(targetBlue, img);

                            {
                                int keyPixel = img.getRGB(0, 0);
                                Color color = new Color(keyPixel);
                                int blue = color.getBlue();
                                Color newColor = new Color(messageLength, targetBlue, blue);
                                img.setRGB(0, 0, newColor.getRGB());
                            }

                            int[] randomArray = Transaction.randomArray(message);
                            MergeSort.mergeSort(randomArray);

                            encryptRgbValues.encryptPixel(targetBlue, message, messageLength, randomArray, img);
                            JOptionPane.showMessageDialog(Main.this, "Message has been successfully encrypted.");
                            ImageIO.write(img, "png", new File("encrypted_image.png"));
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                            logFile.writeLogFile(User.getUserName(), timeStamp, "A message is encrypted.\n");
                            enPanel.setVisible(false);
                            deButton.setVisible(true);
                            enButton.setVisible(true);
                            messageBox.setText("");
                        }
                    }
                    catch (RuntimeException | IOException a){
                        throw new RuntimeException(a);


                    }

                }
                else{
                    JOptionPane.showMessageDialog(Main.this,"You have to Chose an Image First !!");
                }
            }
        });
        choseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choseImg();

            }
        });
        choseImageButtonDecrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choseImg();
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(imgChosen == true){
                    try {
                        File image2 = new File(pathImg);
                        BufferedImage img2 = ImageIO.read(image2);
                        Decryption decryption = new Decryption();
                        int[] infoArray=decryption.decryptionKey(encryptRgbValues.img);
                        int[][] messageArray=decryption.decryptionPixels(encryptRgbValues.img,infoArray);

                        JOptionPane.showMessageDialog(Main.this,"Message has been successfully decrypted : "+decryption.readMessage(messageArray));
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                        logFile.writeLogFile(User.getUserName(),timeStamp,"A message is decrypted.\n");

                    }
                    catch (RuntimeException | IOException a){
                        throw new RuntimeException(a);

                    }

                }
                else{
                    JOptionPane.showMessageDialog(Main.this,"You have to Chose an Image First to Decrypt!!");
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User(userBox.getText());
                String getPassword = new String(passwordBox.getPassword());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                if(getPassword.equals(User.getPassword())){
                    logFile.writeLogFile(User.getUserName(),timeStamp,"A new login.\n");
                    JOptionPane.showMessageDialog(Main.this,"You have successfully logged in\nUser Name: "+ user.getUserName()+"\nDate "+ timeStamp);
                    enButton.setVisible(true);
                    deButton.setVisible(true);
                    passwordBox.setVisible(false);
                    userBox.setVisible(false);
                    loginButton.setVisible(false);
                    passwordLabel.setVisible(false);
                    userLabel.setVisible(false);
                }
                else{
                    logFile.writeLogFile(userBox.getText(),timeStamp,"A failed login (Incorrect Password).\n");
                    JOptionPane.showMessageDialog(Main.this,"Password is incorrect");
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();

    }


    public void choseImg(){
        createLogFile logFile = new createLogFile();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        try
        {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","png","jpg","jpeg");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File("."));
            int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = new File(chooser.getSelectedFile().getAbsolutePath());
                pathImg = selectedFile.getAbsolutePath();
                System.out.println(pathImg);
                BufferedImage img = ImageIO.read(new File(pathImg));
                int width          = img.getWidth();
                int height         = img.getHeight();
                if(width > 50 && height > 50){
                    JOptionPane.showMessageDialog(Main.this,"You Image's Resolution should be <= 50,50 ");
                    logFile.writeLogFile(userBox.getText(),timeStamp,"Failed to Chose Image (Wrong Resolutions).\n");
                    choseImg();
                }
                else{
                    imgChosen = true;

                    logFile.writeLogFile(User.getUserName(),timeStamp,"An image is chosed Path:  ."+pathImg+"\n");
                }
            }
        }
        catch (Exception a)
        {
            JOptionPane.showMessageDialog(Main.this,a);

        }

    }



}
class encryptRgbValues {

    public static BufferedImage img;
    private static int targetBlue;


    //The method which we increase the blue value of pixels that have the same blue value as the random blue value by 1
    public static void checkBlueValue(int targetBlue,BufferedImage img) {
        encryptRgbValues.targetBlue = targetBlue;
        encryptRgbValues.img = img;

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int pixel = img.getRGB(x, y);
                Color color = new Color(pixel);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                if (blue == targetBlue) {
                    blue = blue + 1;
                    Color newColor = new Color(red, green, blue);
                    img.setRGB(x, y, newColor.getRGB());
                }

            }
        }
    }

    //The method which pixels are encrypted
    public static void encryptPixel(int targetBlue, String message, int messageLength, int[] Array, BufferedImage img) {

        final Random rand = new Random();

        int[][] imagePixels = new int[img.getHeight()][img.getWidth()];


        for (int i = 0; i < messageLength; i++) {

            boolean fill = true;

            while (fill) {

                int rows = rand.nextInt(img.getHeight());
                int columns = rand.nextInt(img.getWidth());
                if (imagePixels[rows][columns] == 0) {
                    Color newColor = new Color(Array[i], (int) message.charAt(i), targetBlue);
                    img.setRGB(rows, columns, newColor.getRGB());
                    fill = false;
                    imagePixels[rows][columns]++;
                }
            }
        }

    }
}

class randomTransactions {
    private static final Random rand = new Random(); //Random object. (It will be used to get random integers from the list)

    //The method which the random blue value is determined
    public int chooseTargetBlue() {
        int targetBlue = rand.nextInt(254); //If targetBlue is equal to 255, we cannot change the value of pixels whose value is already 255.
        return targetBlue;
    }

    //The method in which a random array with different elements is created
    public int[] randomArray(String Message) {
        final List<Integer> numbers = new ArrayList<>();
        //This loop fills the number arraylist by range(0,256).
        for (int i = 0; i < 256; i++) {
            numbers.add(i);
        }

        int length = Message.length();

        int[] randomArr = new int[length];

        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(numbers.size());
            int randomInt = numbers.get(randomIndex);
            numbers.remove(randomIndex);
            randomArr[i] = randomInt;
        }
        return randomArr;
    }

    //We used bubble sort method to sort the elements of the array
    public void bubbleSort(int[] array)
    {
        int length = array.length;
        boolean swapped;
        for (int i = 0; i < length - 1; i++) {
            swapped = false;
            for (int j = 0; j < length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}

class MergeSort {

    public static void mergeSort(int[] array) {
        if (array.length > 1) {
            int mid = array.length / 2;

            // Split the array into two halves
            int[] left = new int[mid];
            int[] right = new int[array.length - mid];

            System.arraycopy(array, 0, left, 0, mid);
            System.arraycopy(array, mid, right, 0, array.length - mid);

            // Recursively sort each half
            mergeSort(left);
            mergeSort(right);

            // Merge the sorted halves back together
            merge(array, left, right);
        }
    }

    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        // Merge the left and right arrays
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // Copy any remaining elements of left
        while (i < left.length) {
            array[k++] = left[i++];
        }

        // Copy any remaining elements of right
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }
}
class Decryption{

    public int[] decryptionKey(BufferedImage img){
        int[] infoArray=new int[2];
        int pixel= img.getRGB(0,0);
        Color color=new Color(pixel);
        infoArray[0]=color.getRed();
        infoArray[1]=color.getGreen();
        return infoArray;
    }

    public int[][] decryptionPixels(BufferedImage img,int[] infoArray){
        int[][] messageArray=new int[2][infoArray[0]];
        int value=0;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int pixel = img.getRGB(x, y);
                Color color = new Color(pixel);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                if (blue == infoArray[1]){
                    messageArray[0][value]=red;
                    messageArray[1][value]=green;
                    value++;

                }
            }
        }

        return messageArray;
    }
    public String readMessage(int[][] messageArray) {
        int length = messageArray[0].length;
        //Bubble sort
        boolean swapped;
        for (int i = 0; i < length - 1; i++) {
            swapped = false;
            for (int j = 0; j < length - 1 - i; j++) {
                if (messageArray[0][j] > messageArray[0][j + 1]) {
                    int[][] temp = new int[2][1];
                    temp[0][0] = messageArray[0][j];
                    temp[1][0] = messageArray[1][j];
                    messageArray[0][j] = messageArray[0][j + 1];
                    messageArray[1][j] = messageArray[1][j + 1];
                    messageArray[0][j + 1] = temp[0][0];
                    messageArray[1][j + 1] = temp[1][0];
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        ArrayList<Character> decryptedMessage = new ArrayList<>();
        //Write message
        for (int i = 0; i < length; i++) {
            char decryptedChar = (char) messageArray[1][i];
            decryptedMessage.add(decryptedChar);
        }


        String decryptedString = decryptedMessage.toString()
                .replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();

        return decryptedString;

    }

}

class createLogFile {
    public void createLogFile() {
        try {
            File myObj = new File("logfile.txt");
            myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeLogFile(String user,String timeStamp, String message){
        try {
            FileWriter myWriter = new FileWriter("logfile.txt",true);
            myWriter.write("User: "+user+" Date: "+timeStamp+" Action: "+message);
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}