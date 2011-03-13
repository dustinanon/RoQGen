import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class QuickParse {
    private static final String[] _find  = { "src=\"", "href=\"" };
	private static String[] _resources = new String[1];
	private static QuickParse _instance = new QuickParse();
	private int _rCount = 0;
    private String _url;
	
	static {}
	
	private QuickParse() {}
	
	public static QuickParse getInstance() {
	    return _instance;
	}
	
	public void Parse(File file, String path) throws Exception {
BufferedReader br = new BufferedReader(new FileReader(file));
        
        String line = null;
        while((line = br.readLine()) != null) {
            for (String f : _find) {
                if (line.contains(f)) {
                    final int fPos = line.indexOf(f) + f.length();
                    final int ePos = line.indexOf("\"", fPos);
                    
                    String sub = null;
                    try {
                        sub = line.substring(fPos, ePos);
                    } catch (Exception e) {
                        //dunno wtf
                    }
                    
                    //filter out some crap
                    if (sub != null && !sub.startsWith("#") && !sub.startsWith("mailto:")
                            && !sub.startsWith("javascript:") && !sub.startsWith("https://") && !sub.startsWith("+")
                            && !sub.startsWith("'") && !sub.startsWith("{") && !sub.equals("/") && !sub.equals("")
                            && ((sub.startsWith("http://") && sub.contains(_url)) || sub.startsWith("/") || sub.startsWith("../"))) {
                        
                        String subpath = path;
                        
                        while (sub.startsWith("../")) {
                            sub = sub.replace("../", "");
                            subpath = subpath.substring(0, subpath.lastIndexOf("/"));
                        }
                        
                        subpath = subpath + "/";
                        
                        //is the path relative?
                        if (sub.startsWith("/")) {
                            sub = "http://" + _url + sub;
                        } else if (!sub.startsWith("http://")) {
                            //construct the absolute path
                            sub = "http://" + _url + subpath + sub;
                        }
                        
                        //make sure we don't have any duplicates
                        boolean dupe = false;
                        for (String r : _resources)
                            if (r != null && r.equals(sub)) {
                                dupe = true;
                                break;
                            }
                        
                        if (!dupe)
                            addResource(sub);
                    }
                }
            }
        }
	}
	
	public String[] getResources() {
	    //let's trim off that extra null data
	    int length = 0;
	    for (int i = _resources.length - 1; _resources[i] == null; i--) {length = i;}
	    
	    final String[] _newArr = new String[length];
	    System.arraycopy(_resources, 0, _newArr, 0, length);
        return _newArr;
	}

    private void addResource(String substring) {
        //make sure we have enough space for the new string
        if (_rCount == _resources.length) {
            final String[] _newArr = new String[_rCount * 2 + 1];
            System.arraycopy(_resources, 0, _newArr, 0, _rCount);
            _resources = _newArr;
        }
        
        _resources[_rCount++] = substring;
    }

    public void setURL(String uRL) {
        _url = uRL;
    }
}
