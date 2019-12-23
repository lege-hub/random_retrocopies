package random_pseudogenes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Random_Pseudogenes {
  public static Scanner in = new Scanner(System.in);
    public static Formatter out = new Formatter(System.out);

    public static List<String> fileToList(String file) throws FileNotFoundException {
        Scanner infx = new Scanner(new File(file));
        List<String> listData = new ArrayList<>();
        while (infx.hasNext()) {
            listData.add(infx.nextLine());
        }
        return listData;
    }

    public static void main(String[] args) throws FileNotFoundException {

        HashMap<String, String> filesKaryotype = new HashMap<>();

        filesKaryotype.put("human", "ENSP0");
        filesKaryotype.put("dog", "ENSCAFP");
        filesKaryotype.put("chimp", "ENSPTRP");
        filesKaryotype.put("mouse", "ENSMUSP");
        filesKaryotype.put("rat", "ENSRNOP");

        String filePositions = "positions.txt";

        Iterator entries = filesKaryotype.entrySet().iterator();
       
        System.out.println("Species\tRetrocopy start\tRetrocopy end"
                + "\tParental gene start\tParental gene start");
        
        while (entries.hasNext()) {
            Map.Entry species = (Map.Entry) entries.next();
            String key = (String) species.getKey();
            String parameters = (String) species.getValue();
            List<String> listLinesKaryo = fileToList(filePositions);

            String prefix = parameters;

            List<String[]> lresultsParental = new ArrayList<>();
            List<String[]> lresultsRetrocopies = new ArrayList<>();
            
            String[] vline, vlineCompare = null;
            String chr;
            for (int i = 0; i < listLinesKaryo.size(); i++) {
                vline = listLinesKaryo.get(i).split("\t");
                if (i < listLinesKaryo.size() - 1) {
                    vlineCompare = listLinesKaryo.get(i+1).split("\t");
                }
                
                boolean done = false;
                if (vline[0].startsWith(prefix)) {
                    chr = vline[1];
                    String[] vpos = new String[2];
                    String[] vpar = new String[2];
                    if (chr.equalsIgnoreCase(vlineCompare[1])) {
                        vpos[0] = vline[2];
                        vpos[1] = vline[3];
                        vpar[0] = vline[4];
                        vpar[1] = vline[5];
                        lresultsParental.add(vpos);
                        lresultsRetrocopies.add(vpar);
                        
                    } else if (!chr.equalsIgnoreCase(vlineCompare[1])) {
                        vpos[0] = vline[2];
                        vpos[1] = vline[3];
                        vpar[0] = vline[4];
                        vpar[1] = vline[5];
                        lresultsParental.add(vpos);
                        lresultsRetrocopies.add(vpar);
                        done = true;
                        
                    } else if (i == listLinesKaryo.size() - 1) {
                        vpos[0] = vline[2];
                        vpos[1] = vline[3];
                        vpar[0] = vline[4];
                        vpar[1] = vline[5];
                        lresultsParental.add(vpos);
                        lresultsRetrocopies.add(vpar);
                    }
                    if (done == true) {

                        Collections.shuffle(lresultsRetrocopies);

                        for (int j = 0; j < lresultsRetrocopies.size(); j++) {
                            vpos =  lresultsRetrocopies.get(j);
                            vpar = lresultsParental.get(j);
                            System.out.println(key +"\t"+vpos[0] + "\t" + vpos[1] + "\t" + vpar[0] + "\t" + vpar[1]);
                        }

                        lresultsParental.clear();
                        lresultsRetrocopies.clear();

                        done = false;
                    }

                }

            }

        }

    }
}

