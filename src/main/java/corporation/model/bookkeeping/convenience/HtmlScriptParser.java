package corporation.model.bookkeeping.convenience;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.Source;

public class HtmlScriptParser {
	
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine engine;
	
	public HtmlScriptParser(String data) throws IOException, ScriptException {
		MicrosoftConditionalCommentTagTypes.register();
		Source source=new Source(new StringReader(data));
		
		engine = manager.getEngineByName("JavaScript");
		engine.eval("importPackage(Packages.corporation.model.bookkeeping);");
		engine.eval("importPackage(Packages.corporation.model.bookkeeping.convenience);");
		engine.eval("importPackage(Packages.net.htmlparser.jericho);");
		

		engine.put("source", source);
		engine.put("tf", this);


	}

	public void eval(String script) throws ScriptException {
		engine.eval(new StringReader(script));
	}

	public Object get(String string) {
		
		return engine.get(string);
	}

	public ScriptEngine getEngine() {
		return engine;
	}

	public Iterator<Entry<String, Object>> getVars() {
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		Set<Entry<String, Object>> set = bindings.entrySet();
		return set.iterator();
	}
}
