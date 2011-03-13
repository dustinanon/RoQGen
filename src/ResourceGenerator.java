import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ResourceGenerator {
    static String URL = "";
    
    public static void main(String[] args) {
        //URL = "www.microsoft.com";
        URL = args[0].replace("http://", "");
        final String currentDir = System.getProperty("user.dir") + "/" + URL + "/";
        QuickParse.getInstance().setURL(URL);
        final File rootDir = new File(currentDir);
        
        try {
            traverseParse(rootDir);
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir") + "/resources.list")));
            
            String[] resources = QuickParse.getInstance().getResources();
            for (String r : resources)
                bw.write(r + "\r\n");
            
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void traverseParse(File root) throws Exception {
        if (root.isDirectory() && root.list() != null)
                for (String child : root.list())
                    traverseParse(new File(root.getAbsolutePath() + "/" + child));
        else {
            //build the relative path
            String relativePath = root.getAbsolutePath().split(URL)[1];
            relativePath = relativePath.substring(0, relativePath.lastIndexOf("/"));
            QuickParse.getInstance().Parse(root, relativePath);
        }
    }

}
