package Views;

import com.carRental.Helper.*;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class Splash extends JFrame {
    private JPanel wrapper;
    private JProgressBar prg_progress;


    public Splash(){

        // Temayı Nimbus Olarak Değiştirdik
        Helper.changeUINimbus();
        add(wrapper);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setVisible(true);
        progress();


    }

    // Progress bar ilerleme süresi ayarlandı
    public void progress(){
        int min=0;
        int max=10;
        prg_progress.setMinimum(min);
        prg_progress.setMaximum(max);
        prg_progress.setValue(0);

        for(int i=min;i<=max;i++){
            prg_progress.setValue(i);
            try {
                sleep(500); // 500 milisaniyede bir progress dolması ayarı
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }



}
