/*
 * WestfieldClientReportView.java
 */
package com.dramble;

import java.awt.Desktop;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskService;

/**
 * The application's main frame.
 */
public class WestfieldClientReportView extends FrameView {

    public WestfieldClientReportView(SingleFrameApplication app) {
        super(app);

        initComponents();

        this.getFrame().setIconImage(new ImageIcon(WestfieldClientReportApp.class.getResource("/com/dramble/resources/westfield-logo-icon.png")).getImage());

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = WestfieldClientReportApp.getApplication().getMainFrame();
            aboutBox = new WestfieldClientReportAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        WestfieldClientReportApp.getApplication().show(aboutBox);
    }

    @Action
    public void showSettingsBox() {
        if (settingsBox == null) {
            JFrame mainFrame = WestfieldClientReportApp.getApplication().getMainFrame();
            settingsBox = new WestfieldClientReportSettings(mainFrame);
            settingsBox.setLocationRelativeTo(mainFrame);
        }
        WestfieldClientReportApp.getApplication().show(settingsBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        firstReportButton = new javax.swing.JButton();
        secondReportButton = new javax.swing.JButton();
        savedFileChooserButton = new javax.swing.JButton();
        firstReportURL = new javax.swing.JTextField();
        secondReportURL = new javax.swing.JTextField();
        savedFileURL = new javax.swing.JTextField();
        compare = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        settingsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        firstReportFileChooser = new javax.swing.JFileChooser();
        secondReportFileChooser = new javax.swing.JFileChooser();
        savedFileChooser = new javax.swing.JFileChooser();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.dramble.WestfieldClientReportApp.class).getContext().getResourceMap(WestfieldClientReportView.class);
        firstReportButton.setText(resourceMap.getString("firstReportButton.text")); // NOI18N
        firstReportButton.setName("firstReportButton"); // NOI18N
        firstReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstReportButtonActionPerformed(evt);
            }
        });

        secondReportButton.setText(resourceMap.getString("secondReportButton.text")); // NOI18N
        secondReportButton.setName("secondReportButton"); // NOI18N
        secondReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secondReportButtonActionPerformed(evt);
            }
        });

        savedFileChooserButton.setText(resourceMap.getString("savedFileChooserButton.text")); // NOI18N
        savedFileChooserButton.setName("savedFileChooserButton"); // NOI18N
        savedFileChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedFileChooserButtonActionPerformed(evt);
            }
        });

        firstReportURL.setText(resourceMap.getString("firstReportURL.text")); // NOI18N
        firstReportURL.setName("firstReportURL"); // NOI18N

        secondReportURL.setText(resourceMap.getString("secondReportURL.text")); // NOI18N
        secondReportURL.setName("secondReportURL"); // NOI18N

        savedFileURL.setText(resourceMap.getString("savedFileURL.text")); // NOI18N
        savedFileURL.setName("savedFileURL"); // NOI18N

        compare.setText(resourceMap.getString("compare.text")); // NOI18N
        compare.setName("compare"); // NOI18N
        compare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compareActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(compare, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(savedFileURL, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(firstReportURL, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(secondReportURL, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(savedFileChooserButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(secondReportButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(firstReportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstReportButton)
                    .addComponent(firstReportURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondReportURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondReportButton))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savedFileURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(savedFileChooserButton))
                .addGap(20, 20, 20)
                .addComponent(compare)
                .addGap(246, 246, 246))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.dramble.WestfieldClientReportApp.class).getContext().getActionMap(WestfieldClientReportView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        settingsMenuItem.setAction(actionMap.get("showSettingsBox")); // NOI18N
        settingsMenuItem.setText(resourceMap.getString("settingsMenuItem.text")); // NOI18N
        settingsMenuItem.setName("settingsMenuItem"); // NOI18N
        fileMenu.add(settingsMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        firstReportFileChooser.setDialogTitle(resourceMap.getString("firstReportFileChooser.dialogTitle")); // NOI18N
        firstReportFileChooser.setName("firstReportFileChooser"); // NOI18N

        secondReportFileChooser.setName("secondReportFileChooser"); // NOI18N

        savedFileChooser.setName("savedFileChooser"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void firstReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstReportButtonActionPerformed
        firstReportFileChooser.addChoosableFileFilter(new ExcelFilter());
        firstReportFileChooser.setAcceptAllFileFilterUsed(false);
        int returnVal = firstReportFileChooser.showOpenDialog(this.getFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            firstReportURL.setText(firstReportFileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_firstReportButtonActionPerformed

    private void secondReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondReportButtonActionPerformed
        secondReportFileChooser.addChoosableFileFilter(new ExcelFilter());
        secondReportFileChooser.setAcceptAllFileFilterUsed(false);
        int returnVal = secondReportFileChooser.showOpenDialog(this.getFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            secondReportURL.setText(secondReportFileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_secondReportButtonActionPerformed

    private String getNameOnly(String fileName) {
        String fname = "";
        String ext = "";
        int mid = fileName.lastIndexOf(".");
        return fname = fileName.substring(0, mid);
    }

    private void savedFileChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedFileChooserButtonActionPerformed
        savedFileChooser.addChoosableFileFilter(new PDFFilter());
        savedFileChooser.setAcceptAllFileFilterUsed(false);
        int returnVal = savedFileChooser.showSaveDialog(this.getFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String ext = "";
            if (!"pdf".equals(Utils.getExtension(savedFileChooser.getSelectedFile()))) {
                ext = ".pdf";
            }
            savedFileURL.setText(savedFileChooser.getSelectedFile().getAbsolutePath() + ext);
        }
    }//GEN-LAST:event_savedFileChooserButtonActionPerformed

    private void compareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compareActionPerformed
        startMyTaskAction();
    }//GEN-LAST:event_compareActionPerformed

    @Action
    public Task startMyTaskAction() {
        StartMyTask task = new StartMyTask(org.jdesktop.application.Application.getInstance());

        ApplicationContext C = getApplication().getContext();
        TaskMonitor M = C.getTaskMonitor();
        TaskService S = C.getTaskService();
        S.execute(task);
        M.setForegroundTask(task);

        return task;
    }

    private class StartMyTask extends Task<Void, Void> { // this is the Task

        StartMyTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Void doInBackground() {
            try {
                setMessage("Starting ...");
                // specific code for your task
                ExcelComparison comp = null;
                Preferences prefs = Preferences.userNodeForPackage(com.dramble.WestfieldClientReportApp.class);
                boolean columns1 = prefs.getBoolean("isFirstRowColumns1", true);
                boolean columns2 = prefs.getBoolean("isFirstRowColumns2", true);
                int primaryKey1 = prefs.getInt("primaryKey1", 2) - 1;
                int primaryKey2 = prefs.getInt("primaryKey2", 2) - 1;
                int column1 = prefs.getInt("column1", 4) - 1;
                int column2 = prefs.getInt("column2", 4) - 1;
                try {
                    comp = new ExcelComparison(firstReportURL.getText(), secondReportURL.getText(), columns1, columns2, primaryKey1, primaryKey2);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidFormatException ex) {
                    Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                }
                setMessage("Searching for missings");
                ArrayList<String[]> missingPrimaryKeys = comp.comparePrimaryKeys();
                setMessage("Searching for changes");
                ArrayList<String[]> changes = comp.compareColumn(column1, column2);
                missingPrimaryKeys.addAll(changes);
                // Get the value of the preference;
// default value is returned if the preference does not exist


                setMessage("Creating PDF with results");
                try {
                    PDDocument document = null;
                    try {
                        document = PDDocument.load(WestfieldClientReportApp.class.getResource("/com/dramble/resources/template.pdf"));
                    } catch (IOException ex) {
                        Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    int n = 1;
                    while (missingPrimaryKeys.size() > 40 * n) {
                        PDPage templatepage = (PDPage) document.getDocumentCatalog().getAllPages().get(n-1);
                        n++;
                        document.importPage(templatepage);
                    }

                    PDFont font = PDType1Font.HELVETICA_BOLD;


                    List allPages = document.getDocumentCatalog().getAllPages();

                    int count = 0;
                    int finish = missingPrimaryKeys.size();
                    for (int i = 0; i < allPages.size(); i++) {
                        PDPage page = (PDPage) allPages.get(i);
                        PDPageContentStream contentStream = null;

                        try {
                            contentStream = new PDPageContentStream(document, page, true, true);
                        } catch (IOException ex) {
                            Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        contentStream.beginText();
                        contentStream.setFont(font, 11);
                        contentStream.setNonStrokingColor(java.awt.Color.white);

                        contentStream.moveTextPositionByAmount(75, 659);
                        contentStream.drawString("What Changed");

                        contentStream.moveTextPositionByAmount(160, 0);
                        contentStream.drawString(firstReportURL.getText().substring(firstReportURL.getText().lastIndexOf('\\') + 1));

                        contentStream.moveTextPositionByAmount(160, 0);
                        contentStream.drawString(secondReportURL.getText().substring(secondReportURL.getText().lastIndexOf('\\') + 1));

                        contentStream.moveTextPositionByAmount(-160 - 150, 0);

                        contentStream.setFont(font, 10);
                        contentStream.setNonStrokingColor(java.awt.Color.black);

                        for (int j = 0; 0 < missingPrimaryKeys.size() && j < 40; j++) {
                            setProgress(count / finish);
                            String[] change = missingPrimaryKeys.remove(0);

                            contentStream.moveTextPositionByAmount(0, -14);
                            String columnHeaders1 = "";
                            if (!"missing".equals(change[1]) && !"present".equals(change[1]) && columns1) {
                                columnHeaders1 = comp.excelColumns[0].get(column1) + ": ";
                            }
                            String columnHeaders2 = "";
                            if (!"missing".equals(change[2]) && !"present".equals(change[2]) && columns2) {
                                columnHeaders2 = comp.excelColumns[1].get(column2) + ": ";
                            }
                            contentStream.drawString(change[0]);

                            contentStream.moveTextPositionByAmount(160, 0);
                            contentStream.drawString(columnHeaders1 + change[1]);

                            contentStream.moveTextPositionByAmount(160, 0);
                            contentStream.drawString(columnHeaders2 + change[2]);

                            contentStream.moveTextPositionByAmount(-160 - 160, 0);
                        }

                        contentStream.endText();
                        contentStream.close();
                    }
                    document.save(savedFileURL.getText());
                    document.close();
                } catch (COSVisitorException ex) {
                    Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                }
                File pdfFile = new File(savedFileURL.getText());

                setMessage("Opening PDF");
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException ex) {
                        Logger.getLogger(WestfieldClientReportView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
                setMessage("done");// status message
            } catch (java.lang.Exception e) {
                //specific code for exceptions
            }

            return null;
        }

        protected void succeeded() {
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton compare;
    private javax.swing.JButton firstReportButton;
    private javax.swing.JFileChooser firstReportFileChooser;
    private javax.swing.JTextField firstReportURL;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JFileChooser savedFileChooser;
    private javax.swing.JButton savedFileChooserButton;
    private javax.swing.JTextField savedFileURL;
    private javax.swing.JButton secondReportButton;
    private javax.swing.JFileChooser secondReportFileChooser;
    private javax.swing.JTextField secondReportURL;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private String[][] report1Array;
    private String[][] report2Array;
    private JDialog aboutBox;
    private JDialog settingsBox;
}
