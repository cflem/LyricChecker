import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import musixmatch.URLConnectionReader;

public class LyricChecker {
 private String name;
 private String artist;
 private boolean found;
 private ArrayList<String> bwc;
 private ArrayList<String> qwc;
 private ArrayList<String> bw;
 private ArrayList<String> qw;
 private boolean lookupFailed;
 
 public LyricChecker (String n, String a, ArrayList<String> bWords, ArrayList<String> qWords) {
  name = n;
  artist = a;
  found = false;
  bw = bWords;
  qw = qWords;
  bwc = new ArrayList<String>();
  qwc = new ArrayList<String>();
  lookupFailed = false;
 }

 public void checkLyrics () 
 {
  String lyrics = metroLookup(name, artist);
  if (lyrics == null) {
    lookupFailed = true;
    return;
  }
  found = true;
  for(int i = 0; i < bw.size(); i++) {
   String cbw = bw.get(i);
   if (lyrics.indexOf(cbw) > -1) bwc.add(cbw);
  }
  for (int i = 0; i < qw.size(); i++) {
   String cqw = qw.get(i);
   if (lyrics.indexOf(cqw) > -1) 
   {
     if(cqw.equals(" ass "))
       cqw = "ass";
     qwc.add(cqw);
   }
  }
 }
 
 public boolean found () 
 {
  return found;
 }
 
 public static String metroLookup (String song, String artist) 
 {
     try {
     String html = URLConnectionReader.getText("http://www.metrolyrics.com/printlyric/"+processMetro(song)+"-lyrics-"+processMetro(artist)+".html");
     Scanner scan = new Scanner(html);
     scan.useDelimiter("<p class=\"lyrics-body\"><p class='verse'>");
     scan.next();
     scan.useDelimiter("</div>");
     String lookedup = scan.next();
     scan.close();
     return lookedup;
   } catch (IOException e) {
     return null;
   }
 }
 
 private static String processMetro (String str) {
  str = str.replaceAll("\'","");
  str = str.replaceAll(",","");
  str = str.replace("?","");
  str = str.replaceAll(" &","");  //to "" or "and"??
  str = str.replaceAll("\\.","");
  str = str.replaceAll("!","");
  str = str.replace("[","");
  str = str.replace("]","");
  str = str.replaceAll("#","");
  str = str.replace("(","");
  str = str.replace(")","");
  str = str.replaceAll(" - ","-");
  str = str.toLowerCase();
  str = str.replaceAll("\\s","-");
  int feat = str.indexOf("-feat");
  if(feat > 0)
    str = str.substring(0, feat);
  System.out.println(str);
  return str;
 }


 public void addBWord(String s) {
  bw.add(s.toLowerCase());
 }

 public void removeBWord(String s) {
  bw.remove(s.toLowerCase());
 }

 public void addQWord(String s) {
  qw.add(s.toLowerCase());
 }

 public void removeQWord(String s) {
  qw.remove(s.toLowerCase());
 }
 
 public ArrayList<String> foundBadWords () {
  return bwc;
 }
 
 public ArrayList<String> foundQWords () {
  return qwc;
 }
 
 public boolean hasBadWords() {
  return bwc.size() > 0;
 }
 
 public boolean hasQWords() {
  return qwc.size() > 0;
 }
 
 public boolean lookupFailed(){
   return lookupFailed;
 }
 public static void main(String[] args)
 {
   try{
   System.out.println(URLConnectionReader.getText("http://www.metrolyrics.com/printlyric/windowsill-lyrics-arcade-fire.html"));
   }
   catch(IOException e){
     System.out.println("failed");
   }
 }
}