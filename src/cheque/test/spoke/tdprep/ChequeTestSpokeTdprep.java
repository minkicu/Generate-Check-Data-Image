/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheque.test.spoke.tdprep;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.logging.FileHandler;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;

import java.util.logging.Level;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author krissada.r
 */
public class ChequeTestSpokeTdprep extends Application {
    
    //private static final Logger LOGGER = Logger.getLogger("InfoLogging");
    private static final Logger LOGGER = Logger.getLogger(ChequeTestSpokeTdprep.class.getName());
    private static FileHandler fh = null;
    
    private final ObservableList<Record> dataList = FXCollections.observableArrayList();
    
    private static final TextField txtServer = new TextField();
    //private static final TextField txtDatabase = new TextField();
    private static final TextField txtLoginname = new TextField();
    private static final PasswordField txtPassword = new PasswordField();
    private static final TextField txtProjectTitle = new TextField();
    
    private static final TextField txtBranch = new TextField();
    
    private static final DatePicker datePicker = new DatePicker();
    private static final TableView tableView = new TableView();
    
    private static final GridPane gpDatabase = new GridPane();
    
    //private static final Label lblStatus = new Label("");
    private static final ComboBox cbProfile = new ComboBox();
    
    private static final ArrayList<chequeInfo> chequeDetail = new ArrayList();
    
    private static final ArrayList<ICAS_TBL_TXN_IMAGE> txnImage = new ArrayList();
    
    private static BufferedImage GFBGImage= new BufferedImage(700, 350, BufferedImage.TYPE_BYTE_GRAY);
    private static BufferedImage PFBGImage= new BufferedImage(810, 425, BufferedImage.TYPE_BYTE_GRAY);
    private static BufferedImage BRBGImage= new BufferedImage(1400, 700, BufferedImage.TYPE_BYTE_BINARY);
    private static BufferedImage OBGFBGImage= new BufferedImage(700, 350, BufferedImage.TYPE_BYTE_GRAY);
    
    private static String batch = new String();
    private static String seq = new String();
    private static String SODDate = new String();
    private static int SQFileId;
    
    //private static final String DIR = System.getProperty("user.dir");
    private static final String DIR = "c:\\temp";
    


    
    @Override
    public void start(Stage primaryStage) {
        
        //Date date = new Date() ;
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd_HHmmss");
        try {
            fh = new FileHandler("C:\\temp\\log_"
                + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        
            
        fh.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fh);
        
        
           
        Label lblServer = new Label("Server");
        Label lblDatabase = new Label("Database");
        Label lblLogin = new Label("Loginname");
        Label lblPassword = new Label("Password");
        
        Label lblDate = new Label("Clearing Date");
        Label lblBranch = new Label("Branch");
        Label lblProject = new Label("ProjectTitle");
        Label lblProfile = new Label("Profile");
        
        //lblStatus.setStyle("-fx-font-weight: bold");
        //lblStatus.setPadding(new Insets(15,0,0,0));
        
        cbProfile.setItems(FXCollections.observableArrayList("7011","7013","7014","7021","7023","7024","8001"));
        cbProfile.getSelectionModel().select(3);
        
        txtPassword.setPromptText("Your password");
        datePicker.setValue(LocalDate.now());
        datePicker.setPrefWidth(150);
        
        GridPane.setHalignment(lblServer, HPos.RIGHT);
        GridPane.setHalignment(lblDatabase, HPos.RIGHT);
        GridPane.setHalignment(lblLogin, HPos.RIGHT);
        GridPane.setHalignment(lblPassword, HPos.RIGHT);
        
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            //System.out.println("Selected date: " + date);
        });
        
