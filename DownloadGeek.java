import java.io.IOException;
import java.util.ArrayList;

public class DownloadGeek {



    public static void main(String[] args) throws IOException, InterruptedException {


        var downloads = new ArrayList<Process>();

        for (var chin: new String[]{"-", "c"}) {
            for (var glasses: new String[]{"-", "g"}) {
                for (var hair: new String[]{"-", "h"}) {
                    for (var teeth: new String[]{"-", "t"}) {
                        var url = String.format("https://docs.oracle.com/javase/tutorial/uiswing/examples/components/CheckBoxDemoProject/src/components/images/geek/geek-%s%s%s%s.gif", chin, glasses, hair, teeth);
                        Process process = Runtime.getRuntime().exec("wsl wget " + url);
                        downloads.add(process);
                    }
                }
            }
        }


        for (var d: downloads) {
            d.waitFor();
        }
        
    }
    
}
