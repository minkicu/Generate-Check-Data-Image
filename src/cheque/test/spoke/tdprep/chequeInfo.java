/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheque.test.spoke.tdprep;

/**
 *
 * @author krissada.r
 */
public class chequeInfo {
    String sCRC="99";
    String sChequeNo="00000000";
    String sBankNo="000";
    String sBranchNo="0000";
    String sAccountNo="0000000000";
    String sDocType="00";
    String sAmount="000000000000";
    String sDepAccount="0000000000";
    String sChequeDate="99/99/9999";
    String sUIC="0000000000000000000000";
    String sPCT="00";
    String sBatch="";
    String sGroup="";
    
    public void setChequeInfo(Record d1) {
                sBatch = d1.getF1();
                sGroup= d1.getF2();
                sChequeDate = d1.getF3();
                sCRC = d1.getF4();
                sChequeNo = d1.getF5();
                sBankNo = d1.getF6();
                sBranchNo = d1.getF7();
                sAccountNo = d1.getF8();
                sDocType = d1.getF9();
                sAmount = d1.getF10();
                sDepAccount = d1.getF11();
                sPCT = d1.getF12();
    }
}