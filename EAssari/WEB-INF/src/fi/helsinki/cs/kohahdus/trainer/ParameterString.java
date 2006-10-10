package fi.helsinki.cs.kohahdus.trainer;
import java.io.*;
/*
 * ParameterString.java
 *
 * Created on 2. joulukuuta 2003, 10:47
 */



/**
 *
 * @author  laine
 * @version 
 */
public class ParameterString extends java.lang.Object {

    final static int MAX_DEPTH = 10;
    final static int NO_TAG=0;
    final static int START_TAG= 1;
    final static int END_TAG=2;
    final static int TERMINATED=3;
    
    int [] cnt;                // index counters
    String [] elName;          // element name stack
    
    int level=0;               // current depth
    int maxlevel=0;            // maximum depth reached
    int currentPos=0;          // cursorposition
    int endpos=0;              // end position of current tag
    int tagtype=0;             // type of tag
    String cName=null;         // name of current element
    char [] carr;              // the string to be scanned

    /** Creates new ParameterString */
    public ParameterString(String pData) {
        carr= pData.toCharArray();
        endpos= 0;
        cnt =new int[MAX_DEPTH];
        elName= new String[MAX_DEPTH];
        currentPos=0;
        cName=null;
        tagtype=NO_TAG;
        level=-1;
        maxlevel=-1;
    }

   
    public int nextTag() {
      //treat emty element's end tag  
      if (tagtype==TERMINATED) {
         cnt[level+1]=0;
         level--;
         tagtype=END_TAG;
         return 0;
      }
      
      // we continue if tagtype is an end_tag or a non-terminated start_tag
      int startpos = nextAppearance('<', currentPos, carr.length-1);
      if (startpos==-1) {                              
         // there are no more tags
         return -1;
      }   
      
      //  there are tags to process
      endpos= nextAppearance('>',startpos,carr.length-1);   
      if (carr[startpos+1]=='/') {
         // this is a separate end tag
         tagtype=END_TAG;
         cName= new String(carr,startpos+2,endpos-startpos-2);
         if (level>=0 && !cName.equals(elName[level])) {
             // end tag name does not correspond to the start-tag name
            currentPos=startpos;
            return -2;
         }   
         else {
            // pop up 
            currentPos=endpos; 
            cnt[level+1]=0;
            level--; 
            return 0;
         }
      }   
      else {
         // start tag to be processed 
         if (carr[endpos-1]=='/')
            tagtype=TERMINATED;
         else
            tagtype=START_TAG;
         // get the name
         int nextSpace= nextWhitespace(startpos, endpos);
         if (nextSpace<endpos-1) 
            cName= new String(carr,startpos+1,nextSpace-startpos-1);
         else {                                    //no attributes
            if (tagtype==TERMINATED) 
               cName= new String(carr,startpos+1, endpos-1-startpos-1);
            else 
               cName=new String(carr,startpos+1, endpos-startpos-1);
         } 
         currentPos= startpos+1+cName.length();
         //register the element
         level++;
         if (level>maxlevel)
            maxlevel++;
         if (elName[level]==null || !elName[level].equals(cName)) { 
            elName[level]=cName;
            cnt[level]=1;
         }
         else
            // if the previous tag on the same level had the same name it's a list and
            // the index is increased 
            cnt[level]++;
         return 0; 
      }
    }   
      
    public String getElementName() {
      if (level>=0) 
         return elName[level];
      else
         return null;
    }
  
    public int getElementIndex() {
        if (level>=0) 
         return cnt[level];
      else
         return 0;
   }
 
    
    /****
     * activates the next element with a given name
    */
    public boolean nextElementByName(String elemName) {
       boolean ready=false; 
       boolean exists=false; 
       while (!ready) {
          int c = nextTag();
          //System.out.println("CP="+currentPos);
          if (c<0) {
            ready=true;
          }
          else { 
            //  System.out.println("TP="+tagtype);
              if (tagtype==START_TAG || tagtype==TERMINATED) {
                  String en=getElementName();
                  if (en!=null && en.equals(elemName)) {
                     ready=true;
                     exists=true;
                  }         
              }    
          }
       }
       return exists;
    }   
       