        Button btnBrowsFile = new Button("Open CSV File ...");
        btnBrowsFile.setPrefWidth(150.0);
        btnBrowsFile.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.csv)", "*.csv");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file.canRead()) {
                    tableView.getItems().clear();
                    readCSV(file.toString());
                }
            }
            
        });
        
        
        Button btnGenerate = new Button("Generate Test Data ...");
        btnGenerate.setPrefWidth(150.0);
        
        /*
        btnGenerate.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0) {
                getConfig();
                
                if ("".equals(batch.trim())){
                    batch = datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE)+"00481192000000";
                }
                
                if ("".equals(seq.trim())){
                    seq = datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE)+"00481192000000";
                } 

                prepareData(batch,seq);
            }
        });
        
        */
                
        
        btnGenerate.setOnAction(e -> {
            ProgressForm pForm = new ProgressForm();

            // In real life this task would do something useful and return 
            // some meaningful result:
            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws InterruptedException {
                    
                    getConfig();
                
                    if ("".equals(batch.trim())){
                        batch = datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE)+"00481192000000";
                    }

                    if ("".equals(seq.trim())){
                        seq = datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE)+"00481192000000";
                    } 
                    
                    prepareData(batch,seq);
                    
                    return null ;
                }
            };

            // binds progress of progress bars to progress of task:
            pForm.activateProgressBar(task);
            

            // in real life this method would get the result of the task
            // and update the UI based on its value:
            task.setOnSucceeded(event -> {
                pForm.getDialogStage().close();
                txtServer.setDisable(false);
                txtLoginname.setDisable(false);
                txtPassword.setDisable(false);
                btnGenerate.setDisable(false);
                btnBrowsFile.setDisable(false);
                txtBranch.setDisable(false);
                txtProjectTitle.setDisable(false);
                datePicker.setDisable(false);
                cbProfile.setDisable(false);
                tableView.setDisable(false);
                
            });
            
            
            txtServer.setDisable(true);
            txtLoginname.setDisable(true);
            txtPassword.setDisable(true);
            btnGenerate.setDisable(true);
            btnBrowsFile.setDisable(true);
            txtBranch.setDisable(true);
            txtProjectTitle.setDisable(true);
            datePicker.setDisable(true);
            cbProfile.setDisable(true);
            tableView.setDisable(true);
            
            pForm.getDialogStage().show();

            Thread thread = new Thread(task);
            thread.start();
        });
        
        
        
        
        setTableView();
        
        /*
        File GFBackground = new File("C:\\temp\\Cheque_GF.jpeg");
        File PFBackground = new File("C:\\temp\\Payin_GF.jpeg");
        //File BRBackground = new File("C:\\temp\\Cheque_BR.jpeg");
        File OBGFBackground = new File("C:\\temp\\ChequeOB_GF.jpeg");
        */
        
        File GFBackground = new File(DIR+"\\Cheque_GF.jpeg");
        File PFBackground = new File(DIR+"\\Payin_GF.jpeg");
        //File BRBackground = new File("C:\\temp\\Cheque_BR.jpeg");
        File OBGFBackground = new File(DIR+"\\ChequeOB_GF.jpeg");
          
        try {
            GFBGImage = ImageIO.read(GFBackground);
            PFBGImage = ImageIO.read(PFBackground);
            BRBGImage = null;//ImageIO.read(BRBackground);
            OBGFBGImage = ImageIO.read(OBGFBackground);
        } catch (IOException ex) {
            //ex.printStackTrace();
            System.out.println(ex);
        }
        
        txtServer.setText("172.30.132.75");
        txtLoginname.setText("su");
        txtPassword.setText("ncr");
        
        VBox vbServer = new VBox(lblServer,txtServer,lblLogin,txtLoginname,lblPassword,txtPassword);
        vbServer.setSpacing(0.0);
        vbServer.setPadding(new Insets(0,5,5,5));
        
        VBox vbConfig = new VBox(lblDate,datePicker,lblBranch,txtBranch,lblProfile,cbProfile,lblProject,txtProjectTitle);
        vbConfig.setSpacing(0.0);
        vbConfig.setPadding(new Insets(5,5,5,5));
        
        VBox vbBotton = new VBox(btnBrowsFile,btnGenerate);
        vbBotton.setSpacing(10.0);
        vbBotton.setPadding(new Insets(5,5,0,5));

        gpDatabase.add(vbServer, 1, 1);
        gpDatabase.add(vbConfig, 1, 2);
        gpDatabase.add(vbBotton, 1, 3);
        
        gpDatabase.setPadding(new Insets(5, 10, 0, 0));
        
        gpDatabase.setVgap(5);
        gpDatabase.setHgap(5);
       
        
        GridPane gpMain = new GridPane();
        gpMain.add(gpDatabase, 1, 1);
        gpMain.add(tableView, 2, 1);
        //gpMain.add(lblStatus, 2, 2);
        
        gpMain.setPadding(new Insets(5, 0, 0, 0));
        
        Scene scene = new Scene(gpMain) ;
        

        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Cheque.Test.Spoke.TestDataGenerator");
        primaryStage.setWidth(1100);
        primaryStage.setHeight(450);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("Application.css").toExternalForm());
        primaryStage.show();
        
    }
    
    
    private void prepareData(String stBatch,String stUIC) {
        
        
        
        ArrayList<String> batchID = new ArrayList();
        ArrayList<String> groupID = new ArrayList();
        
        ArrayList<ICAS_TBL_TXN_DATA> txnData = new ArrayList();
        ArrayList<ICAS_TBL_TXN_BATCH> txnBatch = new ArrayList();
        
        
        ArrayList<String> intChq = new  ArrayList();
        ArrayList<String> intBatch = new  ArrayList();
        ArrayList<String> intImage = new  ArrayList();
        ArrayList<String> intSQImage = new  ArrayList();
        
        chequeInfo batchdetail=null;
        intBatch.clear();
        
        //lblStatus.setText("Preparing => ");
        //System.out.println(SODDate);
        
        if ("NULL".equals(SODDate)) {
        //if (1==1) { 
            String InsertSQL="";
            InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].ICAS_TBL_SOD";
            InsertSQL+="(WORKING_DATE,CLEARING_TYPE,START_DTM,END_DTM,CREATE_USER_ID,CREATE_DTM,LAST_UPDATE_USER_ID,LAST_UPDATE_DTM,DELETE_DTM,ARCHIVE_FLAG)";
            InsertSQL+=" VALUES ";
            InsertSQL+="('"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"','OW',GETDATE(),NULL,-1000,GETDATE(),-1000,GETDATE(),NULL,0) ";
            
            
            intBatch.add(InsertSQL);
            
            DBRun(intBatch);
            intBatch.clear();
             
        }
        
        
        chequeDetail.forEach((batch)-> {
            if(!batchID.contains(batch.sBatch))
                batchID.add(batch.sBatch);
                //System.out.println(batch.sBatch);
            
            if(!batchID.contains(batch.sGroup))
                batchID.add(batch.sGroup);
                //System.out.println(batch.sGroup);
        });
        
        
        /*
        
        for (int i=0; i<chequeDetail.size(); i++){
            batchdetail = chequeDetail.get(i);
            
            if(!batchID.contains(batchdetail.sBatch))
                batchID.add(batchdetail.sBatch);
            
            if(!groupID.contains(batchdetail.sGroup))
                groupID.add(batchdetail.sGroup);
            
        }
        */
        
        //System.out.println("Batch Size:"+batchID.size());
        //System.out.println("Group Size:"+groupID.size());
        

        for (int i=0; i<batchID.size(); i++){
            //System.out.println("BatchID:"+batchID.get(i));
            
            txnData.clear();
            String sbatchID = batchID.get(i);
            stBatch=stBatch.substring(0, 16)+padding.leftZeroPadding(Integer.valueOf(stBatch.substring(17, 22))+1 ,6);
            Integer idxBatch=0;
            ICAS_TBL_TXN_BATCH batch=new ICAS_TBL_TXN_BATCH();
            
            //System.out.println(stBatch);
            
            for (int j=0; j<groupID.size(); j++) {
                //System.out.println("GroupID:"+groupID.get(j));
                ICAS_TBL_TXN_DATA txnp = new ICAS_TBL_TXN_DATA();
                String sgroupID = groupID.get(j);
                String stGroup=stUIC.substring(0, 16)+padding.leftZeroPadding(Integer.valueOf(stUIC.substring(17, 22))+1 ,6);
                Double gAmount=0.0;
                Integer idxGp=0;
                String DepAccount="";
                
                //System.out.println(stGroup);
                
                for (int k=0; k<chequeDetail.size(); k++){
                    
                    chequeInfo recordChq;
                    recordChq=chequeDetail.get(k);
                    String schqBatchID = recordChq.sBatch;
                    String schqGroupID = recordChq.sGroup;
                    //System.out.println("Chq_BATCHID"+schqBatchID+" Chq_GroupID:"+schqGroupID);
                    ICAS_TBL_TXN_IMAGE img = new ICAS_TBL_TXN_IMAGE() ;
                    
                    if ((sbatchID.equals(schqBatchID)) && (sgroupID.equals(schqGroupID)))  {
                        //System.out.println("FOUND!!!!!! "+schqBatchID+" "+schqGroupID);
                        ICAS_TBL_TXN_DATA txnc = new ICAS_TBL_TXN_DATA();
                        idxBatch+=1;
                        idxGp+=1;
                        
                        stUIC=stUIC.substring(0, 16)+padding.leftZeroPadding(Integer.valueOf(stUIC.substring(17, 22))+1 ,6);
                        
                        //System.out.println(stUIC);
                        
                        Boolean obflag=true;
                        Integer bankNo=Integer.valueOf(chequeDetail.get(k).sBankNo);
                        
                        if (bankNo.equals(4)) {
                                obflag=false;
                        }
                        
                        
                        
                        gAmount=gAmount+Double.valueOf(recordChq.sAmount);
                        txnc.obatchID = recordChq.sBatch;
                        txnc.ogroupID = recordChq.sGroup;
                        txnc.batchID = stBatch;
                        txnc.GroupID = stGroup;
                        txnc.UID = stUIC;
                        txnc.RType = "C";
                        txnc.OBFlag = obflag;
                        txnc.InsertQry=prepareInsertChq(recordChq,stBatch,stGroup,stUIC,idxBatch.toString(),idxGp.toString());
                        txnc.Amount=Double.valueOf(recordChq.sAmount);
                        DepAccount=recordChq.sDepAccount;
                        txnData.add(txnc);
                        
                        
                        intImage.add(prepareImage(recordChq,stUIC));
                        
                        
                    }
                    
                }
                
                
                
                if ((gAmount > 0.0) && (!cbProfile.getValue().toString().equals("8001"))) {
                    
                    idxBatch+=1;
                    idxGp+=1;
                    stUIC=stUIC.substring(0, 16)+padding.leftZeroPadding(Integer.valueOf(stUIC.substring(17, 22))+1 ,6);
                    txnp.obatchID = "";
                    txnp.ogroupID = "";
                    txnp.batchID = stBatch;
                    txnp.GroupID = stGroup;
                    txnp.UID = stUIC;
                    txnp.RType = "P";
                    txnp.OBFlag = false;
                    txnp.InsertQry=prepareInsertPayin(stBatch,stGroup,stUIC,idxBatch.toString(),idxGp.toString());;
                    txnp.Amount=gAmount;

                    txnData.add(txnp);
                    
                    intImage.add(preparePayinImage(stUIC,DepAccount,gAmount));
                    
                    
                }
                                
            }
            
            

            for (int z=0; z<txnData.size();z++){
                //System.out.println(txnData.get(z).obatchID + "/" +txnData.get(z).ogroupID + "/" +txnData.get(z).batchID + " " + txnData.get(z).GroupID + " " + txnData.get(z).UID + " " + txnData.get(z).RType + " " + txnData.get(z).Amount.toString() );
                //System.out.println(txnData.get(z).InsertQry );
                intChq.add(txnData.get(z).InsertQry);
            }
            
            
            batch.BATCH_ID=stBatch;
            batch.INSTRUMNT_COUNT=idxBatch;
            batch.FOR_BRANCH_CODE=padding.leftZeroPadding(Integer.valueOf(txtBranch.getText()),4);
            batch.InsertQry=prepareInsertBatch(stBatch,idxBatch.toString());
            
            txnBatch.add(batch);
            
            

        }
        
        for (int z=0; z<txnBatch.size();z++){

            intBatch.add(txnBatch.get(z).InsertQry);
            
            if (cbProfile.getValue().toString().equals("8001")){
                intSQImage.add(prepareSQPayinImage(txnBatch.get(z).BATCH_ID));
                DBRun(intSQImage);
                intSQImage.clear();
                intBatch.add(prepareSQPayinBatch(txnBatch.get(z).BATCH_ID));
                DBRun(intSQImage);
        
            }
        }
        
        /*
        //lblStatus.setText(lblStatus.getText()+"Batch => ");
        DBRun(intBatch);
        //lblStatus.setText(lblStatus.getText()+"Data => ");
        DBRun(intChq);
        //lblStatus.setText(lblStatus.getText()+"Image => ");
        DBRun(intImage);
        //lblStatus.setText(lblStatus.getText()+"FINISH!!!");
        */
    }
    
    private String prepareSQPayinBatch(String BatchID){
        String InsertSQL="";
        SQFileId = getSQFile(BatchID);
        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_KBANK_SQ_UPLOAD_FILE]";
        InsertSQL+=" VALUES";
        InsertSQL+="('"+BatchID+"','OW',NULL,GETDATE(),1,0,0,0,1,GETDATE(),0,"+SQFileId+",1,'OLD')";
        
        //System.out.println(InsertSQL);
        
        return InsertSQL;
    }
    
    private String prepareSQPayinImage(String BatchID){
        String InsertSQL="";
        
        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_IMPORT_FILE]";
        InsertSQL+="(WORKING_DATE,CLEARING_TYPE,CLEARING_DATE,FILE_TYPE,[FILE_NAME],FILE_CONTENT,FILE_CONTENT_BINARY,FILE_PATH,FILE_SIZE,FILE_LAST_UPDATE_DTM,NUMBER_RECORD,TOTAL_AMOUNT,MERGE_STATUS,MERGE_ERROR,IMPORT_BY)";
        InsertSQL+="SELECT '"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"','OW',NULL,11,'"+BatchID+"',NULL,BulkColumn,'',0,GETDATE(),NULL,NULL,'DN',NULL,'' ";
        InsertSQL+="FROM Openrowset (Bulk 'T:\\Chequemark\\SQ_Payin.jpg', Single_Blob) as Image ";
        
        //System.out.println(InsertSQL);
        
        return InsertSQL;
    }
    
    public static class ProgressForm {
        private final Stage dialogStage;
        //private final ProgressBar pb = new ProgressBar();
        private final ProgressIndicator pin = new ProgressIndicator();

        public ProgressForm() {
            dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            // PROGRESS BAR
            final Label label = new Label();
            label.setText("Generating Data to Database");
            label.setStyle("-fx-font-weight: bold");

            //pb.setProgress(-1F);
            pin.setProgress(-1F);

            /*
            final StackPane stack = new StackPane();
            stack.getChildren().addAll(label, pin);
            stack.setAlignment(Pos.CENTER);
            */
            final VBox vb = new VBox();
            vb.setSpacing(5);
            vb.setAlignment(Pos.CENTER);
            vb.getChildren().addAll(label,pin);
            
            vb.setBackground(Background.EMPTY);

            Scene scene = new Scene(vb);
            dialogStage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            
            
        }

        public void activateProgressBar(final Task<?> task)  {
            //pb.progressProperty().bind(task.progressProperty());
            pin.progressProperty().bind(task.progressProperty());
            dialogStage.show();
        }

        public Stage getDialogStage() {
            return dialogStage;
        }
    }
    

    
    private String prepareImage(chequeInfo dt,String UIC){
        ICAS_TBL_TXN_IMAGE img=new ICAS_TBL_TXN_IMAGE();
        String InsertSQL="";
        
        if ( Integer.valueOf(dt.sBankNo) == 4 )
            img=imageDB.genGFImage(GFBGImage,BRBGImage,dt,UIC,txtProjectTitle.getText());
        else
            img=imageDB.genGFImage(OBGFBGImage,BRBGImage,dt,UIC,txtProjectTitle.getText());
             
        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_TXN_IMAGE]";
        InsertSQL+=" VALUES";
        InsertSQL+="('"+UIC+"','"+img.IMAGE1+"','"+img.IMAGE2+"','"+img.IMAGE3+"',NULL,NULL,1,'OW')";

        return InsertSQL;
    }
    
    private String preparePayinImage(String UIC,String DepAccount,Double gAmount){
        ICAS_TBL_TXN_IMAGE img=new ICAS_TBL_TXN_IMAGE();
        String InsertSQL="";
        
        img=imageDB.genPayinImage(PFBGImage,UIC,DepAccount,gAmount);
        
        
        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_TXN_IMAGE]";
        InsertSQL+=" VALUES";
        InsertSQL+="('"+UIC+"','"+img.IMAGE1+"','"+img.IMAGE2+"','"+img.IMAGE3+"',NULL,NULL,1,'OW')";

        return InsertSQL;
    }
    
    private String prepareInsertBatch(String sbatch,String Count) {
        String InsertSQL="";
        
        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_TXN_BATCH]";
        InsertSQL+="([BATCH_ID],[CLEARING_TYPE],[JOB_ID],[PROCESSING_DATE],[BATCH_IMAGE_SIZE]";
        InsertSQL+=",[INSTRUMENT_COUNT],[FOR_BANK_CODE],[FOR_BRANCH_CODE],[STATION_ID],[BATCH_STATUS]";
        InsertSQL+=",[SCAN_USER_ID],[LOCK_USER_ID],[SCAN_DTM],[BALANCE_FLAG],[DELETE_FLAG]";
        InsertSQL+=",[LAST_BATCH_FLAG],[LAST_UPDATE_USER_ID],[LAST_UPDATE_DTM],[START_TRANSFER_DTM],[FINISH_TRANSFER_DTM]";
        InsertSQL+=",[LOCK_DTM],[CLH_STATUS],[BDC_FLAG],[LOCK_APP_ID],[FORCED_FLAG],[WORKFLOW],[TOTAL_CREDIT],[TOTAL_DEBIT])";
        InsertSQL+=" VALUES ";
        InsertSQL+="('"+sbatch+"','OW',"+cbProfile.getValue().toString().trim()+",'"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"',0";
        InsertSQL+=",'"+Count+"','004','"+padding.leftZeroPadding(Integer.valueOf(txtBranch.getText()),4)+"','kWLahj31Lnm6P9vFLaOexhCYhdB/fK4udQXLSRQLVm4=','TC'";
        InsertSQL+=",-1000,NULL,GETDATE(),0,0";
        InsertSQL+=",0,-1000,GETDATE(),GETDATE(),GETDATE()";
        InsertSQL+=",NULL,NULL,0,NULL,0,NULL,NULL,NULL)";
        
        
        return InsertSQL;
    }
    
    private String prepareInsertPayin(String sbatch,String gpID,String UIC,String idxBatch,String idxGp) {
        
        
        String InsertSQL="";


        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_TXN_DATA]";
        InsertSQL+="([UIC],[CLEARING_TYPE],[REC_TYPE_ID],[BATCH_ID],[APP_STATUS]";
        InsertSQL+=",[APP_REQUIRE],[APP_ERROR_STATUS],[CLEARING_DATE],[PROCESSING_DATE]";
        InsertSQL+=",[FIELD1],[FIELD2],[FIELD3],[FIELD4],[FIELD5],[FIELD6],[FIELD7]";
        InsertSQL+=",[FIELD8],[FIELD9],[FIELD10],[FIELD11],[FIELD12],[FIELD13],[FIELD14],[FIELD15]";
        InsertSQL+=",[FIELD16],[FIELD17],[FIELD18],[FIELD19],[FIELD20]";
        InsertSQL+=",[SCAN_LINE1],[SCAN_LINE2],[JOB_ID],[TXN_GROUP_ID],[TXN_ORDER_IN_GROUP]";
        InsertSQL+=",[IMAGE_FILE1_SIZE],[IMAGE_FILE2_SIZE],[IMAGE_FILE3_SIZE],[DIGEST_MESSAGE],[IN_POOL_FLAG]";
        InsertSQL+=",[DELETE_FLAG],[ICMG_GEN_FILE_FLAG],[CBS_GEN_FILE_FLAG],[TRANSFERED_FLAG],[NCF_FLAG_LIST]";
        InsertSQL+=",[INTERNAL_FLAG_LIST],[LOCK_USER_ID],[LOCK_APP_ID],[LAST_ACTION_TYPE],[LAST_APP_ID]";
        InsertSQL+=",[LAST_UPDATE_DTM],[LAST_UPDATE_USER_ID],[TXN_ORDER_IN_BATCH],[HOST_STATUS],[DC_FLAG_LIST]";
        InsertSQL+=",[ROUTE_STATE_ID],[BRANCH_BATCH_NUMBER],[ICMG_BATCH_NUMBER],[SETTLEMENT_PERIOD],[SETTLEMENT_DATE]";
        InsertSQL+=",[DUPLICATE_FLAG],[ALLOW_DUPLICATE_FLAG],[SEND_BANK],[SEND_BRANCH],[CLEARING_HOUSE_CODE]";
        InsertSQL+=",[TXN_BATCH_ID],[REPRESENT_FLAG],[SETTLEMENT_PERIOD_RETURN],[SETTLEMENT_DATE_RETURN],[LOCK_DTM]";
        InsertSQL+=",[IN_HOUSE_FLAG],[CBS_ROUND_NUM],[ICMG_READY_FLAG],[ICMG_FORCED_GEN_FLAG],[HOST_FORCED_GEN_FLAG]";
        InsertSQL+=",[LOAD_INVENTORY_DATE],[CLH_STATUS],[VERIFY_METHOD],[ARCHIVE_FLAG],[DATA_TAG]";
        InsertSQL+=",[WORKFLOW],[REPRESENT_COUNT],[TF_DTM],[PAY_CLH1_CODE],[PAY_CLH2_CODE],[SEND_CLH1_CODE],[SEND_CLH2_CODE])";
        InsertSQL+=" VALUES ";
        InsertSQL+="('"+UIC+"','OW',1,'"+sbatch+"',0";
        InsertSQL+=",0,1,NULL,'"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"'";
        InsertSQL+=",'0.0','77','','','','',''";
        InsertSQL+=",'','','','','','','','','','','','',''";
        InsertSQL+=",'77'";
        //,'d'+@CRC+' c'+@ChequeNo+'c'+@BankNo+'-'+@BranchNo+'d '+@AccountNo+'c'+@DocType --'d92 c00038037c004-0099d 0000000000c03'
        InsertSQL+=",'77',"+cbProfile.getValue().toString().trim()+",'"+gpID+"',"+idxGp;
        InsertSQL+=",500,500,500,'',0";
        InsertSQL+=",0,0,0,0,'00'";
        InsertSQL+=",'',NULL,NULL,'IN',101020";
        InsertSQL+=",GETDATE(),-1000,"+idxBatch+",NULL,NULL";
        InsertSQL+=",NULL,NULL,NULL,NULL,NULL";
        InsertSQL+=",0,0,'004','"+padding.leftZeroPadding(Integer.valueOf(txtBranch.getText()),4)+"',NULL";
        InsertSQL+=",NULL,0,NULL,NULL,NULL";
        InsertSQL+=",0,0,0,0,0";
        InsertSQL+=",NULL,NULL,NULL,0,NULL";
        InsertSQL+=",NULL,0,GETDATE(),NULL,NULL,NULL,NULL)";

        return InsertSQL;
            
    }
    
    private String prepareInsertChq (chequeInfo schq,String sbatch,String gpID,String UIC,String idxBatch,String idxGp) {
        String InsertSQL="";
        String InhouseFlag="0";
        
        if (schq.sCRC.trim().equals(""))
            schq.sCRC = "00";
        
        if (schq.sChequeNo.trim().equals(""))
            schq.sChequeNo = "00000000";
        
        if (schq.sBankNo.trim().equals(""))
            schq.sBankNo = "000";
        
        if (schq.sBranchNo.trim().equals(""))
            schq.sBranchNo = "0000";
        
        if (schq.sAccountNo.trim().equals(""))
            schq.sAccountNo = "0000000000";
        
        if (schq.sDocType.trim().equals(""))
            schq.sDocType = "00";
        
        if (schq.sPCT.trim().equals(""))
            schq.sPCT = "00";
        
        
        schq.sCRC=padding.leftZeroPadding(Integer.valueOf(schq.sCRC),2);
        schq.sChequeNo=padding.leftZeroPadding(Long.valueOf(schq.sChequeNo),8);
        schq.sBankNo=padding.leftZeroPadding(Integer.valueOf(schq.sBankNo),3);
        schq.sBranchNo=padding.leftZeroPadding(Integer.valueOf(schq.sBranchNo),4);
        schq.sAccountNo=padding.leftZeroPadding(Long.valueOf(schq.sAccountNo),10);
        schq.sDocType=padding.leftZeroPadding(Integer.valueOf(schq.sDocType),2);
        schq.sPCT=padding.leftZeroPadding(Integer.valueOf(schq.sPCT),2);
        
        
        

        String sMICR="d"+schq.sCRC+" c"+schq.sChequeNo+"c"+ schq.sBankNo+"-"+schq.sBranchNo+"d "+schq.sAccountNo+"c"+schq.sDocType;

        if (schq.sBankNo.equals("004")) {
            InhouseFlag = "1";
        }
        
        if (cbProfile.getValue().toString().trim().equals("8001")) {
            gpID=sbatch;
            idxGp=idxBatch;
        }
        
        InsertSQL+="INSERT INTO SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].[ICAS_TBL_TXN_DATA]";
        InsertSQL+="([UIC],[CLEARING_TYPE],[REC_TYPE_ID],[BATCH_ID],[APP_STATUS]";
        InsertSQL+=",[APP_REQUIRE],[APP_ERROR_STATUS],[CLEARING_DATE],[PROCESSING_DATE]";
        InsertSQL+=",[FIELD1],[FIELD2],[FIELD3],[FIELD4],[FIELD5],[FIELD6],[FIELD7]";
        InsertSQL+=",[FIELD8],[FIELD9],[FIELD10],[FIELD11],[FIELD12],[FIELD13],[FIELD14],[FIELD15]";
        InsertSQL+=",[FIELD16],[FIELD17],[FIELD18],[FIELD19],[FIELD20]";
        InsertSQL+=",[SCAN_LINE1],[SCAN_LINE2],[JOB_ID],[TXN_GROUP_ID],[TXN_ORDER_IN_GROUP]";
        InsertSQL+=",[IMAGE_FILE1_SIZE],[IMAGE_FILE2_SIZE],[IMAGE_FILE3_SIZE],[DIGEST_MESSAGE],[IN_POOL_FLAG]";
        InsertSQL+=",[DELETE_FLAG],[ICMG_GEN_FILE_FLAG],[CBS_GEN_FILE_FLAG],[TRANSFERED_FLAG],[NCF_FLAG_LIST]";
        InsertSQL+=",[INTERNAL_FLAG_LIST],[LOCK_USER_ID],[LOCK_APP_ID],[LAST_ACTION_TYPE],[LAST_APP_ID]";
        InsertSQL+=",[LAST_UPDATE_DTM],[LAST_UPDATE_USER_ID],[TXN_ORDER_IN_BATCH],[HOST_STATUS],[DC_FLAG_LIST]";
        InsertSQL+=",[ROUTE_STATE_ID],[BRANCH_BATCH_NUMBER],[ICMG_BATCH_NUMBER],[SETTLEMENT_PERIOD],[SETTLEMENT_DATE]";
        InsertSQL+=",[DUPLICATE_FLAG],[ALLOW_DUPLICATE_FLAG],[SEND_BANK],[SEND_BRANCH],[CLEARING_HOUSE_CODE]";
        InsertSQL+=",[TXN_BATCH_ID],[REPRESENT_FLAG],[SETTLEMENT_PERIOD_RETURN],[SETTLEMENT_DATE_RETURN],[LOCK_DTM]";
        InsertSQL+=",[IN_HOUSE_FLAG],[CBS_ROUND_NUM],[ICMG_READY_FLAG],[ICMG_FORCED_GEN_FLAG],[HOST_FORCED_GEN_FLAG]";
        InsertSQL+=",[LOAD_INVENTORY_DATE],[CLH_STATUS],[VERIFY_METHOD],[ARCHIVE_FLAG],[DATA_TAG]";
        InsertSQL+=",[WORKFLOW],[REPRESENT_COUNT],[TF_DTM],[PAY_CLH1_CODE],[PAY_CLH2_CODE],[SEND_CLH1_CODE],[SEND_CLH2_CODE])";
        InsertSQL+=" VALUES ";
        InsertSQL+="('"+UIC+"','OW',2,'"+sbatch+"',0";
        InsertSQL+=",0,1,NULL,'"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"'";
        InsertSQL+=",'"+ schq.sAmount.trim()+"','"+schq.sDocType+"','"+schq.sAccountNo+"','"+schq.sBranchNo+"','"+schq.sBankNo+"','"+schq.sChequeNo+"','"+schq.sCRC+"'";
        InsertSQL+=",'','','','','','','','','','','','',''";
        InsertSQL+=",'"+sMICR+"'";
        //,'d'+@CRC+' c'+@ChequeNo+'c'+@BankNo+'-'+@BranchNo+'d '+@AccountNo+'c'+@DocType --'d92 c00038037c004-0099d 0000000000c03'
        InsertSQL+=",'"+schq.sAccountNo.substring(8, 10)+"C"+schq.sDocType+"',"+cbProfile.getValue().toString().trim()+",'"+gpID+"',"+idxGp;
        InsertSQL+=",500,500,500,'',0";
        InsertSQL+=",0,0,0,0,'"+schq.sPCT+"'";
        InsertSQL+=",'',NULL,NULL,'IN',101020";
        InsertSQL+=",GETDATE(),-1000,"+idxBatch+",NULL,NULL";
        InsertSQL+=",NULL,NULL,NULL,NULL,NULL";
        InsertSQL+=",0,0,'004','"+padding.leftZeroPadding(Integer.valueOf(txtBranch.getText()),4)+"',NULL";
        InsertSQL+=",NULL,0,NULL,NULL,NULL";
        InsertSQL+=","+InhouseFlag+",0,0,0,0";
        InsertSQL+=",NULL,NULL,NULL,0,NULL";
        InsertSQL+=",NULL,0,GETDATE(),NULL,NULL,NULL,NULL)";

        return InsertSQL;
            
    }
    
    private void getConfig() {
        
        Connection connect=null;
        String connString;
        
        try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connString = "jdbc:sqlserver://"+txtServer.getText()+";DatabaseName=master;user="+txtLoginname.getText()+";Password="+txtPassword.getText();
                connect = DriverManager.getConnection(connString);

                if(connect != null){
                    //System.out.println("Database Connected.");
                    Statement statement = connect.createStatement();

                    //SELECT ISNULL(MAX(RIGHT(BATCH_ID,6)),0),MAX(BATCH_ID) FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_TXN_BATCH] WHERE [PROCESSING_DATE]=@CAPTUREDATE AND [STATION_ID] = 'kWLahj31Lnm6P9vFLaOexhCYhdB/fK4udQXLSRQLVm4='

                    String queryString = "SELECT ISNULL(MAX(BATCH_ID),'') BATCH_ID ";
                    queryString += "FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_TXN_BATCH] ";
                    queryString += "WHERE [PROCESSING_DATE]='"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"' ";
                    queryString += "AND [STATION_ID] = 'kWLahj31Lnm6P9vFLaOexhCYhdB/fK4udQXLSRQLVm4='";
                    
                    //System.out.println("qry=>"+queryString);
                    
                    ResultSet rs = statement.executeQuery(queryString);
                    while (rs.next()) {
                           
                           batch = rs.getString(rs.findColumn("BATCH_ID"));
                           //System.out.println("qry=>"+queryString+"\nresult=>"+batch);
                           
                    }
                    
                    //ISNULL(MAX(RIGHT(UIC,6)),0) FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_TXN_DATA] WHERE BATCH_ID
                    
                    queryString = "SELECT  ISNULL(MAX(UIC),'') SEQ ";
                    queryString += "FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_TXN_DATA] ";
                    queryString += "WHERE BATCH_ID='"+batch+"'";
                    
                    //System.out.println("qry=>"+queryString);
                    
                    rs = statement.executeQuery(queryString);
                    while (rs.next()) {
                           
                           seq = rs.getString(rs.findColumn("SEQ"));
                           
                           
                    }
                    
                    queryString = "SELECT WORKING_DATE FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.[dbo].ICAS_TBL_SOD WHERE CLEARING_TYPE = 'OW'";
                    queryString += " AND WORKING_DATE = '"+datePicker.getValue().format(DateTimeFormatter.ISO_DATE)+"'";
                    
                    //System.out.println(queryString);
                    SODDate="NULL";
                    rs = statement.executeQuery(queryString);
                    
                    while (rs.next()) {
                           
                        SODDate = rs.getString(rs.findColumn("WORKING_DATE"));
                              
                    }
                    
                } else {
                    System.out.println("Database Connect Failed.");
                }

        } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            connect.close();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void DBRun(ArrayList<String> qry){
        Connection connect=null;
        String connString;
        
        try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connString = "jdbc:sqlserver://"+txtServer.getText()+";DatabaseName=master;user="+txtLoginname.getText()+";Password="+txtPassword.getText();
                connect = DriverManager.getConnection(connString);

                if(connect != null){
                    //System.out.println("Database Connected.");
                    Statement statement = connect.createStatement();

                    //SELECT ISNULL(MAX(RIGHT(BATCH_ID,6)),0),MAX(BATCH_ID) FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_TXN_BATCH] WHERE [PROCESSING_DATE]=@CAPTUREDATE AND [STATION_ID] = 'kWLahj31Lnm6P9vFLaOexhCYhdB/fK4udQXLSRQLVm4='
                    
                    qry.forEach((it)->{
                        try {
                            statement.executeUpdate(it);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE,"Sql Exception: "  
                                   + ex.getStackTrace()[1].getClassName() + "." 
                                   + ex.getStackTrace()[1].getMethodName(), ex);
                            //Logger.getLogger(ChequeTestSpokeTdprep.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } );
                    
                    
                } else {
                    System.out.println("Database Connect Failed.");
                }

        } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            connect.close();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private Integer getSQFile(String batch){
        Connection connect=null;
        String connString;
        String result;
        
        try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connString = "jdbc:sqlserver://"+txtServer.getText()+";DatabaseName=master;user="+txtLoginname.getText()+";Password="+txtPassword.getText();
                connect = DriverManager.getConnection(connString);

                if(connect != null){
                    //System.out.println("Database Connected.");
                    Statement statement = connect.createStatement();

                    //SELECT ISNULL(MAX(RIGHT(BATCH_ID,6)),0),MAX(BATCH_ID) FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_TXN_BATCH] WHERE [PROCESSING_DATE]=@CAPTUREDATE AND [STATION_ID] = 'kWLahj31Lnm6P9vFLaOexhCYhdB/fK4udQXLSRQLVm4='
                    String queryString = "SELECT [FILE_ID]  ";
                    queryString += "FROM SPOKE_CONSOLIDATE.ICAS_DATABASE.dbo.[ICAS_TBL_IMPORT_FILE] ";
                    queryString += "WHERE [FILE_NAME]='"+batch+"' ";

                    
                    //System.out.println("qry=>"+queryString);
                    
                    ResultSet rs = statement.executeQuery(queryString);
                    while (rs.next()) {
                           
                           result = rs.getString(rs.findColumn("FILE_ID"));
                           //System.out.println("qry=>"+queryString+"\nresult=>"+batch);
                           return Integer.valueOf(result);
                           
                    }
                    
                    
                } else {
                    System.out.println("Database Connect Failed.");
                }

        } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            connect.close();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return 0;
    }
    
    private void readCSV(String CsvFile) {
        
        String FieldDelimiter = ",";
        chequeDetail.clear();
        BufferedReader br;
 
        try {
            br = new BufferedReader(new FileReader(CsvFile));
 
            String line;
            while ((line = br.readLine()) != null) {
                
                if (!line.isEmpty() && !line.trim().equals("")) {
                    String[] fields = line.split(FieldDelimiter, -1);

                    Record record = new Record(fields[0], fields[1], fields[2],
                            fields[3], fields[4], fields[5], fields[6], fields[7], fields[8], fields[9], fields[10],fields[11]);
                    dataList.add(record);

                    chequeInfo chq = new chequeInfo();
                    chq.setChequeInfo(record);

                    chequeDetail.add(chq);
                }
                
            }
            tableView.setItems(dataList);
            
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        }
    }
    
    private void setTableView() {

        TableColumn columnF1 = new TableColumn("BatchNo");
        columnF1.setCellValueFactory(new PropertyValueFactory<>("f1"));
        columnF1.setPrefWidth(55);
        
        TableColumn columnF2 = new TableColumn("Group");
        columnF2.setCellValueFactory(new PropertyValueFactory<>("f2"));
        columnF2.setPrefWidth(55);
        
        TableColumn columnF3 = new TableColumn("DueDate");
        columnF3.setCellValueFactory(new PropertyValueFactory<>("f3"));
        columnF3.setPrefWidth(75);
        
        TableColumn columnF4 = new TableColumn("CRC");
        columnF4.setCellValueFactory(new PropertyValueFactory<>("f4"));
        columnF4.setPrefWidth(55);
        
        TableColumn columnF5 = new TableColumn("ChequeNumber");
        columnF5.setCellValueFactory(new PropertyValueFactory<>("f5"));
        columnF5.setPrefWidth(80);
        
        TableColumn columnF6 = new TableColumn("BkNo.");
        columnF6.setCellValueFactory(new PropertyValueFactory<>("f6"));
        columnF6.setPrefWidth(50);
        
        TableColumn columnF7 = new TableColumn("BrNo.");
        columnF7.setCellValueFactory(new PropertyValueFactory<>("f7"));
        columnF7.setPrefWidth(50);
        
        TableColumn columnF8 = new TableColumn("AccountNumber");
        columnF8.setCellValueFactory(new PropertyValueFactory<>("f8"));
        columnF8.setPrefWidth(100);
        
        TableColumn columnF9 = new TableColumn("DocType");
        columnF9.setCellValueFactory(new PropertyValueFactory<>("f9"));
        columnF9.setPrefWidth(65);
        
        TableColumn columnF10 = new TableColumn("Amount");
        columnF10.setCellValueFactory(new PropertyValueFactory<>("f10"));
        columnF10.setPrefWidth(120);
        
        TableColumn columnF11 = new TableColumn("DepositAccount");
        columnF11.setCellValueFactory(new PropertyValueFactory<>("f11"));
        columnF11.setMinWidth(100);
        
        TableColumn columnF12 = new TableColumn("PCT");
        columnF12.setCellValueFactory(new PropertyValueFactory<>("f12"));
        columnF12.setMinWidth(100);
        
        tableView.getItems().clear();
        
        
        tableView.getColumns().addAll(
                columnF1, columnF2, columnF3, columnF4, columnF5, columnF6, columnF7, columnF8, columnF9, columnF10, columnF11, columnF12);
        //tableView.setMouseTransparent(true);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
