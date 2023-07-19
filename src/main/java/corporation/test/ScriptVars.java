package corporation.test;

import java.io.File;

import javax.script.*;

public class ScriptVars { 
    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        engine.eval("print('Hello World')");
        
        File f = new File("test.txt");
        // expose File object as variable to script
        engine.put("file", f);

        // evaluate a script string. The script accesses "file" 
        // variable and calls method on it
        engine.eval("print(file.getAbsolutePath())");
     // evaluate JavaScript code from given file - specified by first argument
        engine.eval(new java.io.FileReader("test.txt"));
        
    }
}