    public String getAttributevalue(String aName) {
        // reads the attribute name by extracting the string between the next
        // equality symbol and the whitespace preceeding it
        
        boolean ready=false;
        int cpos=currentPos;
        int bqtpos=-1; 
        int eqtpos=-1;
        String aValue=null;
        // end tag has no attributes
        if (tagtype==END_TAG)
            return null;
        while (!ready) {
            int eqpos= nextAppearance('=',cpos,endpos);
            if (eqpos>=0) {
               int wsp= prevWhitespace(cpos,eqpos);
               if (wsp>=0) {  
                  String attrName= new String(carr,wsp+1,eqpos-wsp-1);
                  if (attrName.equals(aName)) {
                      // attribute name found
                      // may be enclosed in quotas  
                      bqtpos=nextAppearance('\"', eqpos, endpos);
                      // go get the value
                      if (bqtpos>=0) {
                         eqtpos=nextAppearance('\"',bqtpos+1,endpos);
                         if (eqtpos >= bqtpos) { 
                            aValue= new String (carr, bqtpos+1,eqtpos-bqtpos-1);
                            ready=true;
                         }   
                         else
                            ready=true;
                      } 
                      else {
                         // may be enclosed in single quotas 
                         bqtpos=nextAppearance('\'', eqpos,endpos);
                         if (bqtpos>-1) {
                            eqtpos=nextAppearance('\'',bqtpos,endpos);
                            if (eqtpos >= bqtpos) { 
                               aValue= new String (carr, bqtpos+1,eqtpos-bqtpos-1);
                               ready=true;
                            }    
                            else
                               ready=true;
                         }
                      }
                  }   
                  else       // it was not the attribute requested
                      cpos=eqpos+1;
               }
               else          // there is no whitespace in the element         
                  ready=true;
            }                //  there is no equality symbol in the element    
            else {
               ready=true;
            }   
        }
        return aValue;   // return null if value not found
    }
   
           
   
    private int nextWhitespace(int from, int upto) {   
       boolean ready=false;
       int i= from;
       while (!ready) {
          if (i>upto) {
             ready=true;
             i=-1;
          }   
          else
             if (Character.isWhitespace(carr[i]))
                ready=true;
             else
                i++;
            }    
            return i;
        }    
        
   private int prevWhitespace(int from, int upto) {   
       boolean ready=false;
       int i= upto;
       while (!ready) {
          if (i<from) {
             ready=true;
             i=-1;
          }   
          else
             if (Character.isWhitespace(carr[i]))
                ready=true;
             else
                i--;
            }    
            return i;
        }    
        
        private int nextAppearance(char c, int from, int upto) {
        boolean ready=false;
        int i= from;
        while (!ready) {
            if (i>upto) {
               ready=true;
               i=-1;
            }   
            else
               if (carr[i]==c)
                  ready=true;
               else
                  i++;
        }    
        return i;
        }    
        
        private int prevAppearance(char c, int from, int upto) {
        boolean ready=false;
        int i= upto;
        while (!ready) {
            if (i<from) {
               ready=true;
               i=-1;
            }   
            else
               if (carr[i]==c)
                  ready=true;
               else
                  i--;
            }    
            return i;
        }  
        
        public static void main (String [] args)  throws IOException {
            
            ParameterString p= new ParameterString ("<INPUT TYPE=\"TEXT\" NAME=\"TESTI\"/>");
            System.out.println("TESTING: <INPUT TYPE=\"TEXT\" NAME=\"TESTI\"/>");
            boolean ef= p.nextElementByName("INPUT");
            System.out.println("RESULT: "+ef);
            if (ef) {
                System.out.println("CURRENTPOS= "+p.currentPos);
                System.out.println("ENDPOS= "+p.endpos);
                System.out.println("CNAME= "+p.cName);
                String tp= p.getAttributevalue("TYPE");
                System.out.println("ATTR TYPE= "+tp);
                tp=p.getAttributevalue("NAME");
                System.out.println("ATTR NAME= "+tp);
            }
            p= new ParameterString("<OUTPUT TYPE=\"HTML\"   WITH=\"PRINTER \">"+
                                  "Jotain</OUTPUT><MUU JOKU=\"JOKU\">   </MUU>");
            ef=p.nextElementByName("OUTPUT");
            System.out.println("RESULT: "+ef);
            if (ef) {
                System.out.println(p.carr);
                System.out.println("CURRENTPOS= "+p.currentPos);
                System.out.println("ENDPOS= "+p.endpos);
                System.out.println("CNAME= "+p.cName);
                System.out.println("LEVEL="+p.level);
                String tp= p.getAttributevalue("WITH");
                System.out.println("ATTR OUTPUT.WITH= "+tp);
                System.out.println("CARR.LENGTH="+p.carr.length);
                System.out.println("ELNAME[0]="+p.elName[0]);
                System.out.println("CNT[0]="+ p.cnt[0]);
                ef=p.nextElementByName("MUU");
                System.out.println("RESULT: "+ef);
                tp=p.getAttributevalue("JOKU");
                System.out.println("ATTR JOKU= "+tp);
                ef=p.nextElementByName("YKS");
                System.out.println("RESULT YKS: "+ef);
           } 
        }    
}       
