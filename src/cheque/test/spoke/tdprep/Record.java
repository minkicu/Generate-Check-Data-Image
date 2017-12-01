package cheque.test.spoke.tdprep;

import javafx.beans.property.SimpleStringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author krissada.r
 */
public class Record {
    
        private SimpleStringProperty f1, f2, f3, f4, f5, f6, f7, f8, f9, f10,f11,f12;

        public String getF1() {
            return f1.get();
        }
 
        public String getF2() {
            return f2.get();
        }
 
        public String getF3() {
            return f3.get();
        }
 
        public String getF4() {
            return f4.get();
        }
 
        public String getF5() {
            return f5.get();
        }
 
        public String getF6() {
            return f6.get();
        }
        
        public String getF7() {
            return f7.get();
        }
        
        public String getF8() {
            return f8.get();
        }

        public String getF9() {
            return f9.get();
        }

        public String getF10() {
            return f10.get();
        }
        
        public String getF11() {
            return f11.get();
        }
        
        public String getF12() {
            return f12.get();
        }
        

        
        Record(String f1, String f2, String f3, String f4,
               String f5, String f6, String f7, String f8, 
               String f9, String f10, String f11, String f12) {
            this.f1 = new SimpleStringProperty(f1);
            this.f2 = new SimpleStringProperty(f2);
            this.f3 = new SimpleStringProperty(f3);
            this.f4 = new SimpleStringProperty(f4);
            this.f5 = new SimpleStringProperty(f5);
            this.f6 = new SimpleStringProperty(f6);
            this.f7 = new SimpleStringProperty(f7);
            this.f8 = new SimpleStringProperty(f8);
            this.f9 = new SimpleStringProperty(f9);
            this.f10 = new SimpleStringProperty(f10);
            this.f11 = new SimpleStringProperty(f11);
            this.f12 = new SimpleStringProperty(f12);
            
        }
}
