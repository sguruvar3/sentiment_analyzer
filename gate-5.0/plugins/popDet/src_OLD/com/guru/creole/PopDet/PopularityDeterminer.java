
package com.guru.creole.PopDet;

import gate.Annotation;
import gate.AnnotationSet;
import gate.creole.ANNIEConstants;
import gate.creole.ExecutionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class PopularityDeterminer extends gate.creole.AbstractLanguageAnalyser {

  private String inputASname, outputASname;

  public String getinputASname() {
    return inputASname;
  }

  public void setinputASname(String inputASname) {
    this.inputASname = inputASname;
  }

  public String getoutputASname() {
    return outputASname;
  }

  public void setoutputASname(String outputASname) {
    this.outputASname = outputASname;
  }

  public void execute() throws ExecutionException {
    gate.Document doc = getDocument();
    //Define an array of hashMaps
    HashMap<String,String>[] mapsAr = new HashMap[4];
    
    int totalWordCount = 0;

    doc.getFeatures().clear();

    AnnotationSet inputAnnSet = (inputASname == null || inputASname.length() == 0)
            ? doc.getAnnotations()
            : doc.getAnnotations(inputASname);

    AnnotationSet outputAnnSet = (outputASname == null || outputASname.length() == 0)
            ? doc.getAnnotations()
            : doc.getAnnotations(outputASname);

    doc.getFeatures().put("Number of characters",
            new Integer(doc.getContent().toString().length()).toString());
    try {
      doc.getFeatures().put(
              "Number of tokens",
              new Integer(inputAnnSet.get(ANNIEConstants.TOKEN_ANNOTATION_TYPE)
                      .size()).toString());
    }
    catch(NullPointerException e) {
      throw new ExecutionException(
              "You need to run the English Tokenizer first!");
    }
    try {
      doc.getFeatures().put("Number of sentences", new Integer(inputAnnSet.get(ANNIEConstants.SENTENCE_ANNOTATION_TYPE).size()).toString());
    }
    catch(NullPointerException e) {
      throw new ExecutionException(
              "You need to run the Sentence Splitter first!");
    }

    // iterate through the sentences
    Iterator sentenceIterator = inputAnnSet.get(ANNIEConstants.SENTENCE_ANNOTATION_TYPE).iterator(), tokenIterator;
    int wordCount = 0;
      
      HashMap<String,Integer> map = new HashMap<String, Integer>();
      HashMap<String,Integer> goodCntrMap = new HashMap<String, Integer>();
      HashMap<String,Integer> badCntrMap = new HashMap<String, Integer>();
      HashMap<String,Integer> goodSecMap = new HashMap<String, Integer>();
      HashMap<String,Integer> badSecsMap = new HashMap<String, Integer>();
      ArrayList<String> contents=new ArrayList();//String contents="";
      ArrayList<String> goodIN=new ArrayList();
      ArrayList<String> badIN=new ArrayList();
      try{
        contents=readAsArrayList("/home/otrs/apache-tomcat-6.0.29/webapps/LBTRReport/gate-5.0/plugins/popDet/src/com/guru/creole/PopDet/ruleset_stocks.txt");
        goodIN=readAsArrayList("/home/otrs/apache-tomcat-6.0.29/webapps/LBTRReport/gate-5.0/plugins/popDet/src/com/guru/creole/PopDet/ruleset_good.txt");
        badIN=readAsArrayList("/home/otrs/apache-tomcat-6.0.29/webapps/LBTRReport/gate-5.0/plugins/popDet/src/com/guru/creole/PopDet/ruleset_bad.txt");
        System.out.println("content size "+contents.size());
        System.out.println("good size "+goodIN.size());
        System.out.println("bad size "+badIN.size());
	//To simulate exact match replace all the new-line chars with @@@
	//contents=contents.replaceAll("\\n", "@@@");
	  }
      catch(Exception e){
          e.printStackTrace();
	  }
    while(sentenceIterator.hasNext()) {
      Annotation sentenceAnnotation = (Annotation)sentenceIterator.next();
      tokenIterator = inputAnnSet.get(ANNIEConstants.TOKEN_ANNOTATION_TYPE,
              sentenceAnnotation.getStartNode().getOffset(),
              sentenceAnnotation.getEndNode().getOffset()).iterator();

      // iterate through the tokens in the current sentence
      int sentenceCount = 0;
      String word="";
      boolean secNameFound = false;
      String secName="";
      while(tokenIterator.hasNext()) {
    	
        Annotation tokenAnnotation = (Annotation)tokenIterator.next();
        if(tokenAnnotation.getFeatures().get(
                ANNIEConstants.TOKEN_KIND_FEATURE_NAME).equals("word"))
          wordCount++;
        word = (String)tokenAnnotation.getFeatures().get(
                ANNIEConstants.TOKEN_STRING_FEATURE_NAME);
          /*try {
            outputAnnSet.add(tokenAnnotation.getStartNode().getOffset(),
                    tokenAnnotation.getEndNode().getOffset(), "Goldfish",
                    gate.Factory.newFeatureMap());
          }
          catch(gate.util.InvalidOffsetException ioe) {
            throw new ExecutionException(ioe);
          }*/
        if(contents.size()!=0){
        	Boolean retcode = getMatchingIndexes(contents, word);
		if (retcode){
			if (!secNameFound){
				secName = word.toUpperCase();
				secNameFound = true;
			}
			Boolean word_present=map.containsKey(word.toUpperCase());
			if (word_present){
				int val=map.get(word.toUpperCase());
				map.put(word.toUpperCase(), val+1);
			}
			else{
				map.put(word.toUpperCase(), 1);
			}
	        }		
         }

        //To find the good words count
        if(goodIN.size()!=0){
	       Boolean retcode =getMatchingIndexes(goodIN, word);
		if (retcode){
			Boolean isPresent=goodCntrMap.containsKey(word.toUpperCase());
			if (isPresent){
				int val=goodCntrMap.get(word.toUpperCase());
				goodCntrMap.put(word.toUpperCase(), val+1);
				//promote the good sec by incrementing the val
				System.out.println("secName = " + secName);
				val=goodSecMap.get(secName);
				goodSecMap.put(secName, val+1);
			}
			else{
				goodCntrMap.put(word.toUpperCase(), 1);
				goodSecMap.put(secName, 1);
			}
	        }		
         }
        
        //To find the bad words count
        if(badIN.size()!=0){	
	       Boolean retcode =getMatchingIndexes(badIN, word);
		if (retcode){
			Boolean isPresent=badCntrMap.containsKey(word.toUpperCase());
			if (isPresent){
				int val=badCntrMap.get(word.toUpperCase());
				badCntrMap.put(word.toUpperCase(), val+1);
				//promote the good sec by incrementing the val
				val=badSecsMap.get(secName);
				badSecsMap.put(secName, val+1);
			}
			else{
				badCntrMap.put(word.toUpperCase(), 1);
				badSecsMap.put(word.toUpperCase(), 1);
			}
	        }		
         }
          sentenceCount++;
        }
      
      //Reset all the values
      secName="";
      secNameFound = false;

      sentenceAnnotation.getFeatures().put(new String("Goldfish Count"),
              new Integer(sentenceCount));

      totalWordCount += sentenceCount;
    }

    doc.getFeatures().put("No. of sentences", new Integer(totalWordCount).toString());
    doc.getFeatures().put("Number of words", new Integer(wordCount).toString());
    doc.getFeatures().put("Total \"Goldfish\" count", new Integer(totalWordCount).toString());
    doc.getFeatures().put("Map",map);
    doc.getFeatures().put("badSecsMap",badSecsMap);
    doc.getFeatures().put("badCntrMap",badCntrMap);
    doc.getFeatures().put("goodSecMap",goodSecMap);
    doc.getFeatures().put("goodCntrMap",goodCntrMap);
    
    
  }

  private static String readFile(String path) throws IOException {
		  FileInputStream stream = new FileInputStream(new File(path));
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    return Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		}
  
  public static ArrayList readAsArrayList (String path) throws java.io.FileNotFoundException, java.io.IOException
  {
      FileReader fr = new FileReader (new File(path));
      BufferedReader br = new BufferedReader (fr);
      ArrayList aList = new ArrayList (1024);
      
      String line = null;
      while (     (line = br.readLine()) != null)
      {
          aList.add(line);
      }
      
      br.close();
      fr.close();
      
      return aList;
  }
  
  public static Boolean getMatchingIndexes(List<String> list, String regex) {
	  ListIterator<String> li = list.listIterator();

	  Boolean retcode =false;
	  while(li.hasNext() && retcode == false) {
	    String next = li.next();
	    if(Pattern.matches(regex, next)) {
	      retcode = true;	
	    }
	  }

	  return retcode;
	}

  
}
