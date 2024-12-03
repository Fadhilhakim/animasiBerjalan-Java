package com;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JFrame;


public class animasiBerjalan extends JPanel implements Runnable {

    Image karakter[] = new Image[11];
    String karakterImg[] = {"ambilKursi.png", "taruKursi.png", "duduk.png", "idle.png", "1.png","2.png","3.png","4.png","5.png","6.png","7.png"};

    Image currentImage;
    Thread runner;
    int xpos;
    int ypos = 50;
    boolean balikKiri = false;

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void stop() {
        if (runner != null) {
            runner.interrupt();
            runner = null;
        }
    }

    public void run() {
        for (int i = 0; i < karakterImg.length; i++) {
            karakter[i] = Toolkit.getDefaultToolkit().getImage("src/images" + karakterImg[i]);
        }

        setBackground(Color.black);
        for (int idx = 0; idx < 5; idx++) {
            
            balikKiri = false;
            // animasi masuk
            jalan(0, this.getWidth() / 3);
            pause(100);
            
            duduk(); // idle, ambil kursi dan duduk
            
            // animasi keluar ke kiri
            jalanKeKiri(this.getWidth() / 2, 0);
            pause(100);
            
            duduk(); // idle, ambil kursi dan duduk
        }
    }
    
    void jalan(int start, int end) {

        currentImage = karakter[3]; // idle    
        repaint();
        pause(700);

        playSound("lala1.wav");


        boolean j1 = true;

        for (int i = start; i < end; i += 100) {
            
            if (j1 == true) {
                playSound("jalankiri.wav"); // Putar audio kiri
                j1 = false;
            } else {
                playSound("jalanKanan.wav"); // Putar audio kanan
            }
            playSound("jalankiri.wav"); // Putar audio kiri

            xpos = i;
            for (int j = 4; j <= 10; j++) { 
                xpos+=10;
                currentImage = karakter[j];
                repaint();
                pause(100);
            }
        }
    }

    void duduk() {
        playSound("jalankiri.wav"); // Putar audio kiri
        currentImage = karakter[3]; // idle
        repaint();
        pause(1000);

        playSound("ambil_kursi.wav");
        currentImage = karakter[0]; // ambil kursi
        repaint();
        pause(500);

        playSound("taru_kursi.wav");
        currentImage = karakter[1]; // taru kursi
        repaint();
        pause(1000);
        
        playSound("duduk.wav");
        currentImage = karakter[2]; // duduk
        repaint();
        playSound("hai.wav");
        pause(1000);
    }

    void jalanKeKiri(int start, int end) {

        balikKiri = true; // Membalik arah gambar ke kiri
        
        currentImage = karakter[3]; // idle
        repaint();
        pause(700);
        
        playSound("lala2.wav");
        
        boolean j1 = true;

        for (int i = start; i > end; i -= 100) {
            
            if (j1 == true) {
                playSound("jalankiri.wav"); // Putar audio kiri
                j1 = false;
            } else {
                playSound("jalanKanan.wav"); // Putar audio kanan
            }

            xpos = i;
            for (int j = 4; j <= 10; j++) {
                xpos-=10;
                currentImage = karakter[j];
                repaint();
                pause(100);
            }
        }
    }
    
    void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }

    public void paint(Graphics g) {
        super.paint(g); 
        
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            if (balikKiri == true) {
                g2d.drawImage(currentImage, xpos + currentImage.getWidth(this), ypos, -currentImage.getWidth(this), currentImage.getHeight(this), this);
            } else if (balikKiri == false) {
                g2d.drawImage(currentImage, xpos, ypos, this);
            } else {
                g2d.drawImage(currentImage, xpos, ypos, this);

            }
        }
    }

    void playSound(String fileName) {
        try {
            File file = new File("src/audio" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("animasiBerjalan");
        animasiBerjalan animasiPanel = new animasiBerjalan();
        frame.add(animasiPanel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        animasiPanel.start();   
    }
}
